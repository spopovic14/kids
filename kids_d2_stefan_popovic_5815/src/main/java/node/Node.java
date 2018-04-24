package node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import data.PipelineId;

/**
 * Represents a single node in the system.
 * @author stefan
 *
 */
public abstract class Node {
	
	/**
	 * Id that will be given to the next node
	 */
	private static int nextId = 0;
	
	/**
	 * Unique identifier of this node
	 */
	private final int id;
	
	/**
	 * Number of threads used by this node. Can be changed
	 */
	private AtomicInteger threadCount;
	
	/**
	 * A map of parameters used by this node
	 */
	private HashMap<String, NodeParameter<?>> parameters;
	
	/**
	 * ExecutorService used for starting tasks
	 */
	private ExecutorService executorService;
	
	/**
	 * List of tasks for this node. Each task is a separate thread
	 */
	private List<Task> taskList;
	
	/**
	 * Indicates whether this node is currently running or not
	 */
	private boolean running;
	
	/**
	 * Every bit represents the status of the thread with id (bit position - 1)
	 * (left to right). 1 means that the thread is working and 0 means that
	 * it's waiting. If this number is 0, then every thread is waiting,
	 * and if it's greater than 0, at least one thread is working
	 */
	private AtomicInteger threadStatuses;
	
	/**
	 * @param threadCount should be greater than or equal to 2 (defaults to 2 if not)
	 */
	public Node(int threadCount) {
		this.id = nextId++;
		
		this.threadCount = new AtomicInteger();
		setThreadCount(threadCount);
		this.parameters = new HashMap<String, NodeParameter<?>>();
		this.executorService = Executors.newCachedThreadPool();
		this.taskList = new LinkedList<Node.Task>();
		this.threadStatuses = new AtomicInteger(0);
		
		fillTaskList();
	}
	
	/**
	 * Should return an array of names of all the required parameters for this node.
	 * @return
	 */
	public abstract String[] getRequiredParameterNames();
	
	/**
	 * Returns the current nodes name
	 * @return
	 */
	public abstract String getNodeName();
	
	/**
	 * Does a work cycle for one thread in this node. Since the thread count can be
	 * reduced by the user at any time, this method should not be blocking (or it
	 * should have a timeout for actions that are blocking) so the thread can
	 * finish the current cycle before terminating. This method should also
	 * change the thread status depending on what it does (not working if
	 * there is no input and working if it's being processed)
	 * @param threadID
	 * @throws Exception
	 */
	protected abstract void doWorkCycle(int threadID) throws Exception;
	
	/**
	 * Gets called when a thread starts. Should be used for initialization of
	 * needed resources such as a socket.
	 * @param threadID
	 */
	protected abstract void onThreadStart(int threadID);
	
	/**
	 * Gets called when a thread terminates. Should be used for closing resources
	 * used by the thread.
	 * @param threadID
	 */
	protected abstract void onThreadFinish(int threadID);
	
	/**
	 * Gets called when an exception occurs on any thread. The outcome is different
	 * depending on the type of this node.
	 * @param e
	 */
	protected abstract void onThreadException(Exception e);
	
	/**
	 * Should return the next available resource id. Is used when creating PipelineId
	 * objects. Needs to return a unique value for this object.
	 * @return
	 */
	protected abstract int getNextResourceId();
	
	/**
	 * Starts this node. Throws a RuntimeException if there are required parameters
	 * that haven't been supplied
	 */
	public final void start() {
		String[] requiredParameterNames = getRequiredParameterNames();
		for(String parameterName : requiredParameterNames) {
			if(!parameters.containsKey(parameterName)) {
				throw new RuntimeException(
					getNodeName() + ": parameter " + parameterName + " is required"
				);
			}
		}
		
		startTasks();
		running = true;
	}
	
	/**
	 * Adds the parameter with parameterName to the parameter list
	 * @param parameterName
	 * @param parameter
	 */
	public final void addParameter(String parameterName, NodeParameter<?> parameter) {
		if(!running) {
			parameters.put(parameterName, parameter);
		}
	}
	
	/**
	 * Returns the value of a parameter by its name. Returns null if there is no
	 * parameter with that name
	 * @param parameterName
	 * @return
	 */
	protected final Object getParameterValue(String parameterName) {
		if(!parameters.containsKey(parameterName)) {
			return null;
		}
		
		return parameters.get(parameterName).getValue();
	}
	
	/**
	 * Sets the number of threads for this node. This number cannot be less than 2
	 * (it defaults to 2 if a value less than 2 is passed as an argument)
	 * @param newCount should be greater than or equal to 2 (defaults to 2 if not)
	 */
	public final void setThreadCount(int newCount) {	
		if(newCount < 2) {
			newCount = 2;
		}
		
		threadCount.set(newCount);
		
		if(running) {
			fillTaskList();
			terminateTasks();
			startTasks();
		}
	}
	
	/**
	 * Gets the status of the node. Returns ACTIVE if it's working, WAITING
	 * if all the threads are waiting for work and STOPPED if the node was
	 * stopped (because of an error or another reason)
	 * @return
	 */
	public NodeStatus getStatus() {
		if(!running) {
			return NodeStatus.STOPPED;
		}
		
		return threadStatuses.get() == 0 ? NodeStatus.WAITING : NodeStatus.ACTIVE;
	}
	
	/**
	 * Standard thread count getter
	 * @return
	 */
	public final int getThreadCount() {
		return threadCount.get();
	}
	
	/**
	 * Creates a PipelineId using this node's id and the next resource id
	 * @return
	 */
	public final PipelineId createPipelineId() {
		return new PipelineId(getId(), getNextResourceId());
	}
	
	/**
	 * Terminates this node. Sends the termination signal to all tasks. If the
	 * timeout is done before they can finish, they will be stopped immediately
	 * @param timeout
	 * @param unit
	 */
	public final void terminate(long timeout, TimeUnit unit) {
		threadCount = new AtomicInteger(0);
		terminateTasks();
		
		try {
			if(!executorService.awaitTermination(timeout, unit)) {
				executorService.shutdown();
			}
		} catch (InterruptedException e) {
			executorService.shutdown();
		}
	}
	
	/**
	 * Fills the task list with new tasks, up until there are threadCount tasks in it
	 */
	private void fillTaskList() {
		for(int i = taskList.size(); i < threadCount.get(); i++) {
			Task task = new Task(i);
			taskList.add(task);
		}
	}
	
	/**
	 * Starts all tasks in the task list that aren't running already
	 */
	private void startTasks() {
		for(Task task : taskList) {
			if(!task.isRunning()) {
				executorService.submit(task);
			}
		}
	}
	
	/**
	 * Sends the terminate signal to all the tasks and clears the task list
	 */
	private void terminateTasks() {
		if(threadCount.get() >= taskList.size()) {
			return;
		}
		
		List<Task> tasksToTerminate = taskList.subList(threadCount.get(), taskList.size());
		
		for(Task task : tasksToTerminate) {
			task.terminate();
		}
		
		tasksToTerminate.clear();
	}
	
	/**
	 * Sets the staus of the thread to 1 (if working) or 0 (if not working)
	 * @param threadId
	 * @param working
	 */
	protected final void setThreadStatus(int threadId, boolean working) {
		if(working) {
			threadStatuses.accumulateAndGet(threadId, (x, y) -> x | (1 << y));
		}
		else {
			threadStatuses.accumulateAndGet(threadId, (x, y) -> x & ~(1 << y));
		}
	}
	
	/**
	 * Calls onThreadException and terminates all the threads immediately
	 * @param threadId
	 * @param e
	 */
	private void catchThreadException(int threadId, Exception e) {
		onThreadException(e);
		terminate(0, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Id getter
	 * @return
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Represents a single thread that does work for a single node. Each task has its
	 * own ID
	 * @author stefan
	 *
	 */
	private class Task implements Runnable {
		
		/**
		 * Id of the current task
		 */
		private int id;
		
		/**
		 * Is true when the task should terminate as soon as possible
		 */
		private boolean terminated;
		
		/**
		 * Indicates whether this task is running
		 */
		private boolean running;
		
		/**
		 * Creates a task with the given id
		 * @param id
		 */
		public Task(int id) {
			this.id = id;
		}

		/**
		 * Gets called on a new thread
		 */
		public void run() {
			running = true;
			onThreadStart(id);
			
			while(!terminated) {
				try {
					doWorkCycle(id);
				} catch (Exception e) {
					catchThreadException(id, e);
					break;
				}
			}
			
			onThreadFinish(id);
			running = false;
		}
		
		/**
		 * Signals that this task should be terminated as soon as possible
		 */
		public void terminate() {
			setThreadStatus(id, false);
			terminated = true;
		}
		
		/**
		 * Getter for running
		 * @return
		 */
		public boolean isRunning() {
			return running;
		}
		
	}
	
}

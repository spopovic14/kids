package node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import data.PipelineCollection;

public abstract class WorkerNode extends Node {
	
	/**
	 * List of input nodes for this worker node
	 */
	protected List<InputNode> inputNodes;
	
	/**
	 * List of output nodes for this worker node
	 */
	protected List<OutputNode> outputNodes;
	
	/**
	 * The WorkerNode before this node. Can be null
	 */
	protected WorkerNode previousWorkerNode;
	
	/**
	 * The WorkerNode after this node. Can be null
	 */
	protected WorkerNode nextWorkerNode;
	
	/**
	 * The combined input of this node
	 */
	protected BlockingQueue<PipelineCollection> input;
	
	/**
	 * Constructor for WorkerNode that instantiates ArrayLists as the input and output node lists
	 * @param threadCount
	 */
	public WorkerNode(int threadCount) {
		super(threadCount);
		inputNodes = new ArrayList<>();
		outputNodes = new ArrayList<>();
		input = new LinkedBlockingQueue<>();
	}

	@Override
	protected void onThreadException(Exception e) {
		throw new RuntimeException(getNodeName() + " [" + getId() + "] Node exception: " + e.getMessage());
	}
	
	/**
	 * Adds an InputNode to this node's input nodes and sets that node's output
	 * queue to this node's input queue
	 * @param node
	 */
	public void addInputNode(InputNode node) {
		if(!inputNodes.contains(node)) {
			inputNodes.add(node);
			node.setOutput(input);
		}
	}
	
	/**
	 * Adds an OutputNode to this node's input nodes
	 * @param node
	 */
	public void addOuputNode(OutputNode node) {
		if(!outputNodes.contains(node)) {
			outputNodes.add(node);
		}
	}
	
	/**
	 * Setter for previousNode. Also sets that node's nextNode to this node
	 * @param node
	 */
	public void setPreviousWorkerNode(WorkerNode node) {
		previousWorkerNode = node;
		node.setNextWorkerNode(this);
	}
	
	/**
	 * Setter for nextNode. Also sets that node's previousNode to this node
	 * @param node
	 */
	public void setNextWorkerNode(WorkerNode node) {
		nextWorkerNode = node;
		node.setPreviousWorkerNode(this);
	}
	
	/**
	 * Getter for input
	 * @return
	 */
	public BlockingQueue<PipelineCollection> getInput() {
		return input;
	}
	
	public List<InputNode> getInputNodes() {
		return inputNodes;
	}
	
	public List<OutputNode> getOutputNodes() {
		return outputNodes;
	}
	
	/*
	 * ========================================================================
	 *                           Input node methods
	 * ========================================================================
	 */
	
	/**
	 * Retrieves and removes the head of the input queue, waiting if necessary
	 * until an element becomes available.
	 * @return
	 * @throws InterruptedException
	 */
	protected PipelineCollection inputTake() throws InterruptedException {
		return input.take();
	}
	
	/**
	 * Retrieves, but does not remove, the head of the input queue, or returns
	 * null if this queue is empty.
	 * @return
	 */
	protected PipelineCollection inputPeek() {
		return input.peek();
	}
	
	/**
	 * Retrieves and removes the head of the input queue, or returns null if
	 * this queue is empty.
	 * @return
	 */
	protected PipelineCollection inputPoll() {
		return input.poll();
	}
	
	/**
	 * Retrieves and removes the head of the input queue, waiting up to the
	 * specified wait time if necessary for an element to become available.
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	protected PipelineCollection inputPoll(long timeout, TimeUnit unit) throws InterruptedException {
		return input.poll(timeout, unit);
	}
	
	/*
	 * ========================================================================
	 *                           Output node methods
	 * ========================================================================
	 */
	
	/**
	 * Inserts the specified element into the output queue, waiting if necessary
	 * for space to become available.
	 * @param data
	 * @throws InterruptedException
	 */
	protected void put(BlockingQueue<PipelineCollection> output, PipelineCollection data) throws InterruptedException {
		output.put(data);
	}
	
	/**
	 * Inserts the specified element into the output queue if it is possible to
	 * do so immediately without violating capacity restrictions, returning
	 * true upon success and false if no space is currently available.
	 * @param data
	 * @return
	 */
	protected boolean offer(BlockingQueue<PipelineCollection> output, PipelineCollection data) {
		return output.offer(data);
	}
	
	/**
	 * Inserts the specified element into the output queue, waiting up to the
	 * specified wait time if necessary for space to become available.
	 * @param data
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	protected boolean offer(BlockingQueue<PipelineCollection> output, PipelineCollection data, long timeout, TimeUnit unit) throws InterruptedException {
		return output.offer(data, timeout, unit);
	}
	
	protected void outputAll(PipelineCollection data) throws InterruptedException {
		if(nextWorkerNode != null) {
			nextWorkerNode.getInput().put(data);
		}
		
		for(OutputNode output : outputNodes) {
			put(output.getInput(), data);
		}
	}

}

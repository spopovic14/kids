package node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import data.PipelineCollection;

public abstract class InputNode extends Node {
	
	/**
	 * The output of this node
	 */
	private BlockingQueue<PipelineCollection> output;

	/**
	 * InputNode constructor. Instantiates a LinkedBlockingQueue as this nodes output
	 * @param threadCount
	 */
	public InputNode(int threadCount) {
		super(threadCount);
		output = new LinkedBlockingQueue<PipelineCollection>();
	}
	
	/**
	 * Inserts the specified element into the output queue, waiting if necessary
	 * for space to become available.
	 * @param data
	 * @throws InterruptedException
	 */
	protected void put(PipelineCollection data) throws InterruptedException {
		output.put(data);
	}
	
	/**
	 * Inserts the specified element into the output queue if it is possible to
	 * do so immediately without violating capacity restrictions, returning
	 * true upon success and false if no space is currently available.
	 * @param data
	 * @return
	 */
	protected boolean offer(PipelineCollection data) {
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
	protected boolean offer(PipelineCollection data, long timeout, TimeUnit unit) throws InterruptedException {
		return output.offer(data, timeout, unit);
	}
	
	/**
	 * Returns the output queue of this node
	 * @return
	 */
	public BlockingQueue<PipelineCollection> getOutput() {
		return output;
	}

}

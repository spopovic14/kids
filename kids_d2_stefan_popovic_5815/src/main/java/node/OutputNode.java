package node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import data.PipelineCollection;

public abstract class OutputNode extends Node {
	
	/**
	 * The input for this OutputNode
	 */
	private BlockingQueue<PipelineCollection> input;

	/**
	 * Constructor that takes a BlockingQueue as an input
	 * @param threadCount
	 * @param input
	 */
	public OutputNode(int threadCount) {
		super(threadCount);
		input = new LinkedBlockingQueue<>();
	}
	
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
	
	/**
	 * Returns the input queue of this node
	 * @return
	 */
	public BlockingQueue<PipelineCollection> getInput() {
		return input;
	}
	
	public void setInput(BlockingQueue<PipelineCollection> input) {
		this.input = input;
	}
	
}

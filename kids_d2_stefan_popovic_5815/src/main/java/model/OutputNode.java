package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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
	public OutputNode(int threadCount, BlockingQueue<PipelineCollection> input) {
		super(threadCount);
		this.input = input;
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
	
}

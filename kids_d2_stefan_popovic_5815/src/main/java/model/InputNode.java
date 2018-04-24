package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class InputNode extends Node {
	
	protected BlockingQueue<PipelineCollection> outputQueue;

	public InputNode(int threadCount) {
		super(threadCount);
		outputQueue = new LinkedBlockingQueue<PipelineCollection>();
	}
	
	public PipelineCollection getOutputBlocking() throws InterruptedException {
		return outputQueue.take();
	}
	
	public PipelineCollection getOutput() {
		return outputQueue.poll();
	}
	
	public PipelineCollection getOutput(long timeout, TimeUnit unit) throws InterruptedException {
		return outputQueue.poll(timeout, unit);
	}
	
	public PipelineCollection peekOutput() {
		return outputQueue.peek();
	}

}

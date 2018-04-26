package data;

import java.util.concurrent.TimeUnit;

public interface PipelineCollection {
	
	public PipelineId getId();
	
	public PipelineData peek(PipelineId id);
	
	public PipelineData poll(long timeout, TimeUnit unit) throws InterruptedException;
	
	public PipelineData take() throws InterruptedException;
	
	public void put(PipelineData data) throws InterruptedException;
	
}

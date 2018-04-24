package model;

public interface PipelineCollection {
	
	public PipelineId getId();
	
	public PipelineData peek(PipelineId id);
	
	public PipelineData take() throws InterruptedException;
	
	public void put(PipelineData data) throws InterruptedException;
	
}

package model;

public interface PipelineCollection {
	
	public PipelineId getId();
	
	public PipelineData peek(PipelineId id);
	
	public PipelineData take(PipelineId id);
	
	public void put(PipelineData data);
	
}

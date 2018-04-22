package model;

public interface PipelineCollection {
	
	public PipelineID getID();
	
	public PipelineData peek(PipelineID id);
	
	public PipelineData take(PipelineID id);
	
	public void put(PipelineData data);
	
}

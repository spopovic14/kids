package model;

public interface PipelineData {
	
	public PipelineID getID();
	
	public Object getValue(String key);
	
	public void setValue(String key, Object value);
	
}

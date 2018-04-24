package model;

public interface PipelineData {
	
	public PipelineId getId();
	
	public Object getValue(String key);
	
	public void setValue(String key, Object value);
	
}

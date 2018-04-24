package data;

import java.util.HashMap;

public class GenericPipelineData implements PipelineData {
	
	private PipelineId id;
	
	private HashMap<String, Object> values;
	
	public GenericPipelineData(PipelineId id) {
		this.id = id;
		this.values = new HashMap<String, Object>();
	}

	public PipelineId getId() {
		return id;
	}

	public Object getValue(String key) {
		return values.get(key);
	}

	public void setValue(String key, Object value) {
		values.put(key, value);
	}

}

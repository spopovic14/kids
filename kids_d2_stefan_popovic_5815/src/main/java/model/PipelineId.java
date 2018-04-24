package model;

public class PipelineId {
	
	private String id;
	
	public PipelineId(int nodeId, int nodeResourceId) {
		this.id = nodeId + "-" + nodeResourceId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PipelineId)) {
			return false;
		}
		
		return id.equals(((PipelineId)obj).id);
	}
	
	public String getId() {
		return id;
	}
	
}

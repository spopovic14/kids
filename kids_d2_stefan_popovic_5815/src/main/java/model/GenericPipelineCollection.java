package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GenericPipelineCollection implements PipelineCollection {
	
	private PipelineId id;
	
	private BlockingQueue<PipelineData> queue;
	
	public GenericPipelineCollection(PipelineId id) {
		this.id = id;
		this.queue = new LinkedBlockingQueue<PipelineData>();
	}

	public PipelineId getId() {
		return id;
	}

	public PipelineData peek(PipelineId id) {
		for(PipelineData data : queue) {
			if(data.getId().equals(id)) {
				return data;
			}
		}
		
		return null;
	}

	public PipelineData take() throws InterruptedException {
		return queue.take();
	}

	public void put(PipelineData data) throws InterruptedException {
		queue.put(data);
	}

}

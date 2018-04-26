package example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import data.GenericPipelineCollection;
import data.PipelineCollection;
import data.PipelineData;
import node.WorkerNode;

public class RangeSplitter extends WorkerNode {
	
	private AtomicInteger nextResourceId = new AtomicInteger(0);
	
	/**
	 * True if one thread should each process a single collection, false if threads
	 * should work on one big collection together
	 */
	private boolean collectionEach = true;
	
	private String splitKey;

	public RangeSplitter(int threadCount) {
		super(threadCount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Pair<String, Class<?>>> getRequiredParameterNames() {
		List<Pair<String, Class<?>>> list = new ArrayList<>();
		
		list.add(new ImmutablePair<String, Class<?>>("Split-Key", String.class));
		
		return list;
	}

	@Override
	public String getNodeName() {
		return "RangeSplitter";
	}

	@Override
	protected void onNodeStart() throws Exception {
		splitKey = getParameterValue("Split-Key").toString();
	}

	@Override
	protected void doWorkCycle(int threadId) throws Exception {
		PipelineCollection lessThanTen = new GenericPipelineCollection(createPipelineId());
		PipelineCollection tenOrMore = new GenericPipelineCollection(createPipelineId());
		
		if(collectionEach) {
			PipelineCollection collection = input.take();
			PipelineData data;
			
			while((data = collection.poll(0, TimeUnit.MILLISECONDS)) != null) {
				System.out.println(data.getValue(splitKey) + " = value");
				if((int)data.getValue(splitKey) < 10) {
					lessThanTen.put(data);
					System.out.println(getId() + " LESS");
				}
				else {
					tenOrMore.put(data);
					System.out.println(getId() + " MORE");
				}
			}
		}
		
		System.out.println("Splitter");
		
		outputAll(lessThanTen);
		outputAll(tenOrMore);
		
		Thread.sleep(2300);
	}

	@Override
	protected void onThreadStart(int threadId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onThreadFinish(int threadId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getNextResourceId() {
		return nextResourceId.incrementAndGet();
	}

}

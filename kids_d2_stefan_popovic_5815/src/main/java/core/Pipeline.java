package core;

import java.util.ArrayList;
import java.util.List;

import node.WorkerNode;

public class Pipeline {
	
	private List<WorkerNode> workerNodes;
	
	public Pipeline() {
		workerNodes = new ArrayList<>();
	}
	
	public void start() {
		for(WorkerNode node : workerNodes) {
			node.start();
		}
	}
	
	public void addWorkerNode(WorkerNode node) {
		if(!workerNodes.contains(node)) {
			workerNodes.add(node);
		}
	}
	
	public WorkerNode getWorkerNodeAt(int index) {
		return workerNodes.get(index);
	}
	
	public List<WorkerNode> getWorkerNodes() {
		return workerNodes;
	}
	
}

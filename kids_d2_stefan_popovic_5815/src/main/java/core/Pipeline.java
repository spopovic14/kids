package core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import node.InputNode;
import node.OutputNode;
import node.WorkerNode;

public class Pipeline {
	
	private List<WorkerNode> workerNodes;
	
	public Pipeline() {
		workerNodes = new ArrayList<>();
	}
	
	public void start() {
		for(WorkerNode node : workerNodes) {
			for(InputNode input : node.getInputNodes()) {
				input.start();
			}
			
			node.start();
			
			for(OutputNode output : node.getOutputNodes()) {
				output.start();
			}
		}
	}
	
	public void stop() {
		for(WorkerNode node : workerNodes) {
			for(InputNode input : node.getInputNodes()) {
				input.terminate(0, TimeUnit.MILLISECONDS);
			}
			
			node.terminate(0, TimeUnit.MILLISECONDS);
			
			for(OutputNode output : node.getOutputNodes()) {
				output.terminate(0, TimeUnit.MILLISECONDS);
			}
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

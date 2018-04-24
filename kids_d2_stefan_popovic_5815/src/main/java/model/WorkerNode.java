package model;

import java.util.ArrayList;
import java.util.List;

public abstract class WorkerNode extends Node {
	
	/**
	 * List of input nodes for this worker node
	 */
	protected List<InputNode> inputNodes;
	
	/**
	 * List of output nodes for this worker node
	 */
	protected List<OutputNode> outputNodes;
	
	/**
	 * Constructor for WorkerNode that instantiates ArrayLists as the input and output node lists
	 * @param threadCount
	 */
	public WorkerNode(int threadCount) {
		super(threadCount);
		inputNodes = new ArrayList<>();
		outputNodes = new ArrayList<>();
	}

	@Override
	protected void onThreadException(Exception e) {
		throw new RuntimeException(getNodeName() + " [" + getId() + "] Node exception: " + e.getMessage());
	}
	
	/**
	 * Adds an InputNode to this node's input nodes
	 * @param node
	 */
	public void addInputNode(InputNode node) {
		if(!inputNodes.contains(node)) {
			inputNodes.add(node);
		}
	}
	
	/**
	 * Adds an OutputNode to this node's input nodes
	 * @param node
	 */
	public void addOuputNode(OutputNode node) {
		if(!outputNodes.contains(node)) {
			outputNodes.add(node);
		}
	}

}

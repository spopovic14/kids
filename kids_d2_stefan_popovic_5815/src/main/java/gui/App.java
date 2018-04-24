package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import core.Pipeline;
import node.WorkerNode;

@SuppressWarnings("serial")
public class App extends JFrame {
	
	private Pipeline pipeline;
	
	private JPanel nodePanel;

	public App() {
		super("Stefan Popovic RN58/15");
	}
	
	public void start() {
		initialize();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initialize() {
		pipeline = new Pipeline();
		
		setLayout(new BorderLayout());
		
		nodePanel = new JPanel();
		
		JPanel pan2 = new JPanel();
		pan2.setBackground(Color.RED);
		
		JScrollPane scroll = new JScrollPane(nodePanel);
		
		add(scroll, BorderLayout.CENTER);
		add(pan2, BorderLayout.SOUTH);
		
		JButton button = new JButton("Start");
		pan2.add(button);
		
		
		setSize(800, 600);
	}
	
	public void addWorkerNode(WorkerNode node) {
		pipeline.addWorkerNode(node);
		WorkerNodePane pane = new WorkerNodePane(node);
		nodePanel.add(pane);
	}
	
	public static void main(String[] args) {
		App app = new App();
		app.start();
		
		app.addWorkerNode(new WorkerNode(2) {
			
			@Override
			protected void onThreadStart(int threadID) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onThreadFinish(int threadID) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public List<Pair<String, Class<?>>> getRequiredParameterNames() {
				List<Pair<String, Class<?>>> list = new LinkedList<>();
				list.add(new ImmutablePair<>("FirstParameter", String.class));
				return list;
			}
			
			@Override
			public String getNodeName() {
				// TODO Auto-generated method stub
				return "SqlReader";
			}
			
			@Override
			protected int getNextResourceId() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			protected void doWorkCycle(int threadID) throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}

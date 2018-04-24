package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import node.NodeParameter;
import node.Parameter;
import node.WorkerNode;

@SuppressWarnings("serial")
public class WorkerNodePane extends JPanel {
	
	private WorkerNode node;
	
	private JLabel idLabel;
	
	private JLabel threadsLabel;
	
	public WorkerNodePane(WorkerNode node) {
		this.node = node;
		
		setLayout(new FlowLayout());
		
		JPanel idPanel = new JPanel();
		idPanel.setSize(300, 100);
		
		idLabel = new JLabel(node.getId() + " " + node.getNodeName());
		idPanel.add(idLabel);
		
		add(idPanel);
		
		JPanel threadPanel = new JPanel();
		
		JButton decreaseThreadsButton = new JButton("-");
		decreaseThreadsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				node.setThreadCount(node.getThreadCount() - 1);
				threadsLabel.setText(node.getThreadCount() + "");
			}
		});
		
		JButton increaseThreadsButton = new JButton("+");
		increaseThreadsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				node.setThreadCount(node.getThreadCount() + 1);
				threadsLabel.setText(node.getThreadCount() + "");
			}
		});
		
		threadsLabel = new JLabel(node.getThreadCount() + "");
		
		threadPanel.add(new JLabel("Threads:"));
		threadPanel.add(decreaseThreadsButton);
		threadPanel.add(threadsLabel);
		threadPanel.add(increaseThreadsButton);
		
		add(threadPanel);
		
		for(Pair<String, Class<?>> pair : node.getRequiredParameterNames()) {
			pair.getRight(); // - returns the class
			// add a widget for every parameter
		}
	}
	
	private class ParameterInput {
		
//		private Parameter<T>
		
	}
	
}

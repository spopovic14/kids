package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.tuple.Pair;

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
			pair.getValue(); // - returns the class
			// add a widget for every parameter
			
		}
		
		for(Entry<String, Parameter> entry : node.getParameters().entrySet()) {
			String name = entry.getKey();
			Parameter parameter = entry.getValue();
			
			if(parameter.getType().isEnum()) {
				EnumParameterInput input = new EnumParameterInput(parameter);
				add(input);
			}
			else {
				JLabel lab = new JLabel(name + ":");
				add(lab);
				
				ParameterInput input = new StringParameterInput(parameter);
				add(input);
				
				JLabel notice = new JLabel("*Param fields do not work");
				add(notice);
			}
		}
	}
	
	private abstract class ParameterInput extends JPanel {
		
		private Parameter parameter;
		
		public ParameterInput(Parameter parameter) {
			this.parameter = parameter;
		}
		
		public abstract Object getValue();
		
	}
	
	private class StringParameterInput extends ParameterInput {
		
		private JTextField field;

		public StringParameterInput(Parameter parameter) {
			super(parameter);
			
			field = new JTextField(10);
			add(field);
			
			// Hard code the value
			field.setText("id");
		}
		
		public Object getValue() {
			return field.getText();
		}
		
	}
	
	private class EnumParameterInput extends ParameterInput {
		
		private JComboBox<Object> box;

		public EnumParameterInput(Parameter parameter) {
			super(parameter);
			
			Class<?> clazz = parameter.getType();
			Object[] values = clazz.getEnumConstants();
			
			box = new JComboBox<>(values);
			
			add(box);
		}
		
		public Object getValue() {
			return box.getSelectedItem();
		}
		
	}
	
}

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import core.Pipeline;
import example.GuiDisplay;
import example.RangeSplitter;
import example.SqlReader;
import node.NodeStatus;
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
		
		JPanel content = new JPanel(new BorderLayout());
		
		nodePanel = new JPanel();
		
		JPanel pan2 = new JPanel();
		
		JScrollPane scroll = new JScrollPane(nodePanel);
		
		content.add(scroll, BorderLayout.CENTER);
		content.add(pan2, BorderLayout.SOUTH);
		
		JButton startButton = new JButton("Start");
		pan2.add(startButton);
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pipeline.start();
			}
		});
		
		JButton stopButton = new JButton("Stop");
		pan2.add(stopButton);
		
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pipeline.stop();
			}
		});
		
		setContentPane(content);
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
		
		SqlReader reader = new SqlReader(2);
		reader.getParameter("JDBC-Url").setValue("jdbc:mysql://localhost:3306/kids_test");
		reader.getParameter("User").setValue("root");
		reader.getParameter("Password").setValue("");
		reader.getParameter("Package-Size").setValue(3);
		
		RangeSplitter splitter = new RangeSplitter(2);
		splitter.getParameter("Split-Key").setValue("id");
		
		splitter.addInputNode(reader);
		
		GuiDisplay display = new GuiDisplay(2);
		splitter.addOuputNode(display);
		
		app.addWorkerNode(splitter);
		app.revalidate();
	}
	
}

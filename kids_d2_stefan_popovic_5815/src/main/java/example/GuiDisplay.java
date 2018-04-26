package example;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.tuple.Pair;

import data.PipelineCollection;
import data.PipelineData;
import node.OutputNode;

public class GuiDisplay extends OutputNode {
	
	private AtomicInteger nextResourceId = new AtomicInteger(0);
	
	private DefaultTableModel tableModel;

	public GuiDisplay(int threadCount) {
		super(threadCount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Pair<String, Class<?>>> getRequiredParameterNames() {
		// TODO Auto-generated method stub
		return new LinkedList<>();
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "GUI Display";
	}

	@Override
	protected void onNodeStart() throws Exception {
		JFrame frame = new JFrame("GUI Display");
		
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(new Object[] {"ID", "Value"});
		JTable table = new JTable(tableModel);
		
		JScrollPane scroll = new JScrollPane(table);
		frame.add(scroll);
		frame.setSize(1366, 768);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	protected void doWorkCycle(int threadId) throws Exception {
		PipelineCollection collection = inputTake();
		PipelineData data;
		
		while((data = collection.poll(0, TimeUnit.MILLISECONDS)) != null) {
			Object[] newRow = new Object[] {
				data.getValue("id"),
				data.getValue("value")
			};
			tableModel.addRow(newRow);
		}
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
	protected void onThreadException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getNextResourceId() {
		return nextResourceId.incrementAndGet();
	}

}

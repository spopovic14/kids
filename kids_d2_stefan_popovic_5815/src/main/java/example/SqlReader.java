package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Queue;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import data.GenericPipelineCollection;
import data.GenericPipelineData;
import data.PipelineCollection;
import data.PipelineData;
import node.InputNode;


public class SqlReader extends InputNode {
	
	private AtomicInteger nextResourceId = new AtomicInteger(0);
	
	private Connection connection;
	private QueryRunner runner;
	private String query = "SELECT * FROM kids_test_table LIMIT ? OFFSET ?";
	private int packageSize;
	private int offset = 0;
	
	private BlockingQueue<PipelineData> internalQueue;

	public SqlReader(int threadCount) {
		super(threadCount);
		internalQueue = new LinkedBlockingQueue<>();
	}

	@Override
	public List<Pair<String, Class<?>>> getRequiredParameterNames() {
		List<Pair<String, Class<?>>> list = new ArrayList<>();
		
		list.add(new ImmutablePair<String, Class<?>>("JDBC-Url", String.class));
		list.add(new ImmutablePair<String, Class<?>>("User", String.class));
		list.add(new ImmutablePair<String, Class<?>>("Password", String.class));
		list.add(new ImmutablePair<String, Class<?>>("Package-Size", Integer.class));
		
		return list;
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "SqlReader";
	}
	
	@Override
	protected void onNodeStart() throws Exception {
		String jdbcUrl = getParameterValue("JDBC-Url").toString();
		String user = getParameterValue("User").toString();
		String password = getParameterValue("Password").toString();
		String jdbcDriver = "com.mysql.jdbc.Driver";
		
		DbUtils.loadDriver(jdbcDriver);
		connection = DriverManager.getConnection(jdbcUrl, user, password);
		runner = new QueryRunner();
		packageSize = (int) getParameterValue("Package-Size");
	}

	@Override
	protected void doWorkCycle(int threadId) throws Exception {
		if(threadId == 1) {
			// The first thread reads data from the database and writes is to the internal queue
			Queue<PipelineData> queue = runner.query(connection, query, new Handler(), packageSize, offset);
			PipelineData data;
			while((data = queue.poll()) != null) {
				System.out.println("Read: " + data.getValue("value"));
				internalQueue.offer(data);
				offset++;
			}
			Thread.sleep(500);
		}
		else {
			// Other threads read data from the internal queue, put it into collections and send those collections
			PipelineCollection collection = new GenericPipelineCollection(createPipelineId());
			
			for(int i = 0; i < packageSize; i++) {
				PipelineData data = internalQueue.poll(3000, TimeUnit.MILLISECONDS);
				if(data != null) {
					collection.put(data);
					System.out.println("Write to collection: " + data.getValue("value"));
				}
			}
			
			put(collection);
			System.out.println("Put: " + collection.getId().getId());
			Thread.sleep(1000);
		}
		
	}

	@Override
	protected void onThreadStart(int threadID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onThreadFinish(int threadID) {
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
	
	private class Handler implements ResultSetHandler<Queue<PipelineData>> {

		@Override
		public Queue<PipelineData> handle(ResultSet result) throws SQLException {
			Queue<PipelineData> queue = new LinkedList<>();
			
			MapListHandler handler = new MapListHandler();
			
			List<Map<String, Object>> list = handler.handle(result);
			for(Map<String, Object> map : list) {
				PipelineData data = new GenericPipelineData(createPipelineId());
				for(Entry<String, Object> entry : map.entrySet()) {
					data.setValue(entry.getKey(), entry.getValue());
				}
				queue.add(data);
			}
			
			return queue;
		}
		
	}

}

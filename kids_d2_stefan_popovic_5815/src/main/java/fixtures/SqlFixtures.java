package fixtures;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

public class SqlFixtures {

	public static void main(String[] args) throws Exception {
		Connection connection;
		
		String jdbcUrl = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String password = "";
		String jdbcDriver = "com.mysql.jdbc.Driver";
		
		DbUtils.loadDriver(jdbcDriver);
		connection = DriverManager.getConnection(jdbcUrl, user, password);
		QueryRunner runner = new QueryRunner();
		
		runner.execute(connection, "CREATE DATABASE IF NOT EXISTS `kids_test`");
		runner.execute(connection, "CREATE TABLE `kids_test`.`kids_test_table` ( `id` INT NOT NULL AUTO_INCREMENT , `value` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
		for(int i = 0; i < 500; i++) {
			runner.execute(connection, "INSERT INTO kids_test.kids_test_table (value) VALUES (?)", "Value " + i);
		}
	}
	
}

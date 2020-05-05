package center.orbita.test.counterdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import center.orbita.test.configs.DatabaseConfig;

@Service
public class ReadCounter {
	private static final Logger logger = LogManager.getLogger(ReadCounter.class);
	private Connection conn;
	private Statement stmt;
	
	@Autowired
	DatabaseConfig db;
	
	
	@Autowired
	private void init() {
		try {
			conn = db.dataSource().getConnection();
			logger.info("Connected: " + conn.getCatalog());
			stmt = conn.createStatement();
			logger.info("Stmt: "+ stmt.toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQL Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public synchronized long getMaxCounter() {
		
		String SQLtext = "SELECT counter FROM test.counter WHERE counter = (SELECT MAX(counter) FROM test.counter)";
		boolean sts_sp = false;
		long result=0;
		try {
			ResultSet rs = null;
			Statement sp1 = conn.createStatement();
			sts_sp = sp1.execute(SQLtext);
			logger.info("boolean sts_sp = " + sts_sp);
			
			if (sts_sp) {
				rs = sp1.getResultSet();
				while (rs.next()) {
					result = rs.getLong(1);
				}
				rs.close();
			}
			sp1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQL Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		} 
//		finally {
//			if (conn != null)	try {
//				//conn.commit();
//				conn.close();
//			} catch(SQLException e) { logger.error("Error: ", e); }
//		}
		return result;
	}
	
	public synchronized long zeroingCounterAndGet() {
		
		String SQLtext = "UPDATE test.counter SET counter=0, timestamp_upd=current_timestamp WHERE counter = (SELECT MAX(counter) FROM test.counter)";
		int sts_sp = 0;
		
		try {
			Statement sp1 = conn.createStatement();
			sts_sp = sp1.executeUpdate(SQLtext);
			logger.info("Update rc = " + sts_sp);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQL Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		} 

		return getMaxCounter();
	}
	
	
	public synchronized long incrementCounterAndGet() {
		
		String SQLtext = "UPDATE test.counter SET counter=(SELECT MAX(counter)+1 FROM test.counter), "
				+ "timestamp_upd=current_timestamp "
				+ "WHERE counter = (SELECT MAX(counter) FROM test.counter)";
		int sts_sp = 0;
		
		try {
			Statement sp1 = conn.createStatement();
			sts_sp = sp1.executeUpdate(SQLtext);
			logger.info("Update rc = " + sts_sp);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQL Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		} 

		return getMaxCounter();
	}
	
}

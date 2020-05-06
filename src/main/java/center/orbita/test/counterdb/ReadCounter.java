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

/*
 * Класс для работы с БД
 * Подключение к БД
 * Работа с таблицей счетчика test.counter
 * 
 */

@Service
public class ReadCounter {
	private static final Logger logger = LogManager.getLogger(ReadCounter.class);
	private Connection conn;
	private Statement stmt;
	
	@Autowired
	DatabaseConfig db;
	
	// - Инициализация подключения к БД
	// На каждый запрос (отдельная нить) отдельный коннект
	private void init() {
		try {
			conn = db.dataSource().getConnection();
			logger.info("Connected: " + conn.getCatalog());
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQL Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	// - Получение текущего значения счетчика для  GET-запроса
	public synchronized long getMaxCounter() {
		init();
		final String SQLtext = "SELECT counter FROM test.counter "
				+ "WHERE counter = (SELECT MAX(counter) FROM test.counter)";
		boolean sts_sp = false;
		long result=0;
		try {
			ResultSet rs = null;
			Statement sp1 = conn.createStatement();
			sts_sp = sp1.execute(SQLtext);
			
			if (sts_sp) {
				rs = sp1.getResultSet();
				while (rs.next()) {
					result = rs.getLong(1);
				}
				rs.close();
				logger.info("Current count = "+result);
			}
			sp1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQL Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if (conn != null)	try {
				//conn.commit();
				stmt.close();
				conn.close();
			} catch(SQLException e) { logger.error("Error close connect: ", e); }
		}
		return result;
	}
	
	// - обнуление и получение значения счетчика для DELETE - запроса
	public synchronized long zeroingCounterAndGet() {
		init();
		final String SQLtext = "UPDATE test.counter SET counter=0, timestamp_upd=current_timestamp "
				+ "WHERE counter = (SELECT MAX(counter) FROM test.counter)";
		
		try {
			Statement sp1 = conn.createStatement();
			sp1.executeUpdate(SQLtext);
			logger.info("Zeroing is successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Update Zeroing Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if (conn != null)	try {
				stmt.close();
				conn.close();
			} catch(SQLException e) { logger.error("Error close connect: ", e); }
		}
		
		return getMaxCounter();
	}
	
	// - Увеличение счетчика на единицу и полчение значения для POST-запроса
	public synchronized long incrementCounterAndGet() {
		init();
		final String SQLtext = "UPDATE test.counter SET counter=(SELECT MAX(counter)+1 FROM test.counter), "
				+ "timestamp_upd=current_timestamp "
				+ "WHERE counter = (SELECT MAX(counter) FROM test.counter)";
		
		try {
			Statement sp1 = conn.createStatement();
			sp1.executeUpdate(SQLtext);
			logger.info("Updated increment" );
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Update increment Error! "+e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if (conn != null)	try {
				stmt.close();
				conn.close();
			} catch(SQLException e) { logger.error("Error close connect: ", e); }
		}

		return getMaxCounter();
	}
	
}

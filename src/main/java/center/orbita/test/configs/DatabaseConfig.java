package center.orbita.test.configs;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
public class DatabaseConfig {
	@Bean
	public DataSource dataSource() {
	    final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	    dsLookup.setResourceRef(true);
	    DataSource dataSource = dsLookup.getDataSource("java:comp/env/jdbc/postgresmain");
	    return dataSource;
	}
}

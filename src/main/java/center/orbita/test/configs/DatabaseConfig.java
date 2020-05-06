package center.orbita.test.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

// - Bean для конфигурирования подключения к БД на основе ресурса из встроенного Tomcat 
//
@Configuration
public class DatabaseConfig {
	@Value(value = "${spring.datasource.resourcename}")
	String Resourcename;
	
	@Bean
	public DataSource dataSource() {
	    final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	    dsLookup.setResourceRef(true);
	    DataSource dataSource = dsLookup.getDataSource("java:comp/env/"+Resourcename);
	    return dataSource;
	}
}

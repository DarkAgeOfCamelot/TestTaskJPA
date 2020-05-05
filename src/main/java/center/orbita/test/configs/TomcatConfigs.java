package center.orbita.test.configs;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;

//
//	КЛАСС ДЛЯ ВСТРОЕННОГО В SPRING-BOOT TOMCAT
//	НЕ ИСПОЛЬЗУЕТСЯ ПРИ РАЗВЕРТЫВАНИИ ВО ВНЕШНЕМ TOMCAT	
//

@Configuration
public class TomcatConfigs {
	@Value( "${spring.datasource.url}" )
	private String jdbcUrl;	
	
	@Value( "${spring.datasource.username}" )
	private String jdbcUsername;
	
	@Value( "${spring.datasource.password}" )
	private String jdbcPassword;
	
	@Value( "${spring.datasource.resourcename}" )
	private String jdbcResourcename;
	
		@Bean
		public ServletWebServerFactory servletContainer() {
			
			 TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			     @Override          
			     protected void postProcessContext(Context context) {
			         ContextResource resource = new ContextResource();
			                    
			         resource.setName(jdbcResourcename);
			         resource.setType(DataSource.class.getName());
			         resource.setProperty("driverClassName", "org.postgresql.Driver");
			         resource.setProperty("url", jdbcUrl);
			         resource.setProperty("username", jdbcUsername);
			         resource.setProperty("password", jdbcPassword);
			         context.getNamingResources().addResource(resource);             
			     }
		
			     @Override          
			     protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
			         tomcat.enableNaming();
			         TomcatWebServer container =  super.getTomcatWebServer(tomcat);
			         for(Container child  :container.getTomcat().getHost().findChildren()){
			        	 if (child instanceof Context) {
			        		 ClassLoader contextClassLoader = ((Context)child).getLoader().getClassLoader();
			        		 Thread.currentThread().setContextClassLoader(contextClassLoader);
			        		 break;
			        	 }
			         }
			         return container;           
			     }
		     };
		     
		     return tomcat;
		}
	    
	    @Bean
	    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
	        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();    
	        bean.setJndiName("java:/comp/env/jdbc/postgresmain");
	        bean.setProxyInterface(DataSource.class);
	        bean.setLookupOnStartup(false);
	        bean.afterPropertiesSet();
	        
	        return (DataSource) bean.getObject();
	    }
}

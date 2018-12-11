package com.canalbrewing.myabcdata.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@PropertySource("classpath:myabcdata.properties")
@EnableWebMvc
@ComponentScan(basePackages = "com.canalbrewing.myabcdata")
public class AppConfig extends WebMvcConfigurerAdapter {
	
	@Value("${host}")
	private String host;
	
	@Value("${user}")
	private String username;
	
	@Value("${password}")
	private String password;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureDefaultServletHandling(org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer)
	 * Enable default servlet handler. This will let other http request such as .css, .js slip through the usual DispatcherServlet and let the container process them. So now we can serve the static files css and javascript from our WebApp folder.
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean
	public ViewResolver beanNameViewResolver() {
	      BeanNameViewResolver resolver = new BeanNameViewResolver();
	      return resolver;
	}
		
	@Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
		
	@Bean
    public BasicDataSource dataSource() {
        
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://" + host + "/myabcdata?noAccessToProcedureBodies=true");
		ds.setTestOnBorrow(true);
		ds.setValidationQuery("select 1");
		ds.setUsername(username);
		ds.setPassword(password);
						
		return ds;
    }

}


package com.jts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("com.jts")
@EnableTransactionManagement
@EnableScheduling
public class WebConfig extends WebMvcConfigurerAdapter{

	public static String DB_NAME = "jts_schema";
	public static String DB_URL =  "jdbc:mysql://localhost:3306/"+DB_NAME+"?useUnicode=true&characterEncoding=utf-8";
	public static String DB_USERNAME =  "root";
	public static String DB_PASSWORD =  "";

	public static String fileSaveLocation = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps";
	//public static String fileSaveLocation = "";

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}


	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/");
		resolver.setSuffix(".html");
		resolver.setExposeContextBeansAsAttributes(true);
		return resolver;
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(DB_URL);
		dataSource.setUsername(DB_USERNAME);
		dataSource.setPassword(DB_PASSWORD);
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource){
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		return transactionManager;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize((long) (1.5 * 1024 * 1024 * 1024));
		return multipartResolver;
	}

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("");
		mailSender.setPassword("");

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "true");
		javaMailProperties.put("mail.smtp.starttls.required", "true");
		javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		javaMailProperties.put("mail.smtp.host", "smtp.gmail.com");
		javaMailProperties.put("mail.smtp.port", "465");
		javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	@Override
	public void addFormatters(FormatterRegistry registry)  {
		registry.removeConvertible(String.class, Collection.class);
		registry.addConverter(String.class,Collection.class,noCommaSplitStringToArrayConverter());
	}

	@Bean
	public Converter<String, List<String>> noCommaSplitStringToArrayConverter() {
		return new Converter<String, List<String>>() {
			@Override
			public List<String> convert(final String source) {
				final List<String> classes = new ArrayList<String>();
				classes.add(source);
				return classes;
			}
		};
	}
}

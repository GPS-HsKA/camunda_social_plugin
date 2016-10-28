package de.camunda.cockpit.plugin.social;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import java.sql.*;
import javax.sql.*;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.h2.jdbcx.JdbcDataSource;
import org.jboss.weld.resources.ClassLoaderResourceLoader;

import javax.naming.Context;
import javax.naming.InitialContext;

public class LiquibaseProducer {

	@Resource(lookup = "java:jboss/datasources/mydatasource")
	private DataSource myDataSource;

	@Produces
	@LiquibaseType
	public CDILiquibaseConfig createConfig() {
		CDILiquibaseConfig config = new CDILiquibaseConfig();
		config.setChangeLog("src/resources/liquibase/db.changelog.xml");
		return config;
	}

	@Produces
	@LiquibaseType
	public DataSource createDataSource() throws SQLException {
		JdbcDataSource ds = new JdbcDataSource();
		        ds.setDatabase("jdbc:hsqldb:mem:test");
		        ds.setUser("sa");
		        ds.setPassword("");
		return ds;
	}

	@Produces
	@LiquibaseType
	public ResourceAccessor create() {
		return new ClassLoaderResourceAccessor(getClass().getClassLoader());
	}
}

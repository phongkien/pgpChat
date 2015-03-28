package com.phongkien.dao;

import java.io.FileInputStream;
import java.util.Properties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.phongkien.utils.UtilsFunctions;

/**
 * Database properties management class to allow loading of database properties from a file.
 * 
 * @author phongkien
 *
 */
public class DataSourceManager extends DriverManagerDataSource {
	private String dbConfigPath = "";

	public DataSourceManager() {
		super();
	}

	public DataSourceManager(String url) {
		super(url);
	}

	public DataSourceManager(String url, String username, String password) {
		super(url, username, password);
	}

	public DataSourceManager(String url, Properties conProps) {
		super(url, conProps);
	}

	public void init() throws Exception {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(getDbConfigPath()));
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			
			if (UtilsFunctions.isNull(url) || UtilsFunctions.isNull(username) || UtilsFunctions.isNull(password)) {
				throw new Exception("Invalid configuration, missing one or more properties (url, username, password): " + getDbConfigPath());
			} else {
				this.setUrl(url);
				this.setUsername(username);
				this.setPassword(password);
			}
		} catch (Exception ex) {
			throw new Exception("Invalid configuration, unable to load properties from property file: " + getDbConfigPath());
		}
	}

	public String getDbConfigPath() {
		return dbConfigPath;
	}

	public void setDbConfigPath(String dbConfigPath) {
		this.dbConfigPath = dbConfigPath;
	}
}

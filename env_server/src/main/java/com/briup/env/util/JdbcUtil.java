package com.briup.env.util;
/**
 * 简单封装jdbc的代码，来获得连接对象
 * @author mastercgx
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alibaba.druid.pool.DruidDataSource;

public class JdbcUtil {
	// 连接信息的定义
	private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
	// mysql8的写法
	private final static String URL = "jdbc:mysql://127.0.0.1:3306/mybatis?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
	private final static String USERNAME = "root";
	private final static String PASSWORD = "root";
	
	/**
	 * 普通方式获取连接对象，并且处理了异常
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 封装关闭的方法，内部处理异常
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void close(Connection conn, Statement st, ResultSet rs) {
		if(conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(st != null)
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * 从连接池中取出连接对象，使用完毕调用close()不会直接关闭，而是归还个连接池
	 * @return
	 */
	public static Connection getConnectionFomeDruid() {
		// 新建druid的数据源
		DruidDataSource dataSource = new DruidDataSource();
		// 设置基本信息
		dataSource.setDriverClassName(DRIVER);
		dataSource.setUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		// 设置初始化的连接数
		dataSource.setInitialSize(5);
		// 设置最大活跃数
		dataSource.setMaxActive(10);
		// 定义连接对象
		Connection conn = null;
		// 获取连接对象
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}

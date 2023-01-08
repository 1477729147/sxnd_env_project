package com.briup.jdbc.test;

import java.util.List;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.briup.env.util.JdbcUtil;
import com.briup.jdbc.entity.Gender;
import com.briup.jdbc.entity.Student;

/**
 * 使用JDBC简单做一些增删改查的操作
 * @author mastercgx
 *
 */
public class JdbcTest {
	
	private static final String url = "jdbc:mysql://127.0.0.1:3306/mybatis?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";

	@Test
	public void insert() {
		// 新增
		Connection conn = null;
		Statement st = null;
		try {
			// 注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 连接
			// url username password
			conn = DriverManager.getConnection(url,"root","root");
			// 创建statement对象
			st = conn.createStatement();
			// 准备sql语句（DML）
			// String sql = "insert into student(name) values('tom')";
			// 准备一个student对象
			Student student = new Student();
			student.setName("jerry");
			student.setAge(20);
			student.setGender(Gender.FEMALE);
			student.setCreateTime(new Date());
			// 拼接一个sql语句
			String sql = "insert into student(name,age,gender,create_time) "
					+ "values('"+student.getName()+"',"
					+student.getAge()+",'"
					+student.getGender()+"',now())";
			/*
			insert into student(name,age,gender,create_time)
			values('jerry',20,'男',now())
			*/
			// 这种拼接的方式很复杂
			// PreparedStatement 同构sql语句
			
			// 执行sql语句
			st.execute(sql);
			System.out.println("【添加成功】");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭资源
			JdbcUtil.close(conn, st, null);
		}
		
	}
	
	
	@Test
	public void delete() {
		// 删除
		Connection conn = null;
		Statement st = null;
		try {
			// 注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 连接
			// url username password
			conn = DriverManager.getConnection(url,"root","root");
			// 创建statement对象
			st = conn.createStatement();
			// 准备sql语句（DML）
			// 截止到8:40，大家写出这三个sql语句，截图在公屏上
			// 单个删除（删除id为5的数据）
			// 多个删除（删除id为1，3，5的数据）
			// 全部删除（清空表）
			int id = 4;
			String sql = "delete from student where id = "+id;
			// 执行sql语句
			st.execute(sql);
			System.out.println("【删除成功】");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭资源
			if(st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	@Test
	public void select() {
		// 查询
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 连接
			// url username password
			conn = DriverManager.getConnection(url,"root","root");
			// 创建statement对象
			st = conn.createStatement();
			// 编写sql语句
			// 单表查询、多表查询、嵌套查询、模糊查询、分页查询、分组查询、条件查询
			String sql = "select * from student";
			// 执行sql语句（查询语句必须使用executeQuery方法，才能返回结果集对象）
			rs = st.executeQuery(sql);
			// 对结果集进行处理（将结果集的数据提取出来，和Java对象结合起来）
			// 因为要求是查询全部，需要准备一个集合用来接收数据
			List<Student> list = new ArrayList<>();
			while(rs.next()) {
				// 每循环一次就相当于获取一行的数据
				// 可以根据列名或者列的索引来读取每一行数据
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String genderStr = rs.getString("gender");
				// 三目运算符  布尔表达式?true的操作:false的操作
				Gender gender = "男".equals(genderStr)?Gender.MALE:Gender.FEMALE;
				Date createTime = rs.getDate("create_time");
				// 组合成student 对象
				Student student = new Student(id,name,age,gender,createTime);
				// 每循环一次，添加到集合中
				list.add(student);
			}
					
			System.out.println("【查询成功】");
			list.forEach(System.out::println);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭资源
			if(st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
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
	}
	
	@Test
	public void sameStructInsert() {
		// 使用PreparedStatement进行插入
		// 先定义结构，再传递数据。
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 连接
			// url username password
			conn = DriverManager.getConnection(url,"root","root");
			// 定义sql的结构，jdbc允许使用?对数据进行占位
			String sql = "insert into student(name,age,gender,create_time) values(?,?,?,now())";
			// 创建PreparedStatement对象，创建之前得准备sql的结构
			ps = conn.prepareStatement(sql);
			// 有一个student对象
			Student s = new Student();
			s.setName("鬼脚七");
			s.setAge(30);
			s.setGender(Gender.MALE);
			System.out.println(s);
			// 插入数据
			ps.setString(1, s.getName());
			ps.setInt(2, s.getAge());
			ps.setString(3, s.getGender().toString());
			
			// 执行sql语句
			ps.execute();
			System.out.println("【添加成功】");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭资源
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	@Test
	public void batch() {
		// 使用PreparedStatement进行插入
		// 先定义结构，再传递数据。
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 连接
			// url username password
			conn = DriverManager.getConnection(url,"root","root");
			// 定义sql的结构，jdbc允许使用?对数据进行占位
			String sql = "insert into student(name,age,gender,create_time) values(?,?,?,now())";
			// 创建PreparedStatement对象，创建之前得准备sql的结构
			ps = conn.prepareStatement(sql);
			// 有一个student对象
			Student s = new Student();
			s.setName("鬼脚七");
			s.setAge(30);
			s.setGender(Gender.MALE);
			System.out.println(s);
			// 插入数据1
			ps.setString(1, s.getName());
			ps.setInt(2, s.getAge());
			ps.setString(3, s.getGender().toString());
			ps.addBatch(); // 添加进缓存中
			// 批处理：把多条sql语句做个缓存，然后一次性提交给mysql
			// 插入数据2
			ps.setString(1, "测试1");
			ps.setInt(2, s.getAge());
			ps.setString(3, s.getGender().toString());
			ps.addBatch(); // 放到缓存中
			// 插入数据3 
			ps.setString(1, "测试2");
			ps.setInt(2, s.getAge());
			ps.setString(3, s.getGender().toString());
			ps.addBatch(); // 放到缓存中
			
			// 提交所有的缓存的sql
			ps.executeBatch();
			System.out.println("【操作成功】");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭资源
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	// 提问：最多能创建多少个conn呢？
	@Test
	public void connectionPool() throws SQLException {
		// 使用工具获取conn对象并输出
		// 使用一个死循环无限获取，并且打印循环次数
		int index=0;
		Connection conn = JdbcUtil.getConnection();  // 306
		while(true) {
			Statement st = conn.createStatement();
			index++;
			System.out.println(index+"-----"+st);
			if(st == null) return;
		}
	}
	
	@Test
	public void druidTest() {
		// 输出通过连接池获取连接对象
		System.out.println(JdbcUtil.getConnectionFomeDruid());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

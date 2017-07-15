package com.yc.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import oracle.sql.BLOB;

public class DBHelper {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/**
	 * 加载驱动
	 */
	static {
		try {
			Class.forName(MyProperties.getInstance().getProperty("driverName"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接对象
	 */
	public Connection getConn() {
		// Connection conn = null;

		/*try {
			conn = DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),
					MyProperties.getInstance());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Connection conn=null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource)envCtx.lookup("jdbc/bbsdb");

			conn = ds.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭对象
	 */
	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		// 关闭数据库连接对象
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 关闭预处理对象
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 关闭结果集对象
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置参数 预编译对象 外部传入的参数值 添加值顺序一样 要跟？对应值的顺序一致
	 */
	public void setparams(PreparedStatement pstmt, List<Object> params) {

		if (null != params && params.size() > 0) {
			// 图片必须单独处理，传入的输入流，所以从界面上传入文件对象
			for (int i = 0; i < params.size(); i++) {
				if (params.get(i) instanceof File) {
					File file = (File) params.get(i); // 强转为文件对象
					try {
						InputStream in = new FileInputStream(file);// 转为输入流对象
						pstmt.setBinaryStream(i + 1, in, (int) file.length());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					try {
						pstmt.setObject(i + 1, params.get(i));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 获得结果集中的所有列表
	 */
	public List<String> getAllColumnName(ResultSet rs) {
		List<String> columnNames = new ArrayList<String>();
		try {
			ResultSetMetaData dd = rs.getMetaData();
			for (int i = 1; i <= dd.getColumnCount(); i++) {
				columnNames.add(dd.getColumnName(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnNames;
	}

	/**
	 * 查看操作 ： sql语句可以查询输出多条记录
	 */
	public List<Map<String, Object>> findMultiObject(String sql, List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		conn = this.getConn();
		try {
			pstmt = conn.prepareStatement(sql);
			this.setparams(pstmt, params);
			rs = pstmt.executeQuery();
			// 获得结果集中所有的列名
			List<String> columnNames = getAllColumnName(rs);
			while (rs.next()) {
				map = new HashMap<String, Object>(); // 使用Map把数据存入List中
				for (String name : columnNames) {
					map.put(name, rs.getObject(name)); // 使用增强for循环map.put(String
														// ,Object);
				}
				list.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeAll(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * 查询操作 select * from myemp where id=? 只有一条结果
	 */
	public Map<String, Object> findSingleObject(String sql, List<Object> params) {
		Map<String, Object> map = null;
		try {
			conn = this.getConn(); // 创建连接
			pstmt = conn.prepareStatement(sql); // 预编译sql语句
			this.setparams(pstmt, params); // 设置参数
			rs = pstmt.executeQuery(); // 得到一个结果集
			List<String> columnNames = getAllColumnName(rs); // 得到所有列名
			if (rs.next()) {
				map = new HashMap<String, Object>(); // 使用map来传入参数
				for (String name : columnNames) {
					map.put(name, rs.getObject(name));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeAll(conn, pstmt, rs);
		}
		return map;
	}

	/**
	 * 单条sql语句更新操作：增删改 sql语句 params 传入参数
	 */
	public int doUpdate(String sql, List<Object> params) {
		int result = 0;
		try {
			conn = this.getConn(); // 获取连接对象
			pstmt = conn.prepareStatement(sql); // 预编译
			// 设置参数
			this.setparams(pstmt, params);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeAll(conn, pstmt, null);
		}

		return result;
	}

	/**
	 * 多条sql语句的更新操作 批处理 注意：这些sql语句执行的结果要么一起成功 要么一起失败 sqls params
	 * 对应每一条sql语句所需要的参数值集合
	 * 
	 * @throws SQLException
	 */
	public int doUpdate(List<String> sqls, List<List<Object>> params) throws SQLException {
		int result = 0;

		// 设置事物提交方式为手动提交
		try {
			conn = this.getConn();
			conn.setAutoCommit(false);
			if (sqls != null & sqls.size() > 0) {
				// 对sql语句进行循环
				for (int i = 0; i < sqls.size(); i++) {
					String sql = sqls.get(i);
					pstmt = conn.prepareStatement(sql);
					this.setparams(pstmt, params.get(i)); // 第几条sql语句对应第几条list集合中的第几个小list
					result = pstmt.executeUpdate();
				}
			}
			conn.commit();// 手动提交事物
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			conn.rollback();
		} finally {
			conn.setAutoCommit(true);
			this.closeAll(conn, pstmt, rs);
		}
		return result;
	}

	/**
	 * 聚合函数查询 select count(*) from myemp; sql params
	 */
	public double getCount(String sql, List<Object> params) {
		double result = 0;

		try {
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			setparams(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getDouble(1); // 获取第一列的值
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeAll(conn, pstmt, rs);
		}
		return result;
	}

	// 从数据库读取图片
	public InputStream selectImg(String sql, List<Object> params) throws IOException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream inputStream = null;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			this.setparams(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				BLOB blob = (BLOB) rs.getBlob(1);
				inputStream = blob.getBinaryStream();
				return inputStream;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	

}

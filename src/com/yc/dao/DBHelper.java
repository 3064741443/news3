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
	 * ��������
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
	 * ��ȡ���ݿ����Ӷ���
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
	 * �رն���
	 */
	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		// �ر����ݿ����Ӷ���
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// �ر�Ԥ�������
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// �رս��������
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
	 * ���ò��� Ԥ������� �ⲿ����Ĳ���ֵ ���ֵ˳��һ�� Ҫ������Ӧֵ��˳��һ��
	 */
	public void setparams(PreparedStatement pstmt, List<Object> params) {

		if (null != params && params.size() > 0) {
			// ͼƬ���뵥����������������������Դӽ����ϴ����ļ�����
			for (int i = 0; i < params.size(); i++) {
				if (params.get(i) instanceof File) {
					File file = (File) params.get(i); // ǿתΪ�ļ�����
					try {
						InputStream in = new FileInputStream(file);// תΪ����������
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
	 * ��ý�����е������б�
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
	 * �鿴���� �� sql�����Բ�ѯ���������¼
	 */
	public List<Map<String, Object>> findMultiObject(String sql, List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		conn = this.getConn();
		try {
			pstmt = conn.prepareStatement(sql);
			this.setparams(pstmt, params);
			rs = pstmt.executeQuery();
			// ��ý���������е�����
			List<String> columnNames = getAllColumnName(rs);
			while (rs.next()) {
				map = new HashMap<String, Object>(); // ʹ��Map�����ݴ���List��
				for (String name : columnNames) {
					map.put(name, rs.getObject(name)); // ʹ����ǿforѭ��map.put(String
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
	 * ��ѯ���� select * from myemp where id=? ֻ��һ�����
	 */
	public Map<String, Object> findSingleObject(String sql, List<Object> params) {
		Map<String, Object> map = null;
		try {
			conn = this.getConn(); // ��������
			pstmt = conn.prepareStatement(sql); // Ԥ����sql���
			this.setparams(pstmt, params); // ���ò���
			rs = pstmt.executeQuery(); // �õ�һ�������
			List<String> columnNames = getAllColumnName(rs); // �õ���������
			if (rs.next()) {
				map = new HashMap<String, Object>(); // ʹ��map���������
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
	 * ����sql�����²�������ɾ�� sql��� params �������
	 */
	public int doUpdate(String sql, List<Object> params) {
		int result = 0;
		try {
			conn = this.getConn(); // ��ȡ���Ӷ���
			pstmt = conn.prepareStatement(sql); // Ԥ����
			// ���ò���
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
	 * ����sql���ĸ��²��� ������ ע�⣺��Щsql���ִ�еĽ��Ҫôһ��ɹ� Ҫôһ��ʧ�� sqls params
	 * ��Ӧÿһ��sql�������Ҫ�Ĳ���ֵ����
	 * 
	 * @throws SQLException
	 */
	public int doUpdate(List<String> sqls, List<List<Object>> params) throws SQLException {
		int result = 0;

		// ���������ύ��ʽΪ�ֶ��ύ
		try {
			conn = this.getConn();
			conn.setAutoCommit(false);
			if (sqls != null & sqls.size() > 0) {
				// ��sql������ѭ��
				for (int i = 0; i < sqls.size(); i++) {
					String sql = sqls.get(i);
					pstmt = conn.prepareStatement(sql);
					this.setparams(pstmt, params.get(i)); // �ڼ���sql����Ӧ�ڼ���list�����еĵڼ���Сlist
					result = pstmt.executeUpdate();
				}
			}
			conn.commit();// �ֶ��ύ����
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
	 * �ۺϺ�����ѯ select count(*) from myemp; sql params
	 */
	public double getCount(String sql, List<Object> params) {
		double result = 0;

		try {
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			setparams(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getDouble(1); // ��ȡ��һ�е�ֵ
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeAll(conn, pstmt, rs);
		}
		return result;
	}

	// �����ݿ��ȡͼƬ
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

package com.ss.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MyModel extends AbstractTableModel{
	Vector<String> colName;
	Vector<Vector>data=new Vector<Vector>();
	Connection con;
	
	public MyModel(Connection con) {
		this.con=con;
		
		colName=new Vector<>();
		colName.add("score_id");
		colName.add("학년");
		colName.add("성별");
		colName.add("국어");
		colName.add("영어");
		colName.add("수학");
		
		getList();
	}
	
	
	//오라클 db에서 data가져오기
	public void getList() {
		PreparedStatement pstmt=null;
		ResultSet rs=null;				
		
		String sql="select * from score order by score_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				Vector vec=new Vector();//VO역할
				//vec이 담을 레코드는 1건
				
				vec.add(rs.getString("score_id"));
				vec.add(rs.getString("grade"));
				vec.add(rs.getString("gender"));
				vec.add(rs.getString("kor"));
				vec.add(rs.getString("eng"));
				vec.add(rs.getString("math"));		
				
				data.add(vec);				
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
				

	}
	
	//수정가능하게 하기
	public boolean isCellEditable(int row, int col) {
		boolean flag=true;
		if (col==0) {
			flag=false;
		}
		
		return flag;
	}
	
	//jtable에서 편집시 그 값이 적용되게 하기	
	public void setValueAt(Object aValue, int row, int col) {		
		data.get(row).set(col, aValue);		
	}
	
	public String getColumnName(int col) {
		return colName.get(col);
	}
	
	public int getColumnCount() {
		return colName.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}
	
}

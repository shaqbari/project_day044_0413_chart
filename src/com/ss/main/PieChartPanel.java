package com.ss.main;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartPanel {
	JFreeChart chart;
	DefaultPieDataset dataset;
	Connection con;

	public PieChartPanel(Connection con) {
		this.con = con;
	}

	// ���� �м�
	public void getGenderData() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select gender, count(gender) as �����ڼ�");
		sql.append(" , (select count(*) from score) as ���л�");
		sql.append(" , (count(gender)/(select count(*) from score)*100) as ����");
		sql.append(" from score");
		sql.append(" group by gender");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			dataset = new DefaultPieDataset();
			while (rs.next()) {
				dataset.setValue(rs.getString("gender"), rs.getInt("����"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// �г⺰ �м�
	public void getGradeData() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select grade, count(grade) as �����ڼ�");
		sql.append(" , (select count(*) from score) as ���л�");
		sql.append(" , (count(grade)/(select count(*) from score)*100) as ����");
		sql.append(" from score");
		sql.append(" group by grade");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			dataset = new DefaultPieDataset();
			while (rs.next()) {
				dataset.setValue(rs.getString("grade"), rs.getInt("����"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ChartPanel showChart() {
		//getGenderData();
		getGradeData();
		
		chart = ChartFactory.createPieChart("����ǥ �м�", dataset, true, true, false);

		// ���� ��Ʈ�� ������ ��Ʈ�� �ѱ���Ʈ�� �ٲ��� ������ �������δ�.
		System.out.println(chart.getTitle().getFont().getFontName());

		// �����ϲ�, �����ϻ�����.
		Font oldTitle = chart.getTitle().getFont();
		Font oldLegend = chart.getLegend().getItemFont();

		chart.getTitle().setFont(new Font("����", oldTitle.getStyle(), oldTitle.getSize()));
		chart.getLegend().setItemFont(new Font("����", oldLegend.getStyle(), oldLegend.getSize()));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("����", oldLegend.getStyle(), oldLegend.getSize()));

		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

}

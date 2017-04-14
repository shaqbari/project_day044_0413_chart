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

	// 성별 분석
	public void getGenderData() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select gender, count(gender) as 응시자수");
		sql.append(" , (select count(*) from score) as 총학생");
		sql.append(" , (count(gender)/(select count(*) from score)*100) as 비율");
		sql.append(" from score");
		sql.append(" group by gender");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			dataset = new DefaultPieDataset();
			while (rs.next()) {
				dataset.setValue(rs.getString("gender"), rs.getInt("비율"));
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

	// 학년별 분석
	public void getGradeData() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select grade, count(grade) as 응시자수");
		sql.append(" , (select count(*) from score) as 총학생");
		sql.append(" , (count(grade)/(select count(*) from score)*100) as 비율");
		sql.append(" from score");
		sql.append(" group by grade");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			dataset = new DefaultPieDataset();
			while (rs.next()) {
				dataset.setValue(rs.getString("grade"), rs.getInt("비율"));
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
		
		chart = ChartFactory.createPieChart("성적표 분석", dataset, true, true, false);

		// 현재 차트에 설정된 폰트를 한글폰트로 바꾸지 않으면 깨져보인다.
		System.out.println(chart.getTitle().getFont().getFontName());

		// 기존니꺼, 기존니사이즈.
		Font oldTitle = chart.getTitle().getFont();
		Font oldLegend = chart.getLegend().getItemFont();

		chart.getTitle().setFont(new Font("굴림", oldTitle.getStyle(), oldTitle.getSize()));
		chart.getLegend().setItemFont(new Font("굴림", oldLegend.getStyle(), oldLegend.getSize()));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("굴림", oldLegend.getStyle(), oldLegend.getSize()));

		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

}

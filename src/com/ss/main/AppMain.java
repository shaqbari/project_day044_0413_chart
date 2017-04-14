package com.ss.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class AppMain extends JFrame implements ActionListener {
	JPanel p_center, p_east, p_south;
	JTable table;
	JScrollPane scroll;
	JButton bt_save, bt_graph;

	Connection con;
	DBManager manager = DBManager.getInstance();
	MyModel model;
	PieChartPanel pie;
	
	JFileChooser chooser;
	String path="E:/git/java_workspace3/project_day044_0413_chart/res/";
	File file;
	FileOutputStream fos;
	

	public AppMain() {
		con = manager.getConnection();

		p_center = new JPanel();
		p_east = new JPanel();
		p_south = new JPanel();
		table = new JTable(model = new MyModel(con));
		scroll = new JScrollPane(table);
		bt_save = new JButton("엑셀로 저장");
		bt_graph = new JButton("그래프 보기");

		p_center.setLayout(new BorderLayout());
		p_east.setPreferredSize(new Dimension(450, 450));
		
		pie=new PieChartPanel(con); //원하는 시점에 붙이기 위해 생성자에서
		
		chooser=new JFileChooser(path);
		
		p_center.add(scroll);
		p_south.add(bt_save);
		p_south.add(bt_graph);

		add(p_center);
		add(p_east, BorderLayout.EAST);
		add(p_south, BorderLayout.SOUTH);

		// 윈도우와 리스너 연결
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				manager.disConnect(con);
				System.exit(0);
			}
		});

		// 버튼과 연결
		bt_save.addActionListener(this);
		bt_graph.addActionListener(this);

		setSize(950, 500);
		setVisible(true);
	}

	// 엑셀로 저장
	public void save() {		
		HSSFWorkbook workbook=new HSSFWorkbook();//엑셀 파일개념
		HSSFSheet sheet=workbook.createSheet("시험응시정보");		
		for (int i = 0; i < table.getRowCount(); i++) {//exel의 row결정
			//HSSFRow를 생성한다.
			HSSFRow row=sheet.createRow(i);
			for (int j = 0; j < table.getColumnCount(); j++) {
				//HSSFColumn을 생성한다.
				System.out.print(table.getValueAt(i, j)+", ");
				HSSFCell cell=row.createCell(j);
				cell.setCellValue((String)table.getValueAt(i, j));
			}
			System.out.println("");
		}
		
		int result=chooser.showSaveDialog(this);
		if (result==JFileChooser.APPROVE_OPTION) {
			//사용자는 확장자.xls가 붙어있어야 한다
			File file=chooser.getSelectedFile();
			
			try {
				fos=new FileOutputStream(file);
				workbook.write(fos);
				JOptionPane.showMessageDialog(this, "엑셀 생성완료");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos!=null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	// 그래프보기
	public void graph() {		
		p_east.add(pie.showChart());
		p_east.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj=(Object)e.getSource();
		if (obj==bt_save) {
			save();
		}else if (obj==bt_graph) {
			graph();
		}
	}

	public static void main(String[] args) {
		new AppMain();
	}

}

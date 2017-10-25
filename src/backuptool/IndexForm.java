package backuptool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;



public class IndexForm implements ActionListener{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 460;
	public static JButton backup=null;
	public static JButton recover=null;
	
	public static JTextArea textArea=null;
	public static JTextField field=null;

	IndexForm(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame frame = new JFrame("备份还原工具");
		frame.setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		int x = (width - WIDTH)/2;
		int y = (height - HEIGHT)/2;
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.setLayout(null);
		    
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
	    panel1.setBounds(0, 0, 794, 90);
	    panel1.setBorder(BorderFactory.createTitledBorder("操作区"));
	    
	    
	    backup = new JButton("备份");
		backup.setBounds(100, 30, 200, 30);
		backup.addActionListener(this);
		
		recover = new JButton("还原");
		recover.setBounds(500, 30, 200, 30);
		recover.addActionListener(this);
	
		
		panel1.add(backup);
		panel1.add(recover);
	    
	    JPanel panel2 = new JPanel();
	    TitledBorder td = BorderFactory.createTitledBorder("响应消息");
	    td.setTitleJustification(3);
	    panel2.setBorder(td);
	    panel2.setLayout(new GridLayout(1, 1, 0, 0));
	    panel2.setBounds(0, 90, 795, 330);
	    
	    textArea = new JTextArea();
	    textArea.setFont(FormStyle.textArea_style);
	    textArea.setForeground(Color.blue);
	    textArea.setEditable(false);
	    
	    JScrollPane text=new JScrollPane(textArea);
	    panel2.add(text);
	    
		
		frame.add(panel1);
		frame.add(panel2);
		
		frame.setVisible(true);
		frame.setResizable(false);
		
	}
	
	public static void main(String[] args) {
		new IndexForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == backup){
			int isbackup = JOptionPane.showConfirmDialog(null, "确定备份吗？", "提示！", JOptionPane.YES_NO_OPTION);
			if(isbackup == JOptionPane.YES_OPTION){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setApproveButtonText("备份");
				fileChooser.setApproveButtonToolTipText("点击开始备份");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("选择数据库备份路径");
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if(returnVal == JFileChooser.APPROVE_OPTION){
				File file= fileChooser.getSelectedFile();
				textArea.setText("");
				textArea.append("您选择的文件路径为:"+file.getAbsolutePath()+"\r\n");	
				if(file.isDirectory()){
						String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					    File f = new File(file,"backup"+time+".sql");
			    	  if(!f.exists()){
					    	try {
								f.createNewFile();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					    }
						try{
							 new Thread(new Runnable() {//启动新线程
					                public void run() {   
					                	textArea.append("正在备份，请稍后..."+"\r\n");
					                	Backup backup = new Backup();
										try {
											String result = backup.backup(f);
											textArea.append(result+"\r\n\r\n\r\n\r\n");
											IndexForm.textArea.setCaretPosition(IndexForm.textArea.getText().length());
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
					                }   
					   
					            }).start(); //  
							
						}catch(Exception err){
							textArea.setText("备份失败"+"\r\n");
							err.printStackTrace();
							return;
						}
					}else{
						textArea.setText("你选择的不是文件夹"+"\r\n");
					}
				}else{
					textArea.setText("已取消备份"+"\r\n");
				}
			}
			if(isbackup == JOptionPane.NO_OPTION){
				textArea.setText("您没选择备份路径"+"\r\n");
			}
		}else if(e.getSource() == recover){
			int isrecover = JOptionPane.showConfirmDialog(null, "确定还原吗？", "提示！", JOptionPane.YES_NO_OPTION);
			if(isrecover == JOptionPane.YES_OPTION){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setApproveButtonText("还原");
				fileChooser.setApproveButtonToolTipText("点击开始还原");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.addChoosableFileFilter(new SqlFileFilter("sql"));
				fileChooser.setDialogTitle("选择用以恢复的sql文件");
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file= fileChooser.getSelectedFile();
					textArea.setText("");
					textArea.append("您选择的文件为:"+file.getAbsolutePath()+"\r\n");
					if(file.exists()){
						try {
							
							 new Thread(new Runnable() {//启动新线程
					                public void run() { 
					                	Backup backup = new Backup();
										try {
											String result = backup.recover(file);
											textArea.append(result+"\r\n\r\n\r\n\r\n\r\n");
											IndexForm.textArea.setCaretPosition(IndexForm.textArea.getText().length());
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
					                }   
					            }).start(); //
							
						} catch (Exception err) {
							textArea.append("还原失败"+"\r\n");
							err.printStackTrace();
							return;
						}
					}else{
						textArea.setText("文件有误"+"\r\n");
					}
				}else{
					textArea.setText("已取消还原"+"\r\n");
				}
			}else{
				textArea.setText("没有选择还原文件"+"\r\n");
			}
		}
		
		
		
	}
	
}

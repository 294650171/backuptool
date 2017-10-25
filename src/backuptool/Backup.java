package backuptool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JTextArea;


public class Backup{
	
	public String backup(File file)throws Exception{
			try{	
				
				StringBuffer cmd = new StringBuffer();
				cmd.append("mysqldump ");
				cmd.append(" -h"+SysUtils.HOST);
				cmd.append(" -u"+SysUtils.DB_USER);
				cmd.append(" -p"+SysUtils.DB_PWD);
				cmd.append(" "+SysUtils.DB_NAME);
			    Process child = Runtime.getRuntime().exec(cmd.toString());  
			    InputStream in = child.getInputStream(); 
			    BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
			   
			    OutputStream os = new FileOutputStream(file);
			    OutputStreamWriter writer = new OutputStreamWriter(os,"utf-8");
			    String str = null;
		        StringBuffer sb = new StringBuffer();
		        while((str = br.readLine()) != null){
		            sb.append(str+"\r\n");
		            IndexForm.textArea.append(str+"\r\n");
					IndexForm.textArea.setCaretPosition(IndexForm.textArea.getText().length());
		        }
		        IndexForm.textArea.append("正在写入"+"\r\n");
				int length = IndexForm.textArea.getText().length(); 
				IndexForm.textArea.setCaretPosition(length);
		        writer.write(sb.toString());
		        writer.flush();
		        IndexForm.textArea.append("写入完成"+"\r\n");
		        IndexForm.textArea.setCaretPosition(IndexForm.textArea.getText().length());
		        in.close();
		        os.close();
		        writer.close();
			    br.close();
		        if(child.waitFor() == 0){
		        	return "备份成功！备份文件为"+file.getAbsolutePath();
			    }else{
			    	return "备份失败";
			    }
		        
			    
			}catch(Exception e){
				e.printStackTrace();
				return "备份失败";
		}
		
	}
	
	public String recover(File file)throws Exception{
		try{
			if(file.isFile()){
				StringBuffer cmd = new StringBuffer();
				cmd.append("mysql ");
				cmd.append(" -h"+SysUtils.HOST);
				cmd.append(" -u"+SysUtils.DB_USER);
				cmd.append(" -p"+SysUtils.DB_PWD);
				cmd.append(" "+SysUtils.DB_NAME);
				Process process = Runtime.getRuntime().exec(cmd.toString());
			    OutputStream outputStream = process.getOutputStream();		    
			    OutputStreamWriter writer = new OutputStreamWriter(outputStream,"utf-8");
			    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()),"utf-8"));
			    String str = null;
		        StringBuffer sb = new StringBuffer();
		        IndexForm.textArea.append("开始读取文件"+"\r\n");
		        while((str = br.readLine()) != null){
		            sb.append(str+"\r\n");
		            IndexForm.textArea.append(str+"\r\n");
		            IndexForm.textArea.setCaretPosition(IndexForm.textArea.getText().length());
		        }
		        writer.write(sb.toString());
		        IndexForm.textArea.append("正在还原"+"\r\n");
		        IndexForm.textArea.setCaretPosition(IndexForm.textArea.getText().length());
		        writer.flush();
		        br.close();
		        writer.close();
		        outputStream.close();
		        if(process.waitFor() == 0){
		        	return "还原完成";
		        }else{
		        	return "还原失败";
		        }
			}
		}catch(Exception e){
			e.printStackTrace();
			return "还原失败";
		}
		return "还原失败";
	}
}
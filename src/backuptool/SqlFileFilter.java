package backuptool;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SqlFileFilter extends FileFilter{
	String ext;
	

	public SqlFileFilter(String ext) {
		this.ext = ext;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		}
		
		String fileName = f.getName();
		int index = fileName.lastIndexOf(".");
		if(index > 0 && index < fileName.length()-1){
			String extension = fileName.substring(index+1).toLowerCase();
			if(extension.equals(ext)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getDescription() {
		if(ext.equals("sql"))
			return "sql source File(*.sql)";
		return "";
	}
	
}

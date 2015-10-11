



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.parser.Parser;

public class Utils {

	public final static String BR=System.getProperty("line.separator");
	
	public static String getFileAsString(File input,String encoding)throws Exception{
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(input),encoding));
		String s="";
		String str="";
		while((s=in.readLine())!=null){
			str=str+s+System.getProperty("line.separator");
		}in.close();
		return str;
	}
	/**
	 * @return Jsoup Document by taking a String
	 * */
	public static Document getFileAsDocument(File input,String encoding)throws Exception{
		Document xmlDoc=null;
		if(input.length()!=0){
			String str=new Scanner(new FileInputStream(input),encoding).useDelimiter("\\A").next();
			xmlDoc=Jsoup.parse(str,"",Parser.xmlParser());
			xmlDoc.outputSettings().prettyPrint(false);
		}
		
		return xmlDoc;
	}
	/**
	 * @param str to be written
	 * @param file to be written to
	 *  
	 ***/
	public static void writeFileToDir(String str,File outFile)throws Exception{
		BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
		out.write(str);
		out.close();
		System.out.println("Output written to File:"+outFile);
	}

	public static String removeSymbols(String s)throws Exception{
		return s;
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	public static void copyFolder(File src, File dest)throws IOException{
	 
	    	if(src.isDirectory()){
	 
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		   System.out.println("Directory copied from " 
	                              + src + "  to " + dest);
	    		}
	 
	    		//list all the directory contents
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	    	        
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	        System.out.println("File copied from " + src + " to " + dest);
	    	}
	    }
	}

package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import Config.Config;
import acs.tools.publish.functest.MCalss;

public class createTxtFile extends MCalss{
        
        
	private static final Logger logger = Logger.getLogger(createTxtFile.class); 
	
	private static String filePath = Config.SourcePath;
	private static String readFilePath = Config.NewPath;
	private static String fileNamePath;
	
	/**
	 * 创建文件，判断是否有该文件存在，存在删除再建
	 * @param name 文件名
	 * @return flag
	 * @throws Exception 抛出异常
 	 */
	
	public static boolean createFile(String name,  Logger logger) throws Exception{
				
				boolean flag = false;
				if(name != "组件名称"){
				        fileNamePath = readFilePath +"/"+ name + ".txt";				        
				}else {
				        fileNamePath = filePath +"/"+ name + ".txt";
				}				
				File file = new File(fileNamePath);
				if (!file.exists()){
						file.createNewFile();
						flag = true;
				}else {
				        file.delete();
				        file.createNewFile();
				        flag = true;
				        logger.info(">>>>>>>>>>>>>::新建文件名称："+name+"----------------");
				}
				return flag;
	}
	
	/**
	 * 文件写入
	 * @param newFile  文件数据
	 * @return
	 * @throws IOException
	 */
	
	public static  boolean wirteToFile(Object newFile, Logger logger) throws IOException {
				
				boolean flag = false;
				String fileInput = newFile +"\t";
				String temp = null;
				
				FileInputStream fis = null;
				InputStreamReader isr = null;
				BufferedReader  br = null;
				
				FileOutputStream fos = null;
				PrintWriter pw = null;
				
				try {
							File file = new File(fileNamePath);
							fis = new FileInputStream(file);
							isr = new InputStreamReader(fis);
							br = new BufferedReader(isr);
							StringBuffer buf = new StringBuffer();
							for (int i =1; (temp = br.readLine()) != null; i++){
									buf = buf.append(temp);
									buf = buf.append(System.getProperty("line.separator"));
							}
							buf.append(fileInput);
							fos = new FileOutputStream(file);
							pw = new PrintWriter(fos);
							pw.write(buf.toString().toCharArray());
							logger.info(">>>>>>>>>>>>>::写入文件："+newFile+"----------------");
							pw.flush();
							flag = true;
				} catch (IOException e){
							throw e;
				} finally {
						if (pw != null){
								pw.close();
						}if(fos != null){
								fos.close();
						}if(br != null){
								br.close();
						}if(isr != null){
								isr.close();
						}if(fis != null){
								fis.close();
						}
				}				
				return flag;
	}
	
	
	/**
	 * 获取src下的com文件夹
	 * @param filePath 文件路劲
	 */
	
	public static void getFiles(String filePath) {
	        
	        
	        try {
//	                createFile("组件文件夹名");
	                int com = 1;
                        File files = new File(filePath);
                        if(files.isDirectory()) {
                                File[] file = files.listFiles();
                                for (int i = 0 ; i < file.length;i++){
                                        if(file[i].getName().contains("com.yss.sofa.swallow.test")){
                                                getFiles(file[i].getAbsolutePath());  //递归调用
//                                                wirteToFile(com+"\t", logger);                                               
                                        }else if(file[i].getName().contains("com.yss")) {
                                                wirteToFile(file[i].getName(), logger);                                                
                                                com +=1;
                                                logger.info(">>>>>>>>>>>>>::组件名获取完成！----------------");
                                        }else  if(file[i].isDirectory()){
                                                getFiles(file[i].getAbsolutePath());  //递归调用
                                        }else{
                                                continue;
                                        }
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
	}
	
	
	/**
	 * 获取jar文件名称，并提取版本号之前名称
	 * @param filePath 文件路径
	 * @return 
	 */
public static  String getFileName(String filePath){
	        
                String fbName = null;
	        try{
	                File file = new File(filePath);
	                File[] files = file.listFiles();
	                for(int i = 0; i < files.length; i++){
	                        if(files[i].getName().contains("-SNAPSHOT")){
	                                wirteToFile(files[i].getName().substring(0, files[i].getName().indexOf("-")), logger);  // 提取版本号之前的文件名称
	                        }else if(files[i].getName().equals(Config.FBFileName)){
	                                fbName = files[i].getName();
	                        }
	                }
	        }catch (Exception e){
	                e.printStackTrace();
	        }
	        logger.info(">>>>>>>>>>>>>::.jar文件获取完成！----------------"); 
	        return fbName;
//	        logger.info(">>>>>>>>>>>>>::.jar文件获取完成！----------------"); 
	}
	
	
	
	/**
	 * 读取组件名称文件进行换行处理
	 * @param readerFileName   文件名称
	 * @param logger 日志输出
	 * @return
	 */
	
public static boolean  readerFile(String readerFileName, Logger logger){
	    
	    boolean flag = false;
	    try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
	        
            /* 读入TXT文件 */  
            String pathname = readFilePath +"/"+ readerFileName + ".txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader  
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = "";  
            line = br.readLine().replace(", ", "\n");  
            createFile("组件排列", logger);
            while (line != null) { 
                wirteToFile(line, logger);
                line = br.readLine().replace(", ", "\n"); // 一次读入一行数据               
                    }             
	    } catch (Exception e) {  
//            e.printStackTrace();  
        }  
	logger.info(">>>>>>>>>>>>>::"+readerFileName+"读取完成！----------------");   
        return flag;	    
	}
	
	
	/**
	 * 读取出组件名称之外的txt文件
	 * @param readerFileName  文件名
	 * @param logger 日志输出
	 * @return
	 */
	
@SuppressWarnings({ "unchecked", "resource" })
public static ArrayList<String>  readerFileT(String readerFileName, Logger logger){
        
        ArrayList<String>  fileList = new ArrayList<String>();
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = readFilePath +"/"+ readerFileName + ".txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader  
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = "";  
            line = br.readLine().replace("\t\t", "");          
            while (line != null) { 
//                System.out.println(line);
                fileList.add(line);
                line = br.readLine().replace("\t\t", ""); // 一次读入一行数据  
            } 
                    
        } catch (Exception e) {  
//            e.printStackTrace();  
        }  
        logger.info(">>>>>>>>>>>>>::"+readerFileName+"读取完成！----------------");
        return fileList;               
    }

public static void sleep(int number){
                try{
                                int s = number*1000;
                                Thread.sleep(s);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                                }                        
                }

/**
 * 比较两个txt文件
 * @param filePath1   文件路劲
 * @param filePath2   文件路劲
 * @param logger
 */

public  static void comSameFile(String filePath1, String filePath2, Logger logger){
        
        FileInputStream fps = null;
        FileInputStream fps1 =null;
        
        try{
                fps = new FileInputStream(filePath1);
                fps1 = new FileInputStream(filePath2);
                
                int lenght1 = fps.available();
                int lenght2 = fps1.available();
                
                if(lenght1 == lenght2){                                 // 长度一样，则比较内容
                        byte[] data1 = new byte[lenght1];
                        byte[] data2 = new byte[lenght2];
                        //分别将两个文件放入缓冲区！
                        fps.read(data1);
                        fps1.read(data2);
                        
                        for(int i =0; i<lenght1;i++){
                                if(data1[i] != data2[i]){   //判断，只要有一个字节不一样，文件就不一样
                                     logger.info(">>>>>>>>>>>>>::两个文件不相同！----------------");   
                                }
                        }                                //
                        logger.info(">>>>>>>>>>>>>::两个文件完全相同！----------------");
                }else {  // 文件长度判断！
                        logger.info(">>>>>>>>>>>>>::两个文件长度不一样！----------------");
                }
        }catch(FileNotFoundException e){
                e.printStackTrace();
        }catch(IOException e){
                e.printStackTrace();
        }finally {   //关闭文件，防止内存泄漏
                if(fps != null){
                        try{
                                fps.close();
                        }catch (IOException e){
                                e.printStackTrace();
                        }
                }
                if(fps1 != null){
                        try{
                                fps1.close();
                        }catch(IOException e){
                                e.printStackTrace();
                        }
                }
        }
}

//public static void main(String[] args) throws Exception  {
//        
//        getFileName(filePath);
//
//}


}

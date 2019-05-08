package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import acs.tools.publish.functest.MCalss;

public class ZipUtils extends MCalss{
	
                private static final Logger logger = Logger.getLogger(ZipUtils.class);
	
		private static final int BUFFER = 5120000;
		
		
		private static List<File> getAllFiles(File  srcFile, Logger logger){
			
			
					List<File> fileList = new ArrayList<File>();
					File[] file = srcFile.listFiles();
					for(int i = 0; i < file.length; i ++) {
								if(file[i].isFile()) {
									fileList.add(file[i]);
									logger.info(">>>>>>>>>>>>>::add.file:"  +  file[i].getName()+"----------------");
								}
								
								if(file[i].isDirectory()) {
										if (file[i].listFiles().length != 0 ){
												fileList.addAll(getAllFiles(file[i], logger));
										}
										else {
											fileList.add(file[i]);
											logger.info(">>>>>>>>>>>>>::add empty dir:"  +  file[i].getName()+"----------------");
										}
								}
					}
					return fileList;
			
		}
		
		private static String getRelativeFile(String dirPath, File file){
			
						File dir = new File(dirPath);
						String RelativePath = file.getName();
						
						while (true) {
							       file = file.getParentFile();
							       
							       if ( file == null){
							    	   break;
							       }
							       if(file.equals(dir)){
							    	   break;
							       }else {
							    	   RelativePath = file.getName()  + "/" + RelativePath;
							       }
							       
						}
						return RelativePath;
		}
		
		
		public static void  addFileToZip(String  srcPath, String fileName, Logger logger) throws IOException{
						
			
							
							logger.info(">>>>>>>>>>>>>::开始进行文件压缩！----------------");
							File srcfile = new File(srcPath);
							List<File> fileList =  getAllFiles(srcfile, logger);
							byte [] buffer = new byte[BUFFER];
							ZipEntry  zfp = null;
							int readLenght = 0;
							
							try{
										ZipOutputStream zipoutputstream =  new ZipOutputStream(new FileOutputStream(fileName));
										for (File file : fileList){
													if(file.isFile()){
														zfp = new ZipEntry(getRelativeFile(srcPath, file));
														zfp.setSize(file.length());
														zfp.setTime(file.lastModified());
														zipoutputstream.putNextEntry(zfp);
														
														InputStream ipst = new BufferedInputStream(new FileInputStream(file));
														
														while ((readLenght = ipst.read(buffer, 0, BUFFER)) != -1) {
																	zipoutputstream.write(buffer, 0, readLenght);
														}
														ipst.close();
														logger.info(">>>>>>>>>>>>>::file addFileToPath:"  + file.getCanonicalPath()+"----------------");
													}else{
														zfp = new ZipEntry(getRelativeFile(srcPath, file) + "/" );
														zipoutputstream.putNextEntry(zfp);
														logger.info(">>>>>>>>>>>>>::file addFileToPath:" + file.getCanonicalPath() +"/"+"----------------");
//														System.out.println("file addFileToPath:" + file.getCanonicalPath() + "/");
													}
										}
										zipoutputstream.close();
							} catch (FileNotFoundException e){
							                                logger.info(">>>>>>>>>>>>>::文件压缩失败！----------------");
							} catch (IOException E) {
							                                logger.info(">>>>>>>>>>>>>::文件压缩失败！----------------");
							}
							logger.info(">>>>>>>>>>>>>::文件压缩成功！----------------");

			
			
		}
		
		
		

}

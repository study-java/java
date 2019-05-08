package acs.tools.publish.functest;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import utils.createTxtFile;



public class ExampleExcel extends MCalss{
        
        private static final Logger logger = Logger.getLogger(ExampleExcel.class);
	
	private static HSSFWorkbook wb = null;
//	private String filePath;
//	private String anotherfilePath;
	
//	public ExampleExcel(){
//		
//		this.filePath = filePath;
//		this.anotherfilePath = anotherfilePath;
//		
//	}
	
	createTxtFile ctf = new createTxtFile();
	
	@SuppressWarnings("unchecked")
        public ArrayList<List>ReadExcel(String filePath, Logger logger){
		
		ArrayList list1 = new ArrayList();
		
		try{
				
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
				HSSFSheet sheet = workbook.getSheetAt(0);
				
				int rowTotalCount = sheet.getLastRowNum();
				int columnCount = 12;
				if(columnCount == sheet.getRow(0).getPhysicalNumberOfCells()){
				        columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
				}
				logger.info(">>>>>>>>>>>>>::行数为"+ rowTotalCount +"   列数为："+columnCount+"----------------");
				for (int i = 1; i <= rowTotalCount; i++) {
					
						HSSFRow row = sheet.getRow(i);
						ArrayList listRow = new ArrayList();	
						 int[] list = new int[]{3, 2, 4};
						 for(int j = 0; j<list.length;j++){
						         HSSFCell cell = row.getCell(list[j]); 
						         if(cell != null){
						                 cell.setCellType(Cell.CELL_TYPE_STRING);                                                                                                                   
	                                                         listRow.add(getValue(cell).trim());
						         }	
						 }	
						 list1.add(listRow);	 
				}
				
		}	catch	(FileNotFoundException e) {
					e.printStackTrace();
		}	catch (IOException  e) {
					e.printStackTrace();
		}
		logger.info(">>>>>>>>>>>>>::返回列表----------------");
//		System.out.println(list1);
		return list1;		
	}
	
	
	/**
	 * 读取另一份Excel文件，保存成list集合
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
        public  ArrayList<List> ReadAnotherExcel(String anotherfilePath, Logger logger){
		
	                ArrayList  list2 = new ArrayList();
	       			
			try {				
					HSSFWorkbook  workbook = new HSSFWorkbook(new FileInputStream(anotherfilePath));
					HSSFSheet sheet = workbook.getSheetAt(0);
					
					int rowTotalCount = sheet.getLastRowNum();					
					int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
					logger.info(">>>>>>>>>>>>>::行数为"+ rowTotalCount +"   列数为："+columnCount+"----------------");
					for ( int i = 1; i <= rowTotalCount; i++) {							
							HSSFRow   row = sheet.getRow(i);							      
							ArrayList listRow = new ArrayList();
							        int[] list = new int[]{0, 2, 3};
	                                                        for(int j = 0; j<list.length;j++){
	                                                                HSSFCell cell = row.getCell(list[j]); 
	                                                                cell.setCellType(Cell.CELL_TYPE_STRING);    
	                                                                listRow.add(getValue(cell).trim());
	                                                                }
							list2.add(listRow);
					}
		    	}	catch(FileNotFoundException e) {		    		
		    					e.printStackTrace();
		    	}catch (IOException e) {		    			
		    					e.printStackTrace();
				}
			logger.info(">>>>>>>>>>>>>::返回列表----------------");
//		System.out.println(list2);
		return list2;
	}
	
	
	/**
	 * 获取excel表共某一列的所有值
	 * @param comFilePath   文件名称
	 * @param cellNum   列号
	 * @param logger  日志输出
	 * @return  返回
	 * @throws Exception  异常
	 */
	
@SuppressWarnings({ "unchecked", "static-access" })
public  int componentName(String  comFilePath,int cellNum, Logger logger) throws Exception{
        
        ArrayList  list3 = new ArrayList();
        int s = 0;
            
            try {                
                    HSSFWorkbook  workbook = new HSSFWorkbook(new FileInputStream(comFilePath));
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    
                    int rowTotalCount = sheet.getLastRowNum();                  
//                    int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();   
                    int columnCount = 12;
                    if(columnCount == sheet.getRow(0).getPhysicalNumberOfCells()){
                            columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
                    }
                    logger.info(">>>>>>>>>>>>>::行数为"+ rowTotalCount +"   列数为："+columnCount+"----------------");
                    for ( int i = 1; i <= rowTotalCount; i++) {                            
                            HSSFRow   row = sheet.getRow(i);
                            HSSFCell cell = row.getCell(cellNum); 
                            cell.setCellType(Cell.CELL_TYPE_STRING); 
                            list3.add(getValue(cell).replace("\n", ", "));                                                                                                                
                    }
                }   catch(FileNotFoundException e) {                    
                                e.printStackTrace();
                }catch (IOException e) {                        
                                e.printStackTrace();
                }
            //判断获取的是组件名称还是版本号  列数为10、11
            if(cellNum == 10) {
                    System.out.println(list3);
                    String name = null;
                    String name1 = null;
                    ctf.createFile("svn版本号", logger);
                    for (int i = 0;i<list3.size();i++){    
                            name = list3.get(i).toString().replaceAll("版本号:", "版本号：");
                            name1 =name.replaceAll("版本号：", "");
                            if(name1.equals("无") || name1.equals("")){                                   
                            }else{
                                    ctf.wirteToFile(name1.replaceAll("、", "\t\n"), logger);  
                                    logger.info(">>>>>>>>>>>>>::"+name1.replaceAll("、", "\t\n") +"----------------");
                            }                           
                    }
                    logger.info(">>>>>>>>>>>>>::第一版版本号文件写入完成！----------------");
                                     
                    ctf.createFile("SVNList版本号", logger);
                    List<String> SVNList = ctf.readerFileT("svn版本号", logger);
                        for(int j = 0;j<SVNList.size(); j++){
                                for(int h = SVNList.size() -1; h>j; h--){
                                        if(SVNList.get(h).trim().equals(SVNList.get(j).trim())){
                                                SVNList.remove(h);
                                            }                                                                                                                         
                      }
                      ctf.wirteToFile(SVNList.get(j), logger); 
            }
             logger.info(">>>>>>>>>>>>>::版本号写入完成！----------------");
       }else {
               ctf.createFile("组件获取", logger);
               for (int i = 0;i<list3.size();i++){                            
                           for(int j = list3.size() -1; j>i; j--){
                                       if(list3.get(j).equals(list3.get(i))){
                                               list3.remove(j);
                                 }
                         }
                          ctf.wirteToFile(list3.get(i), logger);
               }
               logger.info(">>>>>>>>>>>>>::第一版组件名称写入完成！----------------");
               ctf.readerFile("组件获取", logger);
               List<String> fileList1 = ctf.readerFileT("组件排列", logger);
               ctf.createFile("组件名称", logger);
               for (int l = 0;l<fileList1.size();l++){
                   for(int k = fileList1.size() -1; k>l; k--){
                               if(fileList1.get(k).trim().equals(fileList1.get(l).trim())){
                                       fileList1.remove(k);
                                   }
                       }
                       ctf.wirteToFile(fileList1.get(l), logger); 
                       s = fileList1.size();
                       }
               logger.info(">>>>>>>>>>>>>::组件名称写入完成！----------------");
       }
        return s;
    }
	
/**
 * cell值类型的修改
 * @param cell cell值
 * @return 返回值
 */


private  String getValue(Cell cell){
				
				if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					return String.valueOf(cell.getBooleanCellValue());
				} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					
					Object inputValue = null;
					Long longVal = Math.round(cell.getNumericCellValue());
					Double doubleVal = cell.getNumericCellValue();
					if (Double.parseDouble(longVal + ".0") == doubleVal){
						inputValue = longVal;
					} else {
						inputValue = doubleVal;
					}
					DecimalFormat df = new DecimalFormat("#");
//					System.out.println(String.valueOf(df.format(inputValue)));
					return String.valueOf(df.format(inputValue));
					
				} else {
					return String.valueOf(cell.getStringCellValue());
				}
	}
	

	
	
	
	private static boolean fileExist(String fileDir){
		
				boolean flag = false;
				File file = new File(fileDir);
				flag = file.exists();
				return flag;
					
	}
	
	public static boolean sheetExist(String fileDir, String sheetName) throws Exception{
				
					boolean flag = false;
					File file = new File(fileDir);
					if(file.exists()){
							try{
									  wb = new  HSSFWorkbook(new FileInputStream(file));
									 HSSFSheet sheet = wb.getSheet(sheetName);
									 if(sheet != null)
										 flag = true;
							} catch (Exception e ){
											throw e;									 
						}
					}else {
						flag = false;
				}
		return flag;
	}
	
	
	public static void createExcel(String fileDir, String sheetName, String titleRow[]) throws Exception{
		
					 wb = new HSSFWorkbook();
					HSSFSheet sheet1 = wb.createSheet(sheetName);
					FileOutputStream out = null;
					try{
							HSSFRow row = wb.getSheet(sheetName).createRow(0);
							for(short i = 0; i<titleRow.length; i++){
									HSSFCell cell = row.createCell(i);
									cell.setCellValue(titleRow[i]);
							}
							out  = new FileOutputStream(fileDir);
							wb.write(out);
					} catch(Exception e) {
							throw e;
					} finally {
							try{
									out.close();
							} catch (IOException e) {
								    e.printStackTrace();
							}
					}
	}
	
	public static boolean deleteExcel(String fileDir){
		
					boolean flag = false;
					File file = new File(fileDir);
					if( !file.exists()) {
							return flag;
					} else {
								if (file.isFile()) {
										file.delete();
										flag = true;
								}
					}
					return flag;
	}
	
	public static void writToExcel(String fileDir, String sheetName, Object object) {
					
					File file = new File(fileDir);
					try{
							wb = new HSSFWorkbook(new FileInputStream(file));
					} catch ( FileNotFoundException e) {
							e.printStackTrace();
					} catch (IOException e) {
						    e.printStackTrace();
					}
					
					FileOutputStream out = null;
					HSSFSheet sheet = wb.getSheet(sheetName);
					
					int rowCount = sheet.getLastRowNum()+1;
					int columnCount1 = sheet.getRow(0).getLastCellNum()+1;
//					System.out.println("行数为："+rowCount+"列数为："+columnCount1);
					try{
							Row row = sheet.createRow(rowCount);
							Class<? extends Object> class_ = object.getClass();
							HSSFRow titleRow = sheet.getRow(0);
							if(titleRow != null){
								 for(int columnIndex = 0; columnIndex<columnCount1; columnIndex++){
									 		String title = titleRow.getCell(columnIndex).toString().trim().toString().trim();
									 		String Utitle = Character.toUpperCase(title.charAt(0))+title.substring(1,title.length());
									 		String methodName = "get"+Utitle;
									 		Method method = class_.getDeclaredMethod(methodName);
									 		String data = method.invoke(object).toString();
									 		Cell cell = row.createCell(columnIndex);
									 		cell.setCellValue(data);
								 }
							}
							out = new FileOutputStream(fileDir);
							wb.write(out);
					} catch(Exception e){
								e.printStackTrace();						
					}finally{
								try {
									   out.close();
								} catch (IOException e){
											e.printStackTrace();
							}										
				}				
	}
	
	/**
	 * 两文件excel文件流比较 并输出txt文件
	 * @param filePath   源文件
	 * @param anotherfilePath   对比文件
	 * @param logger   日志输出
	 * @throws Exception  异常
	 */
	
	@SuppressWarnings({ "static-access", "unchecked", "unused", "null" })
	public void CompareExcel(String filePath, String anotherfilePath, Logger logger) throws Exception{
		
		ArrayList sources = ReadExcel(filePath,logger);
		ArrayList datas = ReadAnotherExcel(anotherfilePath,logger);
		createTxtFile ctf = new createTxtFile();

		ArrayList<String> lst1 = null;
		ArrayList<String> lst2 = null;
		ArrayList<String> lst3 = new ArrayList<String>();
		ArrayList<String> lst4 = new ArrayList<String>();
									
		if(anotherfilePath.contains("Bug")){
		        ctf.createFile("bug列表核对一致", logger);                                                                                                                                          
		        int passNum = 0;
	                for(int i = 0; i<sources.size(); i++){	                        
	                        lst1 = (ArrayList<String>) sources.get(i);	                        
	                        for(int j = 0; j< datas.size(); j++){ 	      
	                                lst2 = (ArrayList<String>) datas.get(j);
	                                if(lst1.equals(lst2)){
	                                        lst3.addAll(lst2);
	                                        logger.info(">>>>>>>>>>>>>::"+"\n"+sources.get(i)+"\n"+datas.get(j) + "\r"+"核对一致的Bug编号----------------");	                                        
	                                        passNum +=1;
	                                        ctf.wirteToFile(passNum+"."+"\t", logger);
	                                        ctf.wirteToFile(sources.get(i), logger);
	                                        ctf.wirteToFile(datas.get(j) + "\r"+"编号核对一致", logger);
	                                        break;
	                                }else{	                                            
	                                        logger.info(">>>>>>>>>>>>>::"+"\n"+sources.get(i)+"\n"+datas.get(j) + "\r"+"不一致的Bug编号----------------");	                                        
	                                        }    
	                                } 
	                        }
	              //把核对不对的编号写入txt文件中
	                ctf.createFile("Bug列表核对不一致", logger);
	                for(int h = 0; h< datas.size(); h++){
	                        lst4 = (ArrayList<String>) datas.get(h);
	                        String s = lst4.get(0);
	                        if(lst3.contains(s)){                           
	                        }else{
	                                ctf.wirteToFile(lst4 + "\r", logger);
	                        }
	                }
	                
		}else {
		        ctf.createFile("需求列表核对一致", logger);
		        int passNum = 0;
	                for(int i = 0; i<sources.size();i++){     
	                        lst1 = (ArrayList<String>) sources.get(i);
	                        for(int j = 0; j< datas.size()-i;j++){  
	                                lst2 = (ArrayList<String>) datas.get(j);
	                                if(lst1.equals(lst2)){
	                                        lst3.addAll(lst2);
	                                        logger.info(">>>>>>>>>>>>>::"+"\n"+sources.get(i)+"\n"+datas.get(j) + "\r"+"核对一致的需求编号----------------");
	                                        passNum +=1;
	                                        ctf.wirteToFile(passNum+"."+"\t", logger);
	                                        ctf.wirteToFile(sources.get(i), logger);
	                                        ctf.wirteToFile(datas.get(j) + "\r"+"编号核对一致", logger);
	                                        break;
	                                }else{
	                                        logger.info(">>>>>>>>>>>>>::"+"\n"+sources.get(i)+"\n"+datas.get(j) + "\r"+"不一致的需求编号----------------");	                                             
	                                        }                                                                                       
	                                }                                                                                                                       
	                        }
	                //把核对不对的编号写 入txt文件中
	                ctf.createFile("需求列表核对不一致", logger);
	                 for(int h = 0; h< datas.size(); h++){
	                         lst4 = (ArrayList<String>) datas.get(h);
	                         String s = lst4.get(0);
	                         if(lst3.contains(s)){                           
	                         }else{
	                                 ctf.wirteToFile(lst4 + "\r", logger);
	                         }
	                 }
		}		 
		logger.info(">>>>>>>>>>>>>::核对结束！----------------");							
	}
}

package acs.tools.publish.functest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class RWExcel {
	
	private String filePath;
	private String anotherfilePath;

	public RWExcel(String filePath, String anotherfilePth){
		
		this.filePath = filePath;
		this.anotherfilePath = anotherfilePth;
		
	}
	
	public ArrayList<List> ReadExcel(int  sheetNum){
		
		ArrayList<List> list = new ArrayList<List>();
		
		try{
				
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
				HSSFSheet sheet = workbook.getSheetAt(sheetNum);
				
				int rowTotalCount = sheet.getLastRowNum();
				int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
				System.out.println("行数为："+rowTotalCount+"列数为："+columnCount);
				for (int i = 0; i <= rowTotalCount; i++) {
					
						HSSFRow row = sheet.getRow(i);
						ArrayList<String>listRow = new ArrayList<String>();
						for (int j = 0; j < columnCount; j++) {
							
								String cell = null;
								if (row.getCell(j) == null) {	
									cell = row.getCell(j)+"";
									listRow.add(cell);
									
								}else{									
											cell = row.getCell(j).toString();
											listRow.add(cell);
								}											
						}						
						list.add(listRow);						
				}
				
		}	catch	(FileNotFoundException e) {
					e.printStackTrace();
		}	catch (IOException  e) {
					e.printStackTrace();
		}	
		return list;
		
	}
	
	/**
	 * 读取另一份Excel文件，保存成list集合
	 */
	
	public  ArrayList<List> ReadAnotherExcel(int  anotherSheetNum){
		
			ArrayList<List> list = new ArrayList<List>();
			
			try {
				
					HSSFWorkbook  workbook = new HSSFWorkbook(new FileInputStream(anotherfilePath));
					HSSFSheet sheet = workbook.getSheetAt(anotherSheetNum);
					
					int rowTotalCount = sheet.getLastRowNum();					
					int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();	
					System.out.println("行数为："+rowTotalCount+"列数为："+columnCount);
					for ( int i = 0; i <= rowTotalCount; i++) {
							
							HSSFRow   row = sheet.getRow(i);							
							ArrayList<String> listRow = new ArrayList<String>();							
							for (int j = 0 ; j < columnCount; j++){
									
									String cell = null;								
									if (row.getCell(j) == null) {
										
											cell = row.getCell(j)+"";											
											listRow.add(cell);
											
									}else{										
										cell = row.getCell(j).toString();
										listRow.add(cell);
									}																	
							}
							list.add(listRow);													
					}
		    	}	catch(FileNotFoundException e) {		    		
		    					e.printStackTrace();
		    	}catch (IOException e) {		    			
		    					e.printStackTrace();
					
				}
		
		return list;
	}
	
	

}

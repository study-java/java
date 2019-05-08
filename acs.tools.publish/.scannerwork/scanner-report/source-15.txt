package utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.arhyme.csv.CSVTokenizer;

import Config.Config;
import acs.tools.publish.functest.MCalss;


public class ExcelScreen extends MCalss{
             
        private  static final Logger logger = Logger.getLogger(ExcelScreen.class);         
        
/**
     * 筛选并重组表格,只筛选第一个sheet 
     * @params originFileName 源文件名
     * @params targetFileName 目标文件名
     * @params columns 只提取原表格中出现过的列(不包括索引)
     * @params filterItems 筛选条件,只提取出现该项的行
     */
    @SuppressWarnings("unused")
public  static void screen(String originFileName, String targetFileName, String[] columns, String filterColumn, String[] filterItems,Logger logger) {
            
            String originFilePath = Config.SourcePath +"/" + originFileName;
            String filterColumnName = filterColumn;
//            if(originFileName.contains("发布申请")){
//                    filterColumnName = "所属客户";
//            }
//            else{
//                    filterColumnName = "客户名称";
//            }
            FileInputStream ipt = null;
            HSSFWorkbook workbook = null;
            ArrayList<Integer> columnIndexList = new ArrayList<Integer>(columns.length);
            int filterColumnIndex = -1;
            HSSFWorkbook workbook_N = null;
            FileOutputStream fos = null;
            int rows = 0;
            int cells = 0;
            
            try{
                    ipt = new FileInputStream(originFilePath);
                    workbook = new HSSFWorkbook(ipt);
                    HSSFFormulaEvaluator  evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    HSSFRow row = sheet.getRow(0);                  
                    HSSFCell cell =null;
                    
                    if (row == null)
                         return;
                    if(originFileName.contains("发布申请")){
                            cells = 12;
                    }else if(originFileName.contains("Bug")){
                            cells = 14;
                    }else {
                            cells = 18;
                    }
                    rows = sheet.getFirstRowNum()+1;
//                    cells = sheet.getRow(0).getPhysicalNumberOfCells()-1;
                    logger.info(">>>>>>>>>>>>>::"+originFileName+"的行数为"+ rows +"   列数为："+cells+"----------------");
                    for (int i = 0; i < rows; i++){
                            for (int j = 0; j< cells; j++){
                                    cell = row.getCell(j);
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    cell.setCellValue(getCellValue(cell));
                                    for (String str : columns){
                                            String value = cell.getStringCellValue(); 
                                            if(str.equals(value)){
                                                    int index = cell.getColumnIndex();
                                                    columnIndexList.add(index);
                                                    if (filterColumnIndex == -1 && filterColumnName.equals(value)){
                                                            filterColumnIndex = index;
                                                    }
                                                    break;                                                   
                                            }                                                                                                                                                                                  
                                    }                                  
                            }                                                                                   
                    }
                    if (columnIndexList.size() == 0 || filterColumnIndex == -1)
                            return;
                    if (targetFileName.endsWith(targetFileName))
                            workbook_N = new HSSFWorkbook();
//                    }else {
//                            workbook_N = new HSSFWorkbook();
//                    }
//                    ipt = new FileInputStream(targetFilePath);
//                    workbook_N = new HSSFWorkbook();
                    HSSFSheet sheet_N = workbook_N.createSheet("Sheet1");                                                                                  //创建sheet1页面
                    int rowindex = 1;                                                                                                                                                             //
                    HSSFRow row_N = sheet_N.createRow(0);
                    HSSFCellStyle cs = workbook_N.createCellStyle();                                                                                                 //创建新excel行
//                    row_N.createCell(0).setCellValue("索引");
                    for (int i = 0; i < columnIndexList.size(); i++) {
                            String value = row.getCell(columnIndexList.get(i)).getStringCellValue();                                                    //获取标题信息
                            row_N.createCell(i).setCellValue(value);                                                                                                               //第一行赋值
                    }
                    int firstRow = sheet.getFirstRowNum();
                    int lastRow = sheet.getLastRowNum(); 
//                    System.out.println(lastRow);
                    for (int h = firstRow; h<=lastRow;h++){
                            HSSFRow row1 = sheet.getRow(h); 
                                    cell = row1.getCell(filterColumnIndex);
                                    if(cell != null){
                                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                            String value = cell.getStringCellValue();
                                            for (String str : filterItems){
                                                    if (str.equals(value)){
                                                            row_N = sheet_N.createRow(rowindex);
                                                            row_N.createCell(0).setCellFormula("ROW()-1");
                                                            for (int i = 0; i < columnIndexList.size(); i++) { 
                                                                    int index = columnIndexList.get(i);                           
                                                                    HSSFCell c = row1.getCell(index);                                                            
                                                                    HSSFCell c_N = row_N.createCell(i);
                                                                    c_N.setCellValue(getCellValue(c));
                                                                    System.out.println(c_N);
                                                                    cs.cloneStyleFrom(c.getCellStyle());
                                                                    c_N.setCellStyle(cs);       
                                                            }  
                                                            rowindex++;
                                                            break;
                                                    }
                                            }
                                    }                                   
                            }
                    fos = new FileOutputStream(targetFileName);
                    workbook_N.write(fos);
            } catch(FileNotFoundException e){
                    e.printStackTrace();
            } catch(EncryptedDocumentException e){
                    e.printStackTrace();
            } catch(IOException e) {
                    e.printStackTrace();
            } finally {
                    if  (ipt != null ){
                            try{
                                    ipt.close();
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }
                    }                       
                    if ( fos != null)  {
                            try{
                                    fos.close();
                            }catch (IOException e){
                                    e.printStackTrace();
                            }
                    }
            } 
            logger.info(">>>>>>>>>>>>>::"+originFileName+"筛选完成！----------------");
    }
       
    private static String getCellValue(Cell c) {

            String r=null;
            if(c==null){
                r="";
            }
            switch(c.getCellType()){   
            case Cell.CELL_TYPE_BLANK://为 空值3
                r="";
                break;
            case Cell.CELL_TYPE_BOOLEAN://boolean型4
                r=c.getBooleanCellValue()+"";
                break;
            case Cell.CELL_TYPE_ERROR://错误 5
                r="";
                break;
            case Cell.CELL_TYPE_FORMULA://公式型 2
                HSSFWorkbook wb = (HSSFWorkbook) c.getSheet().getWorkbook();//取得workbook
                HSSFCreationHelper helper = wb.getCreationHelper();//取得wb的帮助
                HSSFFormulaEvaluator ev = helper.createFormulaEvaluator();//取得helper的公式计算方法
                r=getCellValue(ev.evaluateInCell(c));//调用自身方法，括号里取得cellValue
                break;
            case Cell.CELL_TYPE_NUMERIC://数值型 0
                if(DateUtil.isCellDateFormatted(c)){//如果是excel日期格式
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//规定日期格式
                    Date d = c.getDateCellValue();//取得日期
                    r = s.format(d);//转换日期格式
                }else{
                    r = NumberToTextConverter.toText(c.getNumericCellValue());//数值的话，转换为String
                }
                break;
            case Cell.CELL_TYPE_STRING://字符串型 1
                r=c.getRichStringCellValue().toString();//推荐使用getRichStringCellValue()；
                                                        //getStringCellValue()为老方法
                break;
            default:
                r="";
            }
            return r;
        }
    
    
    /**
     * 读取csv文件
     * @return 返回文件流
     */
    @SuppressWarnings("unused")
public static  List<String> readCSV(Logger logger) {
            
            List<String> allString = new ArrayList<>();
            
            try {
                    File csv = new File("H:/testSvn/需求列表.csv");     // CSV文件路径
                    FileInputStream fps = new FileInputStream(csv);
                    DataInputStream dps = new DataInputStream(fps);
                    BufferedReader br = new BufferedReader(new InputStreamReader(dps, "GBK"));
                    String line = "";
                    String ereryLine = "";
                    try {
                            while ((line = br.readLine()) != null) {
                                    ereryLine = line;
//                                    System.out.println(ereryLine);
                                    allString.add(ereryLine);
                            }
                            logger.info(">>>>>>>>>>>>>::csv表格中所有行数："+allString.size()+"----------------");
                    }catch (Exception e) {
                            e.printStackTrace();
                    }
            } catch (Exception e) {
                    e.printStackTrace();
            }
            logger.info(">>>>>>>>>>>>>::csv数据读取完成！----------------");
            return allString;
     }
    
    /**
     * csv更换为xls文件
     * @param csvFileName csv文件名
     * @param xlsFileName xls文件名
     * @throws IOException  异常抛出
     */
    
 @SuppressWarnings("deprecation")
public final static void CsvToXls(String csvFileName, String xlsFileName, Logger logger) throws IOException {
         
         String csvFilePath = Config.SourcePath +"/"+csvFileName;
         String xlsFilePath = Config.SourcePath +"/"+xlsFileName;
         
         HSSFWorkbook workbook = new HSSFWorkbook();
         HSSFSheet sheet = workbook.createSheet("sheet1");
         logger.info(">>>>>>>>>>>>>::新建Excel的sheet名称：sheet1 ----------------");
         
         BufferedReader br = null;
         
         try{
                 File csv = new File(csvFilePath);     // CSV文件路径
                 FileInputStream fps = new FileInputStream(csv);
                 DataInputStream dps = new DataInputStream(fps);
                 br = new BufferedReader(new InputStreamReader(dps, "GBK"));                                            //读取文件时，需要转换文件流格式
                 int i = 0;
                 while (true) {
                         String line = br.readLine();
                         System.out.println(line);
                         if (line == null)
                                 break;
                         HSSFRow row = sheet.createRow((short) i++);
                         int j = 0;
                         for (CSVTokenizer ctk = new CSVTokenizer(line); ctk.hasMoreTokens();){
                                 String value = ctk.nextToken();  
                                 HSSFCell cell = row.createCell((short)j++);
                                 cell.setCellValue(value);
                         }                                 
                 }
         } finally  {
                 if (br != null)
                         br.close();
         }
         FileOutputStream fos = null;
         try {
                 fos = new FileOutputStream(xlsFilePath);
                 workbook.write(fos);
         } finally {
                 if (fos != null)
                         fos.close();
         }
         logger.info(">>>>>>>>>>>>>::文件转换完成！----------------");
 }
 
 /**
  * 判断cell值为什么类型
  * @param cell 传入值类型
  * @return 返回空
  */
 public final static Object getCellValue1(HSSFCell cell){
         
         if (cell != null){
                 int c = cell.getCellType();
                 if (c == HSSFCell.CELL_TYPE_NUMERIC){
                         int f = cell.getCellStyle().getDataFormat();
                         if (((f >=14 && f <=17) || (f >= 164 && f <= 168) || (f >= 172 && f <= 177) || f == 22))
                                 return  cell.getDateCellValue();
                         double dul = cell.getNumericCellValue();
                         if (dul == Math.floor(dul))
                                 return new Long((long) dul);
                         return new Double(dul);
                 }
                 else if (c == HSSFCell.CELL_TYPE_STRING)
                         return cell.getStringCellValue();
         }
         return null;
 }
    
 
    
 
 /**
  * 复制当前路径下的文件到另一个路径下
  * @param oldPath 老路径
  * @param newPath  新路径
  * @return boolean
  */
    	
public static void copyFolder(String oldPath, String newPath, Logger logger) {
                
                try {
                        (new File(newPath)).mkdirs();                                                                                                                   //如果文件夹不存在，则新建
                        File oldFile = new File(oldPath);
                        String[] file = oldFile.list();
                        File temp = null;
                        for (int i =0;i <file.length;i++){
                                if (oldPath.endsWith(File.separator)) {
                                        temp = new File(oldPath+file[i]); 
                                } else {
                                        temp = new File(oldPath + File.separator+file[i]);
                                }                              
                                if (temp.isFile()){
                                        FileInputStream input = new FileInputStream(temp);
                                        FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                                        byte[] b = new byte[1024 * 5];
                                        int len;
                                        while ((len = input.read(b)) != -1){
                                                output.write(b, 0 , len);
                                        }
                                        output.flush();
                                        output.close();
                                        input.close();
                                }
                                if (temp.isDirectory()) {
                                        copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i], logger);
                                }
                                logger.info(">>>>>>>>>>>>>::文件已复制完成！----------------");
                        }
                } catch (Exception e){
                        logger.info(">>>>>>>>>>>>>::文件复制出错！----------------");
                        e.printStackTrace();
                }             
}
/**
 * 单个文件复制到另一个路径下
 * @param oldPath  老路径
 * @param newPath 新路径
 */

public static void copyFile(String fileName, Logger logger){
        
        
        String srcOldDir = Config.Downloads+"/"+fileName;
        String srcNewDir = Config.SourcePath +"/";
        
        
                try{
                        int bytesum = 0;
                        int byteread = 0;
                        File oldFile = new File(srcOldDir);
                        File file= new File(srcNewDir+File.separator+oldFile.getName());       //获取目标路径的File对象，关键在于目标路径问题，注意复制访问 ，只能是文件不能是文件夹 否则拒绝访问，getpath()得到具体带盘符的路径，getName()得到的是文件的名称，两者字符串拼接就是具体的目标路径（目标文件//路径）
                        if(oldFile.exists()) {
                                FileInputStream fps = new FileInputStream(srcOldDir);
                                FileOutputStream fos = new FileOutputStream(file);
                                byte[] buffer = new byte[1024];
                                int lengh;
                                while ((byteread = fps.read(buffer)) != -1) {
                                        bytesum += byteread;
//                                        System.out.println(bytesum);
                                        fos.write(buffer, 0, byteread);
                                }
                                fos.flush();   
                                fos.close();
                                fps.close();
                                logger.info(">>>>>>>>>>>>>::文件已复制完成！----------------");
                        }
                }catch (Exception e){
                        logger.info(">>>>>>>>>>>>>::文件复制出错！----------------");
                        e.printStackTrace();
                }
        }
}

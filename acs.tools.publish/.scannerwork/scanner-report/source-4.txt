package acs.tools.publish.functest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToOne {
        
        /**
         * 合并两个excel表格,只选第一个sheet
         * 
         * @params majorFilePath 源文件名
         * @params secondFilePath 目标文件名
         */
        
        
        @SuppressWarnings("unused")
        public static void ExcelMerges(String majorFilePath, String secondFilePath){
                
                
                FileInputStream ipt = null;
                HSSFWorkbook workbook_m = null;
                HSSFWorkbook workbook_s = null;
                FileOutputStream opt = null;
                
                try {
                        ipt = new FileInputStream(secondFilePath);
                        workbook_s = new HSSFWorkbook(ipt);
                        HSSFSheet sheet_s = workbook_s.getSheetAt(0);
                        HSSFRow row = null;
                        HSSFCell cell = null;
                        
                        int rows = sheet_s.getLastRowNum()+1;
                        int cells = sheet_s.getRow(0).getPhysicalNumberOfCells();
                        System.out.println("行数为："+rows--+"列数为："+cells);
                        for (int i = 0; i < rows; i++){
                                row = sheet_s.getRow(i);
                                for (int j =0; j < cells; j++){
                                        cell = row.getCell(j);
//                                        System.out.println(getCellValue(cell));
                                }

                        }
                        
                        if (majorFilePath.endsWith(majorFilePath)){
                                ipt = new FileInputStream(majorFilePath);
                                workbook_m = new HSSFWorkbook(ipt);
                        }else {
                                Workbook workbook_m1 = new XSSFWorkbook();
                        }
                        HSSFSheet sheet_m = workbook_m.getSheetAt(0);
                        int rows_m = sheet_m.getLastRowNum();
                        int cells_m = sheet_m.getRow(0).getPhysicalNumberOfCells();
                        System.out.println("行数为："+rows_m--+"列数为："+cells_m);
                        HSSFRow row_m = sheet_m.getRow(0);
                }catch (Exception e) {
                        e.printStackTrace();
                }
                
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
	

        public static void main(String[] args){
                
                ExcelMerges("C:\\Users\\hui\\Desktop\\测试1.xls", "C:\\Users\\hui\\Desktop\\需求.xls");
        }
	

}

package utils;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class LogConfig {
        
        public static void impLog(String fileName){
                
                String logFilePath = "./result/"+fileName+".log";
                File file = new File(logFilePath);
                try{
                        if(file.exists()){
                                file.delete();
                                file.createNewFile();
                        }                        
                }catch (Exception e){
                        e.printStackTrace();
                        
                }
//                System.out.println(logFilePath);
                Properties  prop = new Properties();
                prop.setProperty("log4j.rootLogger", "info,toConsole,toFile");
                prop.setProperty("log4j.appender.file.enconding", "UTF-8");
                prop.setProperty("log4j.appender.toConsole", "org.apache.log4j.ConsoleAppender");
                prop.setProperty("log4j.appender.toConsole.Target", "System.out");
                 prop.setProperty("log4j.appender.toConsole.layout", "org.apache.log4j.PatternLayout ");
                // 设置日志输出格式 参考http://www.cnblogs.com/angleBlue/p/4769234.html
                prop.setProperty("log4j.appender.toConsole.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%c{1}][行:%L] %m%n");
                prop.setProperty("log4j.appender.toFile", "org.apache.log4j.DailyRollingFileAppender");
                prop.setProperty("log4j.appender.toFile.file", logFilePath);
                prop.setProperty("log4j.appender.toFile.append", "true");
                prop.setProperty("log4j.appender.toFile.Threshold", "info");
                prop.setProperty("log4j.appender.toFile.layout", "org.apache.log4j.PatternLayout");
                prop.setProperty("log4j.appender.toFile.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%c{1}][行:%L] %m%n");
                
                PropertyConfigurator.configure(prop);
        }

}

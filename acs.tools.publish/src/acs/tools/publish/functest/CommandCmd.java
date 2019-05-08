package acs.tools.publish.functest;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import Config.Config;
import myutils.common.DateUtils;
import utils.EmailUtils;
import utils.EmailUtils.EmailModel;
import utils.ZipUtils;


public class CommandCmd extends MCalss{
        
	private static final Logger logger = Logger.getLogger(CommandCmd.class);
        
	private static String FilePathName =  Config.NewPath+"/"+Config.FileName+".zip";
	
	private static String  PathName = Config.VerPath + " R & D/PRL/" +Config.version ;
	
	public static void exeCmd(String commandStr){
		
		BufferedReader br = null;
		try{
			Process p = Runtime.getRuntime().exec(commandStr);
			br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine())!= null){
				sb.append(line +"\n");
			}
			System.out.println(sb.toString());
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			if(br!= null){
				try{
					br.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static  void openDcoment(String  fileDcomentPath, Logger logger){
		
		Desktop ds = Desktop.getDesktop();
		try{
			ds.open(new File(fileDcomentPath));
		}catch(Exception e){
		        logger.info(">>>>>>>>>>>>>::文件夹打开失败！请检查。。。----------------");

		}
	}
	
	public static void openFile(String filePath, Logger logger){
		
		Runtime rt = Runtime.getRuntime();
		try{
			rt.exec("runadll32 url.dll FileProtocolHandler file://"+ filePath);
		}catch(Exception e){
		        logger.info(">>>>>>>>>>>>>::文件读取错误！请检查。。。----------------");
		}
	}
	
	
	public static void sendEmail(int i, Logger logger) throws IOException{
		
				int  sub = i ;				
				String commandStr =Config.NewPath+"/"+"into.bat";
				System.out.println(commandStr);
				String filePath = Config.NewPath; 
				CommandCmd.exeCmd(commandStr);
				CommandCmd.openDcoment(filePath, logger);
				
				
				ZipUtils.addFileToZip(Config.SourcePath, FilePathName, logger);
				
				if (new File(FilePathName).exists()){
					String date = DateUtils.getNowString("yyyy年MM月dd日");
					EmailModel model = new EmailUtils.EmailModel();
					model.userName = Config.username;
					model.from = Config.username;					
					model.to = Config.set_to;     // 多个联系人用分号隔开
					model.passWord = Config.password;
					model.smtpHost = Config.SMTPserver;
					model.smtpPort = "25";
					model.messageText =  "<p>各位领导: </p><br>本期版本组件个数为："+sub+"个</br><br>" +Config.version+"版本相关内容已上传到svn，请自行下载！</br><br>版本路径："+ PathName +"</br>"+
															"其中："+"<br>"+PathName +"/database    (含acs脚本oracle增量脚本)"+"</br>"+"<br>"+PathName+"/doc   （含发布申请、测试报告、更新说明、操作手册、配置文件、部署手册）</br>"+
															"<br>" + PathName+ "/exe   （包括acs模块的jar包）</br>"+"<br>"+PathName+"/report   （增量报表）</br>"+"<br>"+PathName+"/src   （发版组件源代码）</br>";
					model.subject = Config.subject + date;
					EmailUtils.sendMessage(model, FilePathName);
					logger.info(">>>>>>>>>>>>>::邮件已发送，请查收！----------------");
				}else{
				        logger.info(">>>>>>>>>>>>>::没有附件，不发送！----------------");
				}
	}

}

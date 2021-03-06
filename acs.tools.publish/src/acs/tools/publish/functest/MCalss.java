package acs.tools.publish.functest;



import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.ExcelScreen;
import utils.LogConfig;
import utils.createTxtFile;
import Config.Config;

public class MCalss {
        @SuppressWarnings("unused")
        private static final Logger logger = Logger.getLogger(MCalss.class);
        
        public MCalss(){
                LogConfig.impLog(MCalss.class.getSimpleName());
        }
        
        @SuppressWarnings("static-access")
        public static void main(String[] args) throws Exception {
        		
        		int s2 = 2;
        		System.out.println("hello world!");
        		System.out.println("看看会不会报乱码的错误了！！！！！终于好了，再试试看看！！！");
                int s1 = 0;
                String name = null;
                BasicConfigurator.configure();
                ChanDaoLogin cdl = new ChanDaoLogin();
                ExcelScreen els = new ExcelScreen();
                CommandCmd  ccd = new CommandCmd();
                createTxtFile ctf = new createTxtFile();
                ExampleExcel rw = new ExampleExcel();
                
                                                            
             //获取禅道中的bug、需求
              cdl.ChanDaoLogin();
               
          //更换文件路劲
              els.copyFile(Config.BugList+".csv", logger);
              els.copyFile(Config.DeMandList+".csv",logger);
             
          //更换文件格式
              els.CsvToXls(Config.BugList+".csv", "Bug列表1.xls",logger);
              els.CsvToXls(Config.DeMandList+".csv", "需求列表1.xls",logger);
                    
          //筛选
             String[] BCells = {"Bug编号","优先级","客户名称","Bug标题","所属Build","指派给","解决者","由谁测试","集成人员","配置模板","Bug状态","提出日期","发布日期","预计发布日期"};
             String[] FCells = {"提交日期","属性","所属客户","BUG/需求编号","BUG、需求名称","影响功能点","功能变更描述","是否影响公共功能","模块","提交人","备注","组件名称"};
             String[] users = {"广发银行","广发证券","华夏银行","民生银行","南京银行","宁波银行","银河证券","中国农业银行","兴业银行","渣打银行","中国光大银行","中信银行","中债理财中心"};
             String[] Busers = {"已发布","已关闭","已测试"};
             String[] Xusers = {"需求"};
             String[] Wusers = {"外部bug"};
//             String[] TCells = {"编号","优先级","客户名称","标题","所属Build","指派给","分析人员","开发人员","测试人员","集成人员","配置模板","预计工时","当前状态","所处阶段","提出日期","发布日期","期望日期","预计发布日期"};
                                    
             els.screen("Bug列表1.xls", Config.SourcePath+"/"+"Bug列表_客户.xls", BCells , "客户名称", users, logger);                                     //去掉Bug列表中的内部bug
             ctf.sleep(1);
             els.screen(Config.FBFileName, Config.SourcePath+"/"+"发布申请_客户.xls", FCells , "所属客户", users, logger );                              //去掉发布清单中的内部Bug
             els.screen(Config.FBFileName, Config.SourcePath+"/"+"发布申请_需求.xls", FCells , "属性", Xusers, logger );                                       //去掉发布清单中的外部Bug
             ctf.sleep(1);
             els.screen("Bug列表_客户.xls", Config.SourcePath+"/"+"Bug列表_Bug.xls", BCells , "Bug状态", Busers, logger);                                   //去掉Bug列表中的退回中、已退回、已解决、激活、解决中            
             els.screen("发布申请_客户.xls", Config.SourcePath+"/"+"发布申请_外部.xls", FCells , "属性", Wusers, logger );                                   //去掉发布清单中的需求
             ctf.sleep(1);
             
             //获取src文件夹下的文件夹名称
//             ctf.createFile("组件文件夹名", logger);
//             ctf.getFiles("H:/test/project/branches/Control/dev/Project R & D/PRL/ACS_5.0.3_DEV_20180731001.alpha/src");
//             
//             //获取exe文件夹下的文件名称   
//             ctf.createFile("jar文件", logger);
//             ctf.getFileName("H:/test/project/branches/Control/dev/Project R & D/PRL/ACS_5.0.3_DEV_20180731001.alpha/exe/acs");
                            
            //发布申请与bug、需求比较
             rw.CompareExcel(Config.SourcePath+"/"+"发布申请_外部.xls", Config.SourcePath+"/"+"Bug列表_Bug.xls", logger);                                              //Bug列表与发布清单比较
             rw.CompareExcel(Config.SourcePath+"/"+"发布申请_需求.xls", Config.SourcePath+"/"+"需求列表1.xls", logger);                                                    //需求与发布清单比较
           //获取组件、版本号  
             name = ctf.getFileName(Config.SourcePath);
             rw.componentName(Config.SourcePath+"/"+name, 10, logger); 
             int compName_z = rw.componentName(Config.SourcePath+"/"+name, 11, logger); 
//             rw.componentName(Config.SourcePath+"/"+"发布申请_主干_20180928.xls", 10, logger);                                                                                                           //获取版本号                                                                                                      
//             int compName_z = rw.componentName(Config.SourcePath+"/"+"发布申请_主干_20180928.xls", 11, logger);                                                                 //获取组件名称并去重
             s1 = compName_z;                                                                                                                                                                                                                                                      //获取组件数目
               
            //邮件发送
             ccd.sendEmail(s1, logger);
                

              
//              List list = ctf.readerFileT("jar文件", logger);
//              List list1 = ctf.readerFileT("result01", logger);
//              ctf.comSameFile("H:/test/SVNList版本号.txt", "H:/test/SVNList版本号001.txt", logger);
//              ccd.sendEmail(s1, logger);
               
        }

}

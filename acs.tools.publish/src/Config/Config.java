package Config;

import org.apache.log4j.Logger;

import acs.tools.publish.functest.MCalss;
import utils.XmlParser;

public class Config {
        	
	public static String Url;
	public static String pmUrl;
	public static String pusername;
	public static String ppassword;
	public static String	ProductPlan;
	public static String SMTPserver;
	public static String  from;
	public static String username;
	public static String password;
	public static String set_to;
	public static String copyto;
	public static String filename;
	public static String subject;
	public static String VerPath;
	public static String version;
	public static String FileName;
	public static String Users;
	public static String Downloads;
	public static String SourcePath;
	public static String NewPath;
	public static String FBFileName;
	public static String SVNFilePath;
	public static String BugList;
	public static String DeMandList;
	
	static{
		XmlParser webxp = new XmlParser("D:\\config.xml");
		pmUrl = webxp.getElementText("config/pmurl");
		pusername = webxp.getElementText("config/pusername");
		ppassword = webxp.getElementText("config/ppassword");
		FileName = webxp.getElementText("config/fileName");
		ProductPlan = webxp.getElementText("config/productplan");
		SMTPserver = webxp.getElementText("/config/SMTPserver");
		from = webxp.getElementText("/config/from");
		username = webxp.getElementText("/config/username");
		password = webxp.getElementText("/config/password");
		set_to = webxp.getElementText("/config/to");
		copyto = webxp.getElementText("/config/copyto");
		filename = webxp.getElementText("/config/filename");
		subject = webxp.getElementText("/config/subject");
		VerPath = webxp.getElementText("/config/verPath");
		version = webxp.getElementText("/config/version");
		Users = webxp.getElementText("/config/users");
		Downloads = webxp.getElementText("/config/downloads");
		SourcePath = webxp.getElementText("/config/sourcePath");
		NewPath = webxp.getElementText("/config/newPath");
		FBFileName = webxp.getElementText("/config/fbFileName");
		SVNFilePath =webxp.getElementText("/confog/svnFilePath");
		BugList = webxp.getElementText("config/bugList");
		DeMandList = webxp.getElementText("/config/demandList");
	}

}

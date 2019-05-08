package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;



public class EmailUtils {
	
	private static Logger log = Logger.getLogger(EmailUtils.class);

	  public static void main(String[] args)
	  {
		  EmailModel model = new EmailModel();

	    model.userName = "chendaiwu@ysstech.com";
	    model.from = "chendaiwu@ysstech.com";
	    model.to = "916463362@qq.com";
	    model.passWord = "cdw520DW0614";
	    model.smtpHost = "smtp.ysstech.com";
	    model.smtpPort = "25";
	    model.messageText = "智能发布系统！";
	    model.subject = "智能发布";

	    sendMessage(model, "d:/0000.txt");
	  }

	  public static void sendMessage(EmailModel model)
	  {
	    Properties smtpProper = setSmtp(model.smtpHost, model.smtpPort, model.userName, model.passWord);
//	    Auth authenticator = new Auth(model.userName, model.passWord);
	    try
	    {
//	      Session session = Session.getInstance(smtpProper, authenticator);
	      Session session = Session.getInstance(smtpProper);
	      MimeMessage message = new MimeMessage(session);
	      session.setDebug(true);

	      message.setFrom(new InternetAddress(model.from));

	      ArrayList toList = new ArrayList();

	      String[] toArray = model.to.split(";");
	      for (int i = 0; i < toArray.length; ++i) {
	        String str = toArray[i];
	        toList.add(new InternetAddress(str));
	      }

	      Address[] addresses = new Address[toList.size()];
	      addresses = (Address[])toList.toArray(addresses);

	      message.setRecipients(MimeMessage.RecipientType.TO, addresses);

	      message.setSubject(model.subject);

	      message.setContent(model.messageText, "text/html;charset=UTF-8");

	      Transport transport = session.getTransport();
	      transport.connect(model.smtpHost, model.userName, model.passWord);
	      transport.sendMessage(message, addresses);
	      transport.close();
	      log.info("发送邮件成功！");
	    }
	    catch (MessagingException e)
	    {
	      log.error("发送邮件失败！");
	      e.printStackTrace();
	    }
	  }

	  public static void sendMessage(EmailModel model, String fileName)
	  {
	    Properties props = setSmtp(model.smtpHost, model.smtpPort, model.userName, model.passWord);
	    Session mailSession = Session.getDefaultInstance(props);

	    mailSession.setDebug(false);

	    System.out.println("Constructing message - from=" + model.from + " to=" + model.to);
	    try {
	      InternetAddress fromAddress = new InternetAddress(model.from);
	      MimeMessage mimeMessage = new MimeMessage(mailSession);

	      String[] toArr = model.to.split(";");
	      if (toArr.length > 1) {
	        String[] arrayOfString1;
	        int j = (arrayOfString1 = toArr).length; for (int i = 0; i < j; ++i) { String to = arrayOfString1[i];
	          mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	        }
	      } else {
	        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(model.to));
	      }

	      mimeMessage.setFrom(fromAddress);

	      mimeMessage.setSentDate(new Date());
	      mimeMessage.setSubject(model.subject);

	      System.out.println("Constructing 'text' body part");

	      MimeBodyPart textBodyPart = new MimeBodyPart();
	      textBodyPart.setContent(model.messageText, "text/html;charset=gb2312");

	      System.out.println("Attaching 'file' body part: " + fileName);

	      MimeBodyPart fileBodyPart = new MimeBodyPart();
	      FileDataSource fileDataSource = new FileDataSource(fileName);
	      fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
	      fileBodyPart.setFileName(fileDataSource.getName());

	      System.out.println("Finished attaching file");

	      Object container = new MimeMultipart();
	      ((Multipart)container).addBodyPart(textBodyPart);
	      ((Multipart)container).addBodyPart(fileBodyPart);

	      mimeMessage.setContent((Multipart)container);

	      System.out.println("Message constructed");

	      Transport transport = mailSession.getTransport("smtp");

	      transport.connect(model.smtpHost, model.userName, model.passWord);
	      transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
	      transport.close();
	    } catch (MessagingException e) {
	      log.error(model.subject + "发送邮件失败！");
	      e.printStackTrace();
	    }
	    log.info(model.subject + " 邮件发送成功!");
	  }

	  private static Properties setSmtp(String smtpHost, String smtpPort, String userName, String passWord)
	  {
	    Properties maiProperties = new Properties();

	    maiProperties.put("mail.transport.protocol", "smtp");

	    maiProperties.put("mail.smtp.host", smtpHost);

	    maiProperties.put("mail.smtp.port", smtpPort);

	    maiProperties.put("mail.user", userName);

	    maiProperties.put("mail.smtp.auth", "true");
	    return maiProperties;
	  }

	  public static class EmailModel
	  {
	    public String userName;
	    public String passWord;
	    public String smtpHost;
	    public String smtpPort;
	    public String from;
	    public String to;
	    public String subject;
	    public String messageText = "";
	  }

}

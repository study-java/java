package utils;

import java.lang.reflect.Field;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmailTo {
	
	public static String SMTP = "smtp";
	public static String SUBJECT = "subject";
	public static String FROM = "from";
	public static String SENDER_NAME = "sendername";
	public static String SET_TO ="to";
	public static String COPY_TO = "copyto";
	public static String USER_NAME ="username";
	public static String PASSWORD = "password";
	public static String FILE_PATH ="file_path";
	public static String smtpPort = "25";
	
	private MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	private Multipart mp;
	private String smtp = null;
	private String subject = null;
	private String from = null;
	private String sendername = null;
	private String to = null;
	private String copyTo = null;
	private String username = null;
	private String password = null;
	private String filepath = null;
	
	
	public SendEmailTo(Parameter parameter){
		
					init(parameter);
				
	}
	
	public boolean send(String text, String encoding){
		
					boolean needCopyTo = true;
					setSmtpHost();
					createMimeMessage();
					setNeedAuthor(true);
					if (!setSubject()) return false;
					if (!setBody(text, encoding)) return false;
					
					if (!(filepath == null || filepath.equals(" "))){
								if (!addFilePath()) return false;
					}
					
					if (!setTo()) return false;
					
					if (copyTo == null || copyTo.equals(" ")){
								needCopyTo = false;
					} else {
									if (!setCopyTo()) return false;
					}
					
					if (setFrom()) return false;
					
					if (!sendOut(needCopyTo)) return false;
					
					return true;
	}
	
	protected void init(Parameter parameter){
					String[] argNames = {"smtp", "from", "to", "username", "password"};
					for (String name : argNames){
								String value = parameter.get(name);
								if(value == null || value.equals(" ")){
										System.out.println("缺少发邮件发送的必要参数：" + name);
								}
								
					}
					
					for(String name : parameter.asMap().keySet()){
								String value  = parameter.get(name);
								if (value != null || value.equals(" ")){
										try{
												Field field = this.getClass().getDeclaredField(name);
												field.set(this,value);
										}catch(NoSuchFieldException e){
												System.out.println("SendEmailTo不存在如下参数：" + name );
										}catch (SecurityException e) {
												System.out.println("初始化SendEmailTo参数异常：" + name );
										} catch (IllegalArgumentException e) {
												System.out.println("SendEmailTo以下参数，参数类型不正确：" + name );
										} catch (IllegalAccessException e) {
												System.out.println("初始化SendEmailTo参数异常：" + name );
										}
								}
					}				
	}
	
	
	protected void setSmtpHost(){
					
					if(props ==null){
						props = System.getProperties();
						props.put("mail.smtp.host", smtp);
					}
	
	}
	
	protected boolean createMimeMessage(){
					
					try{
								session = Session.getDefaultInstance(props,null);
					}catch(Exception e){
								System.out.println("获取邮件扫面对象是发生错误！");
								return false;
					}
					try{
								mimeMsg = new MimeMessage(session);
								mp = new MimeMultipart("related");
								return true;
					}catch(Exception e){
								System.out.println("创建MIME邮件对象失败！");
								return false;
					}
	}
	
	
	protected void setNeedAuthor(boolean need){
					if (props == null)props = System.getProperties();
					if(need){
									props.put("mail.smtp.auth", true);
					}else{
									props.put("mail.smtp.auth", false);
					}
	}
	
	protected boolean setSubject(){
		
					try{
							if(subject != null || subject.equals(" ")){
									mimeMsg.setSubject(subject);
							}else mimeMsg.setSubject(" ");
							return true;
					}catch(Exception e){
							System.out.println("设置邮件主题发生错误！");
							return false;
					}
	}
	
	
	protected  boolean setBody(String text, String encoding){
		
					try{
							MimeBodyPart textBody = new MimeBodyPart();
							textBody.setContent(text, "text/htmllcharset = gb2312");
							mp.addBodyPart(textBody);
							return true;
					} catch (Exception e){
							System.out.println("邮件设置正文是发生错误！");
							return false;
					}
	}
	
	protected boolean addFilePath(){
		
					try{
							BodyPart bodyt = new MimeBodyPart();
							FileDataSource fileds = new FileDataSource(filepath);
							bodyt.setDataHandler(new DataHandler(fileds));
							bodyt.setFileName(fileds.getName());
							mp.addBodyPart(bodyt);
							return true;
					}catch (Exception e){
							System.out.println("添加附件时：" + filepath + "发生错误!");
							return false;	
					}
	}
	
	
	protected boolean setFrom(){
					
					try{
							if(sendername == null ||sendername.equals(" ")){
										mimeMsg.setFrom(new InternetAddress(from));
							}else  mimeMsg.setFrom(new InternetAddress(from, sendername));
							return true;
					}catch (Exception e) {
							System.out.println("设置发信人时出错！");
							return false;
					}
	}
	
	
	protected boolean setTo(){
		
					try{
							mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
							return true;
					} catch (Exception e ) {
							System.out.println("设置收信人时出错！");
							return false;
					}
	}
	
	
	protected boolean setCopyTo(){
		
					try{
							mimeMsg.setRecipients(Message.RecipientType.CC, (Address[])InternetAddress.parse(copyTo));
							return true;
					} catch (Exception e) {
							System.out.println("设置抄送人时出错！");
							return false;
					}
	}

	
	protected boolean sendOut(boolean needeCopyTo){
					
					try{
							mimeMsg.setContent(mp);
							mimeMsg.saveChanges();
							Session mailSession = Session.getInstance(props, null);
							Transport transport = mailSession.getTransport("smtp");
							transport.connect((String)props.get("mail.smtp.host"),username,password);
							transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
							if (needeCopyTo) {
										transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
							}
							transport.close();
							return true;
					} catch (Exception e) {
							System.out.println("邮件发送失败！");
							return false;
					}
	}
	
}

package acs.tools.publish.functest;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SvnLogin {
	
	public static  void main(String[] args) throws Exception{
		
		String chromeDriver = "res/chromedriver.exe";
		System.setProperty("webDriver.chrome.driver", chromeDriver);
		WebDriver  driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
//		driver.manage().window().maximize();
		driver.navigate().to("http://192.168.99.60:18080/svn/project project --username=admin&pass=123456");
		driver.switchTo().frame(0);
	

		Thread.sleep(5);
 		
		
		
	}

}

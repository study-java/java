package acs.tools.publish.functest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import myutils.common.Tools;
import utils.ExcelScreen;
import utils.createTxtFile;

import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.Dimension;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.ActionChainExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import Config.Config;

public class ChanDaoLogin extends MCalss{
        
        private static final Logger logger = Logger.getLogger(ChanDaoLogin.class);
        
	@SuppressWarnings("static-access")
        public static void ChanDaoLogin() throws Exception {
	        
	        CommandCmd  ccd = new CommandCmd();
	        createTxtFile ctf = new createTxtFile();
                ExampleExcel rw = new ExampleExcel();
	        ExcelScreen els = new ExcelScreen();
	        BasicConfigurator.configure();
		String chromdriver = "res/chromedriver.exe"; 
		System.setProperty("webdriver.chrome.driver", chromdriver);
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
//		driver.navigate().to("http://pm.ysstech.com:8080/index.php?m=user&f=login");
		System.out.println(Config.pmUrl);
		driver.navigate().to(Config.pmUrl);
		logger.info(">>>>>>>>>>>>>::进入"+Config.pmUrl+"！----------------");
		getElementByXpath(driver, ".//*[@id='account']").sendKeys(Config.pusername);
		getElementByXpath(driver, ".//input[@name='password']").sendKeys(Config.ppassword);
//		getElementByXpath(driver, ".//*[@id='account']").sendKeys("chendaiwu");
//		getElementByXpath(driver, ".//input[@name='password']").sendKeys("951753cdw");
		getElementByXpath(driver, ".//*[@id='loginPlace']").click();
		getElementByXpath(driver, ".//option[text()='北京办公区']").click();
		getElementByXpath(driver, ".//*[@id='submit']").submit();
		logger.info(">>>>>>>>>>>>>::进入禅道系统！----------------");

		getElementByXpath(driver, ".//*[@id='searchType']").click();
		getElementByXpath(driver, ".//option[text()='P:产品计划']").click();
		WebElement searchTextEle = getElementByXpath(driver, ".//*[@id='searchQuery']");
		searchTextEle.clear();
		searchTextEle.sendKeys(Config.ProductPlan);
//		searchTextEle.sendKeys("11435");

		getElementByXpath(driver, ".//*[@id='objectSwitcher']").click();
		Tools.sleep(3);
		String curHandle = driver.getWindowHandle();
		Set<String> winHandles = driver.getWindowHandles(); // 得到当前窗口的set集合
		for (String s : winHandles) {
			if (s.equals(curHandle))
				continue;
			else {
				driver.switchTo().window(s);
			}
		}
		
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		Tools.sleep(5);
		JavascriptExecutor js = (JavascriptExecutor)driver;  //滑动滚动条
		js.executeScript("scrollTo(0,1200)");
		Tools.sleep(2);

		getElementByXpath(driver, ".//a[text()='导出Bug列表']").click();
		WebElement iframeEle = getElementByXpath(driver,
				".//iframe[contains(@src,'m=productplan&f=export&productID=')]");

		driver.switchTo().frame(iframeEle);
		Tools.sleep(2);
		getElementByXpath(driver, ".//*[@id='fileName']").sendKeys(Config.BugList);
		getElementByXpath(driver, ".//*[@id='encode']").click();
		getElementByXpath(driver, ".//option[text()='GBK']").click();
		getElementByXpath(driver, ".//*[@id='submit']").submit();
		driver.switchTo().defaultContent();
		Tools.sleep(1);
		logger.info(">>>>>>>>>>>>>::Bug列表成功导出->"+Config.Downloads+"/"+Config.BugList+".csv  ！----------------");
		
		waitForElementDisappear(driver, ".//iframe[contains(@src,'m=productplan&f=export&productID=')]", 30);
		Tools.sleep(5);
		
		getElementByXpath(driver, ".//a[text()='导出需求列表']").click();
		Tools.sleep(1);
		iframeEle = getElementByXpath(driver,
				".//iframe[contains(@src,'m=productplan&f=export&productID=')]");
		driver.switchTo().frame(iframeEle);
		Tools.sleep(2);
		getElementByXpath(driver, ".//*[@id='fileName']").sendKeys(Config.DeMandList);
		getElementByXpath(driver, ".//*[@id='encode']").click();
		getElementByXpath(driver, ".//option[text()='GBK']").click();
		getElementByXpath(driver, ".//*[@id='submit']").submit();
		driver.switchTo().defaultContent();
		Tools.sleep(1);
		logger.info(">>>>>>>>>>>>>::需求列表成功导出->"+Config.Downloads+"/"+Config.DeMandList+"csv ！----------------");
		waitForElementDisappear(driver, ".//iframe[contains(@src,'m=productplan&f=export&productID=')]", 30);
		Tools.sleep(10);
		
		driver.close();
		driver.quit();
		logger.info(">>>>>>>>>>>>>::退出禅道系统！----------------");		                            		
	}

	public static WebElement getElementByXpath(WebDriver driver, String xpath) {
		WebElement ele = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath(xpath)));
		return ele;
	}

	public static boolean waitForElementDisappear(WebDriver driver, String xpath, int timeout) {
		return new WebDriverWait(driver, timeout)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
	}
}

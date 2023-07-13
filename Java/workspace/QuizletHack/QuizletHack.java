package io.github.danthe1st.quizlet.hack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class QuizletHack {
	public static void main(String[] args) throws InterruptedException, IOException {
		
		Properties config=new Properties();
		config.load(new FileInputStream("config.properties"));
		
		RemoteWebDriver driver;
		//driver=new ChromeDriver();
		driver=new FirefoxDriver();
		driver.get("https://quizlet.com");
		driver.findElementByClassName("SiteHeader-signInBtn").click();
		driver.findElementByName("username").sendKeys(config.getProperty("user"));
		driver.findElementByName("password").sendKeys(config.getProperty("pwd"));
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='UIModalBody']//button//span[contains(text(), 'Anmelden')]")).click();
		do {
			Thread.sleep(1000);
			driver.get(config.getProperty("link"));
			driver.findElementByXPath("//button//span[text()='Spiel beginnen']").click();
			Thread.sleep(300);
			List<WebElement> wordElems = driver.findElementsByClassName("MatchModeQuestionScatterTile");
			Iterator<WebElement> wordIter = wordElems.iterator();
			while(wordIter.hasNext()) {
				WebElement src = wordIter.next();
				WebElement dest = wordIter.next();
				//System.out.println(src.getText()+"-->"+dest.getText());
				new Actions(driver).dragAndDrop(src,dest).build().perform();
			}
		}while(true);
	}
}

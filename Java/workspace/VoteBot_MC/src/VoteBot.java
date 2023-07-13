import java.io.FileInputStream;
import java.io.IOException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;



public class VoteBot {
	public static void main(String[] args) throws InterruptedException, IOException {
		
		//Properties config=new Properties();
		//config.load(new FileInputStream("config.properties"));
		
		RemoteWebDriver driver;
		driver=new FirefoxDriver();

		do {
			driver.get("https://www.mcdonalds.at/meinburger/?id=14265");
			driver.findElementByClassName("cc-btn cc-deny").click();
			Thread.sleep(300);
			driver.findElementById("detailview_btn_vote").click();
			Thread.sleep(300);
			driver.get("about:preferences#privacy");
			driver.findElementById("clearSiteDataButton").click();
			driver.findElementById("clearButton").click();
			Thread.sleep(300);
		}while(true);
	}
}

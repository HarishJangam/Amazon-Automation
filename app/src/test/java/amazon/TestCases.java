package amazon;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    RemoteWebDriver driver=null;

    @BeforeClass
    public void createDriver(){
        System.out.println("this is starting");
        WebDriverManager.chromedriver().setup();
        driver=new ChromeDriver();
        System.out.println("driver "+ driver);
        driver.get("https://www.google.com/");
        driver.manage().window().maximize();
    }

    @Test
    public void testCases() throws InterruptedException{
        HashMap<String,String>map=new HashMap<>();
        HashMap<Integer,String>result=new HashMap<>();
        System.out.println("testcase started ");
        driver.get("https://www.amazon.in/");
        WebElement searchBar=driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
        searchBar.sendKeys("lg soundbar");
        driver.findElement(By.xpath("//input[@id='nav-search-submit-button']")).click();
        Thread.sleep(2000);
        List<WebElement> products=driver.findElements(By.xpath("//div[@class='puisg-col-inner']//div[@class='a-section a-spacing-small a-spacing-top-small']"));
        System.out.println("product size "+ products.size());
        for(WebElement we:products){
            String priceTag=null;
            String productName=we.findElement(By.xpath(".//*[@class='a-size-medium a-color-base a-text-normal']")).getText();
            try{
                WebElement priceElement=we.findElement(By.xpath(".//*[@class='a-price-whole']"));
                priceTag=priceElement.getText();
            }
            catch(Exception e){
                priceTag="null";
            }
           
            map.put(priceTag,productName);
        
        }
        System.out.println("before sorting by price (Low to High):");
        for (Map.Entry<String, String> set : map.entrySet()) {
            String Key=set.getKey();
            Integer price=0;
            if(!Key.equals("null")){
            String priceText = Key.replace(",", "");
            price=Integer.parseInt(priceText);
            }
            String value=set.getValue().substring(0,20);
            
            result.put(price, value);
        }
        List<Map.Entry<Integer, String>> sortedProducts = new ArrayList<>(result.entrySet());
        sortedProducts.sort(Map.Entry.comparingByKey());
        
        // Print product prices and names in ascending order
        System.out.println("Products sorted by price (Low to High):");
        for (Map.Entry<Integer, String> entry : sortedProducts) {
            System.out.println("Price: " + entry.getKey() + " | Product: " + entry.getValue());
        }

    }

    @AfterClass
    public void closeDriver(){
        driver.quit();
    }

}

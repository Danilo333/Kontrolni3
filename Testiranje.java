package testiranije;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import objects.Adrese;

public class Testiranje {

	private static WebDriver driver;

	@BeforeClass
	public void createDriver() {
		System.setProperty("webdriver.chrome.driver", "C:\\Chromedriver1\\ChromeDriver.exe");
		driver = new ChromeDriver();

	}

	@Test(priority = 2)
	public void losUnos() {

		File f = new File("data.xlsx");
		try {
			InputStream inp = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			Sheet sheet = wb.getSheetAt(0);

			Row prvi = sheet.getRow(0);

			String losUser = prvi.getCell(0).toString();
			String losPass = prvi.getCell(1).toString();

			driver.get(Adrese.URL);
			driver.findElement(By.xpath(Adrese.USERNAME_XPATH)).sendKeys(losUser);
			driver.findElement(By.id(Adrese.PASSWORD_ID)).sendKeys(losPass);
			driver.findElement(By.id(Adrese.LOGIN_ID)).click();

			String actual = driver.getCurrentUrl();
			String expected = "https://www.saucedemo.com/";
			Assert.assertEquals(actual, expected);

		} catch (IOException e) {
			System.out.println("Nije pronadjen fajl!");
			e.printStackTrace();
		}
	}

	@Test(priority = 1)
	public void dobarUnos() {
		File f = new File("data.xlsx");
		try {
			InputStream inp = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			Sheet sheet = wb.getSheetAt(0);

			SoftAssert sa = new SoftAssert();

			for (int i = 1; i < 4; i++) {
				Row drugi = sheet.getRow(i);

				String dobarUser = drugi.getCell(0).toString();
				String dobarPass = drugi.getCell(1).toString();
				driver.get(Adrese.URL);
				driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys(dobarUser);
				driver.findElement(By.id(Adrese.PASSWORD_ID)).sendKeys(dobarPass);
				driver.findElement(By.id(Adrese.LOGIN_ID)).click();

				String actual = driver.getCurrentUrl();
				String expected = "https://www.saucedemo.com/inventory.html";
				sa.assertEquals(actual, expected);

			}

			sa.assertAll();

		} catch (IOException e) {
			System.out.println("Nije pronadjen fajl!");
			e.printStackTrace();
		}

	}

	@Test(priority = 3)
	public void dropDownTest() {
		driver.get(Adrese.INVERNTODY_URL);

		WebElement dropdown = driver.findElement(By.xpath(Adrese.DROPDOWN_XPATH));
		dropdown.sendKeys("price");
		Select se = new Select(dropdown);
		List<String> originalList = new ArrayList();
		for (WebElement e : se.getOptions()) {
			originalList.add(e.getText());
		}

		List<String> tempList = originalList;
		Collections.sort(tempList);
		Assert.assertEquals(tempList, originalList);
	}

}
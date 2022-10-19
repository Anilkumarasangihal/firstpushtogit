package com.MyStore.testcases;


	import org.testng.Assert;
	import org.testng.annotations.AfterMethod;
	import org.testng.annotations.BeforeMethod;
	import org.testng.annotations.Parameters;
	import org.testng.annotations.Test;

import com.MyStore.base.BaseClass;
import com.MyStore.dataprovider.DataProviders;
import com.MyStore.pageobjects.AddToCartPage;
import com.MyStore.pageobjects.IndexPage;
import com.MyStore.pageobjects.OrderPage;
import com.MyStore.pageobjects.SearchResultPage;
import com.MyStore.utility.Log;



	public class OrderPageTest extends BaseClass {

		private IndexPage index;
		private SearchResultPage searchResultPage;
		private AddToCartPage addToCartPage;
		private OrderPage orderPage;

		@Parameters("browser")
		@BeforeMethod(groups = {"Smoke","Sanity","Regression"})
		public void setup(String browser) {
			launchApp(browser); 
		}
		
		@AfterMethod(groups = {"Smoke","Sanity","Regression"})
		public void tearDown() {
			getDriver().quit();
		}
		
		@Test(groups = "Regression",dataProvider = "getProduct", dataProviderClass = DataProviders.class)
		public void verifyTotalPrice(String productName, String qty, String size) throws Throwable {
			Log.startTestCase("verifyTotalPrice");
			index= new IndexPage();
			searchResultPage=index.searchProduct(productName);
			addToCartPage=searchResultPage.clickOnProduct();
			addToCartPage.enterQuantity(qty);
			addToCartPage.selectSize(size);
			addToCartPage.clickOnAddToCart();
			orderPage=addToCartPage.clickOnCheckOut();
			Double unitPrice=orderPage.getUnitPrice();
			Double totalPrice=orderPage.getTotalPrice();
			Double totalExpectedPrice=(unitPrice*(Double.parseDouble(qty)))+2;
			Assert.assertEquals(totalPrice, totalExpectedPrice);
			Log.endTestCase("verifyTotalPrice");
		}
}

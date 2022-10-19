package com.MyStore.testcases;

	import org.testng.Assert;
	import org.testng.annotations.AfterMethod;
	import org.testng.annotations.BeforeMethod;
	import org.testng.annotations.Parameters;
	import org.testng.annotations.Test;

    import com.MyStore.base.BaseClass;
import com.MyStore.dataprovider.DataProviders;
import com.MyStore.pageobjects.AddToCartPage;
import com.MyStore.pageobjects.AddressPage;
import com.MyStore.pageobjects.IndexPage;
import com.MyStore.pageobjects.LoginPage;
import com.MyStore.pageobjects.OrderConfirmationPage;
import com.MyStore.pageobjects.OrderPage;
import com.MyStore.pageobjects.OrderSummary;
import com.MyStore.pageobjects.PaymentPage;
import com.MyStore.pageobjects.SearchResultPage;

import com.MyStore.pageobjects.ShippingPage;
import com.MyStore.utility.Log;

	
	public class EndToEndTest extends BaseClass {
		
		private IndexPage index;
		private SearchResultPage searchResultPage;
		private AddToCartPage addToCartPage;
		private OrderPage orderPage;
		private LoginPage loginPage;
		private AddressPage addressPage;
		private ShippingPage shippingPage;
		private PaymentPage paymentPage;
		private OrderSummary orderSummary;
		private OrderConfirmationPage orderConfirmationPage;

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
		public void endToEndTest(String productName, String qty, String size) throws Throwable {
			Log.startTestCase("endToEndTest");
			index= new IndexPage();
			searchResultPage=index.searchProduct(productName);
			addToCartPage=searchResultPage.clickOnProduct();
			addToCartPage.enterQuantity(qty);
			addToCartPage.selectSize(size);
			addToCartPage.clickOnAddToCart();
			orderPage=addToCartPage.clickOnCheckOut();
			loginPage=orderPage.clickOnCheckOut();
			addressPage=loginPage.login(prop.getProperty("username"), prop.getProperty("password"),addressPage);
			shippingPage=addressPage.clickOnCheckOut();
			shippingPage.checkTheTerms();
			paymentPage=shippingPage.clickOnProceedToCheckOut();
			orderSummary=paymentPage.clickOnPaymentMethod();
			orderConfirmationPage=orderSummary.clickOnconfirmOrderBtn();
			String actualMessage=orderConfirmationPage.validateConfirmMessage();
			String expectedMsg="Your order on My Store is complete.";
			Assert.assertEquals(actualMessage, expectedMsg);
			Log.endTestCase("endToEndTest");
		}

}

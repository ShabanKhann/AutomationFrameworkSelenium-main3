/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.dataprovider.DataProviderManager;

import static anhtester.com.keywords.WebUI.*;
import static anhtester.com.keywords.WebUI.getURL;
//import static javax.swing.text.rtf.RTFAttributes.BooleanAttribute.True;

import anhtester.com.projects.website.crm.models.SignInModel;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.utils.DataGenerateUtils;
import anhtester.com.utils.JsonUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
//import jdk.internal.access.JavaIOFileDescriptorAccess;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Hashtable;

@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class SignInTest extends BaseTest {

    private JsonUtils driver;

    public SignInTest() {
        signInPage = new SignInPage();
    }

    //Using library DataProvider with read Hashtable
    @Test(priority = 1, description = "TC01_signInTestDataProvider", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void signInTestDataProvider(Hashtable<String, String> data) {
        signInPage.signIn(data);

    }

    @Test(priority = 2, description = "TC02_signInTestAdminRole")
    public void signInTestAdminRole() {
        signInPage.signInWithAdminRole();
        verifyElementPresent(getDashboardPage().menuDashboard, 5, "The menu Dashboard does not exist.");

    }

    @Test(priority = 3, description = "TC03_signInTestClientRole")
    public void signInTestClientRole() {
        signInPage.signInWithClientRole();
        verifyContains(getPageTitle(), "Dashboard | RISE - Ultimate Project Manager and CRM");

    }
    //Check the Fields on Register form
    @Test(priority = 4, description = "TC04_registerFormFields")
    public void registerFormFields() {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//a[text()='Register']"));
        verifyElementTextEquals(By.xpath("//label[text()='Email']"), "Email");
        verifyElementTextEquals(By.xpath("//label[text()='Password']"), "Password");
        verifyElementTextEquals(By.xpath("//label[text()='Confirm password']"), "Confirm password");
        verifyElementTextEquals(By.xpath("//a[text()='« Back to Login']"), "« Back to Login");
        verifyElementPresent(By.xpath("//input[@value=\"Register\"]"));

            }
    //Taking Email, Password And Confirm-Password Dynamically
    @Test(priority = 5, description = "TC05_loginDynamically", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginDynamically(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//a[text()='Register']"));
        clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
        clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
        clickElement(By.xpath("//input[@type='submit']"));

//        signInPage.signIn(data);
    }
//If email is already register then gives an error
    @Test(priority = 6, description = "TC06_emailAlreadyExixts")
    public void emailAlreadyExixts() {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//a[text()='Register']"));
        clearAndFillText(By.xpath("//input[@id='email']"), "jio@mailinator.com");
        clearAndFillText(By.xpath("//input[@id='password']"), "123456");
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), "123456");
        clickElement(By.xpath("//input[@type='submit']"));
        verifyElementTextEquals(By.xpath("//span[text()='Email already exists.']"), "Email already exists.");


    }
//When Password and Confirm password does'nt match
    @Test(priority = 7, description = "TC07_registerTest", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void registerTest(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//a[text()='Register']"));
        clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
        clearAndFillText(By.xpath("//input[@id='password']"), "123465");
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), "123456");
        clickElement(By.xpath("//input[@type='submit']"));
        verifyElementTextEquals(By.xpath("//span[text()=\"Password confirmation doesn't match.\"]"), "Password confirmation doesn't match.");
    }
//When any field is Empty on Register Form
    @Test(priority = 8, description = "TC08_emptyField")
    public void emptyField() {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//a[text()='Register']"));
        //clearAndFillText(By.xpath("//input[@id='email']"), "jioi@mailinator.com");
        clearAndFillText(By.xpath("//input[@id='password']"), "123456");
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), "123456");
        clickElement(By.xpath("//input[@type='submit']"));
        verifyElementTextEquals(By.xpath("//span[text()=\"Please specify email.\"]"), "Please specify email.");
    }
//Chechk that English is a Default Language on Register Form
    @Test(priority = 9, description = "TC09_defaultLanguage")
    public void defaultLanguage() {
        getURL("https://console.uat.asians.group/#/domain/list");
        verifyElementPresent(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"),"English");
    }

//Check the Total Four Languages in Dropdown
@Test(priority = 10, description = "TC010_languages")
public void languages()  {
    getURL("https://console.uat.asians.group/#/domain/list");
    clickElement(By.xpath("//div[@id=\"kc-locale-dropdown\"]/a[text()='English']"));
    verifyElementTextEquals(By.xpath("//li/a[text()='日本語']"), "日本語");
    verifyElementTextEquals(By.xpath("//li/a[text()='kr']"), "kr");
    verifyElementTextEquals(By.xpath("//li/a[text()='English']"),"English");
    verifyElementTextEquals(By.xpath("//li/a[text()='中文简体']"),"中文简体");

    }
//Select the Japanese Language and Execute the tests
    //1-Registeration of a User
@Test(priority = 11, description = "TC011_selectJapanese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
public void selectJapanese(Hashtable<String, String> data) {
    getURL("https://console.uat.asians.group/#/domain/list");
    clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
    clickElement(By.xpath("//a[text()='日本語']"));
    clickElement(By.xpath("//a[text()='登録']"));
    clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
    clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
    clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
    clickElement(By.xpath("//input[@value='登録']"));
    }
   //2-Error Message that Email is Already Exixts in Japanese Language
   @Test(priority = 12, description = "TC012_emailerrorJapanese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
   public void emailerrorJapanese(Hashtable<String, String> data) {
       getURL("https://console.uat.asians.group/#/domain/list");
       clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
       clickElement(By.xpath("//a[text()='日本語']"));
       clickElement(By.xpath("//a[text()='登録']"));
       clearAndFillText(By.xpath("//input[@id='email']"), "jio@mailinator.com");
       clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
       clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
       clickElement(By.xpath("//input[@value='登録']"));
       verifyElementTextEquals(By.xpath("//span[text()='既に存在するEメールです。']"), "既に存在するEメールです。");
    }
    //3-Check the Error Message that Password and Confirm Password does'nt Match in Japanese
    @Test(priority = 13, description = "TC013_passwordMissMatchJapanese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void passwordMissMatchJapanese(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
        clickElement(By.xpath("//a[text()='日本語']"));
        clickElement(By.xpath("//a[text()='登録']"));
        clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
        clearAndFillText(By.xpath("//input[@id='password']"), "123465");
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), "123456");
        clickElement(By.xpath("//input[@value='登録']"));
        verifyElementTextEquals(By.xpath("//span[text()='パスワード確認が一致していません。']"), "パスワード確認が一致していません。");
    }
//4-Error Message When any Field is absent in Japanese
@Test(priority = 14, description = "TC014_fieldEmptyJapanese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
public void fieldEmptyJapanese(Hashtable<String, String> data) {
    getURL("https://console.uat.asians.group/#/domain/list");
    clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
    clickElement(By.xpath("//a[text()='日本語']"));
    clickElement(By.xpath("//a[text()='登録']"));
   // clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
    clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
    clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
    clickElement(By.xpath("//input[@value='登録']"));
    verifyElementTextEquals(By.xpath("//span[text()='Eメールを指定してください。']"), "Eメールを指定してください。");
}

//Select the Korean Language and Execute the tests

    //Select the Chinese Language and execute the tests
//1-Registeration of a User
@Test(priority = 15, description = "TC015_selectChinese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
public void selectChinese(Hashtable<String, String> data) {
    getURL("https://console.uat.asians.group/#/domain/list");
    clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
    clickElement(By.xpath("//a[text()='中文简体']"));
    clickElement(By.xpath("//a[text()='注册']"));
    clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
    clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
    clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
    clickElement(By.xpath("//input[@value='注册']"));
}
    //2-Error Message that Email is Already Exixts in Chinese Language
    @Test(priority = 16, description = "TC016_emailerrorChinese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void emailerrorChinese(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
        clickElement(By.xpath("//a[text()='中文简体']"));
        clickElement(By.xpath("//a[text()='注册']"));
        clearAndFillText(By.xpath("//input[@id='email']"), "jio@mailinator.com");
        clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
        clickElement(By.xpath("//input[@value='注册']"));
        verifyElementTextEquals(By.xpath("//span[text()='电子邮件已存在。']"), "电子邮件已存在。");
    }
    //3-Check the Error Message that Password and Confirm Password does'nt Match in Chinese
    @Test(priority = 17, description = "TC017_passwordMissMatchChinese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void passwordMissMatchChinese(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
        clickElement(By.xpath("//a[text()='中文简体']"));
        clickElement(By.xpath("//a[text()='注册']"));
        clearAndFillText(By.xpath("//input[@id='email']"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
        clearAndFillText(By.xpath("//input[@id='password']"), "123465");
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), "123456");
        clickElement(By.xpath("//input[@value='注册']"));
        verifyElementTextEquals(By.xpath("//span[text()='确认密码不相同']"), "确认密码不相同");
    }
    //4-Error Message When any Field is absent in Chinese Language
    @Test(priority = 18, description = "TC018_fieldIsEmptyChinese",dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void fieldIsEmptyChinese(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//div[@id='kc-locale-dropdown']/a[text()='English']"));
        clickElement(By.xpath("//a[text()='中文简体']"));
        clickElement(By.xpath("//a[text()='注册']"));
        clickElement(By.xpath("//input[@id='email']"));
        clearAndFillText(By.xpath("//input[@id='password']"), data.get(SignInModel.getPassword()));
        clearAndFillText(By.xpath("//input[@id=\"password-confirm\"]"), data.get(SignInModel.getPassword()));
        clickElement(By.xpath("//input[@value='注册']"));
        verifyElementTextEquals(By.xpath("//span[text()='请输入email.']"), "请输入email.");

    }


        //User Login the Page with Valid Credentials
    @Test(priority =19, description = "TC019_usersign")
    public void usersign() {
        getURL("https://console.uat.asians.group/#/domain/list");
        clearAndFillText(By.xpath("//input[@id=\"username\"]"), "jio@mailinator.com");
        clearAndFillText(By.xpath("//input[@id=\"password\"]"), "123456");
        clickElement(By.xpath("//input[@id=\"kc-login\"]"));
    }
//Check Error Message when User Logged Page with Invalid credentials
    @Test(priority = 20, description = "TC020_errorEmailPassword", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void errorEmailPassword(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clearAndFillText(By.xpath("//input[@id=\"username\"]"), DataGenerateUtils.randomString(3) + data.get(SignInModel.getEmail()));
        clearAndFillText(By.xpath("//input[@id=\"password\"]"), data.get(SignInModel.getPassword()));
        clickElement(By.xpath("//input[@id=\"kc-login\"]"));
        verifyElementTextEquals(By.xpath("//span[@id=\"input-error\"]"), "Invalid username or password.");
    }

    //Invalid Password
    @Test(priority = 22, description = "TC022_randomPassword", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void randomPassword(Hashtable<String, String> data) {
        getURL("https://console.uat.asians.group/#/domain/list");
        clickElement(By.xpath("//input[@id=\"username\"]"));
        clearAndFillText(By.xpath("//input[@id=\"password\"]"), data.get(SignInModel.getPassword()));
        clickElement(By.xpath("//input[@id=\"kc-login\"]"));
        verifyElementTextEquals(By.xpath("//span[@id=\"input-error\"]"),"Invalid username or password.");
    }
    //when remember me check box is true
    @Test(priority = 14, description = "TC014_rememberMeTrue")
    public void rememberMeTrue() throws InterruptedException {
        getURL("https://console.uat.asians.group/#/domain/list");
        clearAndFillText(By.xpath("//input[@id=\"username\"]"), "jio@mailinator.com");
        clearAndFillText(By.xpath("//input[@id=\"password\"]"), "123456");
        clickElement(By.xpath("//input[@id=\"rememberMe\"]"));
        clickElement(By.xpath("//input[@id=\"kc-login\"]"));
        Thread.sleep(3000);

        getToUrlAuthentication("https://console.uat.asians.group/#/domain/list","jio@mailinator.com","123456");
       // waitForElementVisible(By.xpath("//input[@id=\"username\"]"));
        Thread.sleep(3000);
    }
    @Test(priority = 14, description = "TC014_rememberMeFasle")
    public void rememberMeFalse() throws InterruptedException {
        getURL("https://console.uat.asians.group/#/domain/list");
        clearAndFillText(By.xpath("//input[@id=\"username\"]"), "jio@mailinator.com");
        clearAndFillText(By.xpath("//input[@id=\"password\"]"), "123456");
       // clickElement(By.xpath("//input[@id=\"rememberMe\"]"));
        clickElement(By.xpath("//input[@id=\"kc-login\"]"));
        String url="https://console.uat.asians.group/#/domain/list";
        String username="jio@mailinator.com";
        String password="123456";
        getToUrlAuthentication(url,username,password);

        Thread.sleep(3000);
    }
}
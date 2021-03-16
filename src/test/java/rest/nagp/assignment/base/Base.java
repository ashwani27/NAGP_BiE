package rest.nagp.assignment.base;

import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.nagp.assignment.utilities.ExtentManager;
import rest.nagp.assignment.utilities.JSONUtils;
import rest.nagp.assignment.utilities.ReadPropertiesFile;
import rest.nagp.assignment.utilities.ResourceParameters;
import rest.nagp.assignment.utilities.RestClientWrapper;

public class Base {
	
	protected static ReadPropertiesFile readPropertiesFile;
	protected static RequestSpecification httpRequest;
	protected static String baseURI;
	public static String uniqueNumber = getUniqueNumber();
	
	public static ExtentReports extent;
	public static ExtentTest test;
	
	@BeforeSuite
	public static void init() {
		extent = ExtentManager.getInstance("reports/ExtentReports.html");
		readPropertiesFile = new ReadPropertiesFile(ResourceParameters.path_CONFIG_PROPERTIES);
		baseURI = readPropertiesFile.getProperty("baseuri");
	}
	
	@BeforeMethod
	public void startTest() {
		httpRequest = RestAssured.given().log().all(true);
	}
	
	@AfterMethod
	public void reportFlush(ITestResult result) {
		System.out.println(result.getMethod().getMethodName());
		if (result.getStatus() == ITestResult.FAILURE)
			test.log(LogStatus.FAIL, result.getThrowable());
		else if (result.getStatus() == ITestResult.SKIP)
			test.log(LogStatus.SKIP, result.getThrowable());
		else
			test.log(LogStatus.PASS, "Test passed");

		extent.flush();
	}
	
	
	@SuppressWarnings("deprecation")
	private static String getUniqueNumber() {
		Date date = new Date();
		int number = date.getDate()+ (date.getMonth()+1) + date.getYear() + date.getHours() + date.getMinutes() + date.getSeconds();
		return Integer.toString(number);
	}
	
	protected String getSessionID(RestClientWrapper restClient) throws Exception {
		restClient = new RestClientWrapper(baseURI, httpRequest);
		String bodyString = JSONUtils.getUserSessionRequestBody(ResourceParameters.username
				, ResourceParameters.password
				, ResourceParameters.userSessionTDPath);
		Response serverResponse = restClient.post(ResourceParameters.sessionEndPoint, bodyString);
		return serverResponse.jsonPath().get("User-Token");
	}
}
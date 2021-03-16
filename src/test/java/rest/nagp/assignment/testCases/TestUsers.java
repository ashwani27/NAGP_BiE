package rest.nagp.assignment.testCases;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import rest.nagp.assignment.base.Base;
import rest.nagp.assignment.utilities.JSONUtils;
import rest.nagp.assignment.utilities.ReadPropertiesFile;
import rest.nagp.assignment.utilities.ResourceParameters;
import rest.nagp.assignment.utilities.RestClientWrapper;

public class TestUsers extends Base {
	private static ReadPropertiesFile testDataFile;
	private static RestClientWrapper restClient;
			
	@BeforeMethod
	public void setupRequest(Method method) {
		testDataFile = new ReadPropertiesFile(ResourceParameters.path_TEST_PARAMETERS_PROPERTIES_USERS);
		httpRequest.header("Authorization", "Token token=51bb85563cb8b0fa7d6684c7c7a3f6eb")
				.header("Content-Type", "application/json");		
		test = extent.startTest(method.getName());
	}
	
	@Test
	public void addAUser() throws Exception {
		restClient = new RestClientWrapper(baseURI, httpRequest);
		String bodyString = JSONUtils.getAddUserRequestBody(testDataFile.getProperty("login") + uniqueNumber
				, testDataFile.getProperty("emailExt")
				, testDataFile.getProperty("password")
				, ResourceParameters.addUserTDPath);
		Response serverResponse = restClient.post(ResourceParameters.addUserEndpoint, bodyString);
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void getAUser() throws Exception {	
		
		httpRequest.header("user-token", getSessionID(restClient))
					.param("login", testDataFile.getProperty("getUserLogin"));
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.get(ResourceParameters.addUserEndpoint);
		
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
		assertEquals(serverResponse.jsonPath().get("login"), testDataFile.getProperty("getUserLogin"));
	}
	
	@Test
	public void updateUserEmail() throws Exception {
		httpRequest.header("user-token", getSessionID(restClient))
						.pathParam("login", testDataFile.getProperty("updateLogin"));
		restClient = new RestClientWrapper(baseURI, httpRequest);
		
		String bodyString = JSONUtils.updateUserRequestBody(testDataFile.getProperty("updateLogin")
				, testDataFile.getProperty("updateEmail")
				, testDataFile.getProperty("updatePassword")
				, ResourceParameters.updateUserTDPath);
		System.out.println(bodyString);
		Response serverResponse = restClient.put("/api/users/{login}", bodyString);
		
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);		
		assertEquals(serverResponse.jsonPath().get("message"), "User successfully updated.");
	}	
}

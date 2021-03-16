package rest.nagp.assignment.testCases;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import rest.nagp.assignment.base.Base;
import rest.nagp.assignment.utilities.ReadPropertiesFile;
import rest.nagp.assignment.utilities.ResourceParameters;
import rest.nagp.assignment.utilities.RestClientWrapper;

public class TestActivities extends Base{

	private static ReadPropertiesFile testDataFile;
	private static RestClientWrapper restClient;
			
	@BeforeMethod
	public void setupRequest(Method method) throws Exception {
		testDataFile = new ReadPropertiesFile(ResourceParameters.path_TEST_PARAMETERS_PROPERTIES_QUOTES);
		httpRequest.header("Authorization", "Token token=51bb85563cb8b0fa7d6684c7c7a3f6eb")
				.header("Content-Type", "application/json")
				.header("user-token", getSessionID(restClient));		
		test = extent.startTest(method.getName());
	}
	
	@Test
	public void getUserActivity() throws Exception {
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.get(ResourceParameters.getActivities);
		serverResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void followUserActivity() throws Exception {
		httpRequest.queryParam("filter", "Albert Einstein")
					.queryParam("type", "author");
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.put(ResourceParameters.followActivities);
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void unfollowUserActivity() throws Exception {
		httpRequest.queryParam("filter", "Albert Einstein")
		.queryParam("type", "author");
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.put(ResourceParameters.unfollowActivities);
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void deleteAnActivity( ) throws Exception {
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.get(ResourceParameters.getActivities);
		List<Map<String, Integer>> activityList = null;
		activityList = serverResponse.jsonPath().getList("activities");
		int activityId = activityList.get(0).get("activity_id");
		
		httpRequest.pathParam("activity_id", activityId);
		restClient = new RestClientWrapper(baseURI, httpRequest);
		serverResponse = restClient.delete("/api/activities/{activity_id}");
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}
}

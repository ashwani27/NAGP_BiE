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

public class TestQuotes extends Base {
	
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
	public void addAQuote() throws Exception {
		restClient = new RestClientWrapper(baseURI, httpRequest);
		String bodyString = JSONUtils.getAddQuoteRequestBody(testDataFile.getProperty("newQuoteAuthor")
				, testDataFile.getProperty("newQuoteBoby")
				, ResourceParameters.addQuotesTDPath);
		Response serverResponse = restClient.post(ResourceParameters.addQuoteEndPoint, bodyString);
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
		assertEquals(serverResponse.jsonPath().get("author"), testDataFile.getProperty("newQuoteAuthor"));
	}
	
	@Test
	public void hideAQuote() throws Exception {
		httpRequest.pathParam("quote_id", testDataFile.getProperty("quote_id"));
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.put("/api/quotes/{quote_id}/hide");
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
		assertEquals(serverResponse.jsonPath().get("id").toString(), testDataFile.getProperty("quote_id").toString());
		assertEquals(serverResponse.jsonPath().get("user_details.hidden"), true);
	}
	
	@Test
	public void markAQuoteFav() throws Exception {	
		httpRequest.pathParam("quote_id", testDataFile.getProperty("quote_id_2"));
		restClient = new RestClientWrapper(baseURI, httpRequest);
		Response serverResponse = restClient.put("/api/quotes/{quote_id}/fav");
		serverResponse.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
		assertEquals(serverResponse.jsonPath().get("id").toString(), testDataFile.getProperty("quote_id_2").toString());
		assertEquals(serverResponse.jsonPath().get("user_details.favorite"), true);
	}
}

package rest.nagp.assignment.utilities;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClientWrapper {
	
	public  String resource;
	public  String baseUrl;
	private  RequestSpecification request;
	private  Response restResponse;

	public RestClientWrapper(String baseUrl, RequestSpecification request) {
		this.request = request;
		this.request.baseUri(baseUrl);
		

	}

	public Response get(String resource) throws Exception {

		restResponse = request.when().get(resource);

		return restResponse;
	}
	
	public Response post(String resource, String bodyString) throws Exception {

		restResponse = request.when().body(bodyString).post(resource);

		return restResponse;
	}
	
	public Response put(String resource, String bodyString) throws Exception {
		
		restResponse = request.when().body(bodyString).put(resource);
		
		return restResponse;
	}
	
	public Response put(String resource) throws Exception {
		
		restResponse = request.when().put(resource);
		
		return restResponse;
	}
	
	public Response delete(String resource) throws Exception {
		
		restResponse = request.when().delete(resource);
		
		return restResponse;
	}

}

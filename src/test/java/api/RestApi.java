package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class RestApi {
  @Test
  public void getStories() {
    Response response = RestAssured.get("http://ergast.com/api/f1/drivers.json");
    System.out.println("Body :" + response.getBody().asString());
    System.out.println("Status Code :" + response.getStatusCode());
    System.out.println("String value :" + response.asString());
    System.out.println("StatusLine :" + response.getStatusLine());
    System.out.println("Header :" + response.getHeader("content-type"));
    System.out.println("Time in seconds :" + response.getTimeIn(TimeUnit.SECONDS));
  }
}
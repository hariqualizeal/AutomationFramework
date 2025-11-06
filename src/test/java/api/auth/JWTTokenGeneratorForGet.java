package api.auth;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

import java.net.URI;
import java.net.URISyntaxException;

public class JWTTokenGeneratorForGet {
  public static void main(String[] args) throws URISyntaxException, IllegalStateException {

    // only base url should be used to generate the jwt (not the complete url)
    String baseURLZephyr = "https://prod-api.zephyr4jiracloud.com/connect";
    String accessKeyZephyr = "zephyr access key";
    String secretKeyZephyr = "zephyr secret key";
    String accIDJira = "account id from jira";

    ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(baseURLZephyr, accessKeyZephyr, secretKeyZephyr, accIDJira).build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    String uriGeneric = baseURLZephyr + "/public/rest/api/1.0/zql/fields/values";
    URI uri = new URI(uriGeneric);
    int expirationInSec = 600;
    String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);

    System.out.println("Final api uri: " + uri);
    System.out.println("JWT Token: " + jwt);
  }
}

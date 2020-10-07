package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
			String param = System.getenv("PARAM1");
			
			String OTP_ENV_CLUSTER_DB  = System.getenv("OTP_ENV_CLUSTER_DB");
			String OTP_ENV_DATABASE_DB = System.getenv("OTP_ENV_DATABASE_DB");
			String OTP_ENV_SECRET_STORE = System.getenv("OTP_ENV_SECRET_STORE");
			String OTP_ENV_ENDPOINT = System.getenv("OTP_ENV_ENDPOINT");
			String OTP_ENV_REGION = System.getenv("OTP_ENV_REGION");
			
			String paramDetails = "OTP_ENV_CLUSTER_DB:" + OTP_ENV_CLUSTER_DB + " :: OTP_ENV_DATABASE_DB:" 
				+ OTP_ENV_DATABASE_DB + ":: OTP_ENV_SECRET_STORE" + OTP_ENV_SECRET_STORE + ":: OTP_ENV_ENDPOINT"
				+ OTP_ENV_ENDPOINT + ":: OTP_ENV_REGION:" + OTP_ENV_REGION;
			
            String output = String.format("{ \"message\": \"hello world\", \"param\": \"%s\" }", paramDetails);

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (IOException e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}

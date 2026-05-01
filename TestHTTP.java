import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestHTTP {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
        String json = "{\"email\":\"test@test.com\"}";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:3000/api/send-otp"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        System.out.println("Sending request...");
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + res.statusCode());
        System.out.println("Body: " + res.body());
        System.out.println("Contains 'success':true -> " + res.body().contains("\"success\":true"));
    }
}

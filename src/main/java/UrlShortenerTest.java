import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

public class UrlShortenerTest {
    public static void main(String[] args) {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken("ya29.AHES6ZRzU2GZI030xDSeaNueBOIkl5DGL_7TT5p39cXw-vzNSbIQbA");
            Plus plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName("Google-oauth2/1.0").setHttpRequestInitializer(credential).build();
            Person profile = plus.people().get("me").execute();
            //
            // GoogleCredential credential = new
            // GoogleCredential().setAccessToken("ya29.AHES6ZRzU2GZI030xDSeaNueBOIkl5DGL_7TT5p39cXw-vzNSbIQbA");
            // // Plus plus = Plus.builder(new NetHttpTransport(), new
            // //
            // JacksonFactory()).setApplicationName("Google-PlusSample/1.0").setHttpRequestInitializer(credential).build();
            // // Plus plus =new Plus(new NetHttpTransport(), new JacksonFactory(),new HttpRequestInitializer() {
            // //
            // // @Override
            // // public void initialize(HttpRequest arg0) throws IOException {
            // // // TODO Auto-generated method stub
            // //
            // // }
            // // });
            // Credential credential2 = new Credential.Builder(accessMethod)
            // .setJsonFactory(jsonFactory)
            // .setTransport(httpTransport)
            // .setTokenServerEncodedUrl(tokenServerEncodedUrl)
            // .setClientAuthentication(clientAuthentication)
            // .setRequestInitializer(requestInitializer)
            // .build();
            //
            //
            // // set up global Plus instance
            // JacksonFactory f = new JacksonFactory();
            // Plus plus = new Plus.Builder(new NetHttpTransport(), f,
            // credential).setApplicationName("Google-PlusServiceAccountSample/1.0").build();
            //
            // Person profile = plus.people().get("me").execute();
            System.out.println("ID: " + profile.getId());
            System.out.println("Name: " + profile.getDisplayName());
            System.out.println("Image URL: " + profile.getImage().getUrl());
            System.out.println("Profile URL: " + profile.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
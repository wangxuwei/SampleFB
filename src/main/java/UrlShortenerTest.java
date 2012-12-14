import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;

public class UrlShortenerTest {
    private static Plus                plus;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory   JSON_FACTORY   = new JacksonFactory();

    /** List the public activities for the authenticated user. */
    private static void listActivities() throws IOException {
        // Fetch the first page of activities
        Plus.Activities.List listActivities = plus.activities().list("me", "public");
        listActivities.setMaxResults(5L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("nextPageToken,items(id,url,object/content)");
        ActivityFeed feed = listActivities.execute();
        // Keep track of the page number in case we're listing activities
        // for a user with thousands of activities. We'll limit ourselves
        // to 5 pages
        int currentPageNumber = 0;
        while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 5) {
        }
        // Fetch the next page
        String nextPageToken = feed.getNextPageToken();
        if (nextPageToken == null) {
        }
        listActivities.setPageToken(nextPageToken);
        feed = listActivities.execute();
    }

    public static void main(String[] args) {
        try {
            // GoogleCredential credential = new
            // GoogleCredential().setAccessToken("ya29.AHES6ZRzU2GZI030xDSeaNueBOIkl5DGL_7TT5p39cXw-vzNSbIQbA");
            // Plus plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(),
            // credential).setApplicationName("Google-oauth2/1.0").setHttpRequestInitializer(credential).build();

            Credential credential = new GoogleCredential().setAccessToken("ya29.AHES6ZRBA7zx7v3xcRr0WDh4aSUv4csVL6T3sRGTNxy779q6PtCCJA");
            // set up global Plus instance
            plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName("Google-PlusSample/1.0").build();
            // run commands
            // listActivities();

            // service account credential (uncomment setServiceAccountUser for domain-wide delegation)
            GoogleCredential credential2 = new GoogleCredential.Builder()
//                                    .setTransport(HTTP_TRANSPORT)
//                                    .setJsonFactory(JSON_FACTORY)
                                    .setServiceAccountScopes(PlusScopes.PLUS_ME)
//             .setServiceAccountUser("south.z@gmail.com")
            .build();
            credential2.setAccessToken("ya29.AHES6ZRBA7zx7v3xcRr0WDh4aSUv4csVL6T3sRGTNxy779q6PtCCJA");
            // set up global Plus instance
            plus = new Plus.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential2).setApplicationName("Google-PlusServiceAccountSample/1.0").setHttpRequestInitializer(credential).build();

            // Person profile = plus.people().get("me").execute();
            // PlusScopes.PLUS_ME
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
             Person profile = plus.people().get("me").execute();
             System.out.println("ID: " + profile.getId());
             System.out.println("Name: " + profile.getDisplayName());
             System.out.println("Image URL: " + profile.getImage().getUrl());
             System.out.println("Profile URL: " + profile.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
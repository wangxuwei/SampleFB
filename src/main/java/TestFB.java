import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.Url;
import com.restfb.types.User;

public class TestFB {
    /**
     * RestFB Graph API client.
     */
    private final FacebookClient facebookClient;

    /**
     * Entry point. You must provide a single argument on the command line: a valid Graph API access token.
     * 
     * @param args
     *            Command-line arguments.
     * @throws IllegalArgumentException
     *             If no command-line arguments are provided.
     */
    public static void main(String[] args) {
        new TestFB("AAACEdEose0cBABn9uBtWCuy4Lck2JUOjv9ZCTzBpaOsYzIEZBt6uobC01SHY1I0eoisV9jRF06jhp1YGuzCE2ZBEqZC0nRPGJCOlOmaZAZBzjNWndfDWCh").runEverything();
    }

    TestFB(String accessToken) {
        facebookClient = new DefaultFacebookClient(accessToken);
    }

    void runEverything() {
        fetchObject();
        fetchObjects();
        fetchObjectsAsJsonObject();
        fetchConnections();
        fetchDifferentDataTypesAsJsonObject();
        query();
        multiquery();
        search();
        metadata();
        paging();
        selection();
        parameters();
        rawJsonResponse();
    }

    void fetchObject() {
        System.out.println("* Fetching single objects *");

        User user = facebookClient.fetchObject("me", User.class);
        Page page = facebookClient.fetchObject("cocacola", Page.class);

        System.out.println("User name: " + user.getName());
        System.out.println("Page fan count: " + page.getFanCount());
    }

    void fetchObjectsAsJsonObject() {
        System.out.println("* Fetching multiple objects at once as a JsonObject *");

        List<String> ids = new ArrayList<String>();
        ids.add("btaylor");
        ids.add("http://www.imdb.com/title/tt0117500/");

        // Make the API call
        JsonObject results = facebookClient.fetchObjects(ids, JsonObject.class);

        // Pull out JSON data by key and map each type by hand.
        JsonMapper jsonMapper = new DefaultJsonMapper();
        User user = jsonMapper.toJavaObject(results.getString("btaylor"), User.class);
        Url url = jsonMapper.toJavaObject(results.getString("http://www.imdb.com/title/tt0117500/"), Url.class);

        System.out.println("User is " + user);
        System.out.println("URL is " + url);
    }

    void fetchObjects() {
        System.out.println("* Fetching multiple objects at once *");

        FetchObjectsResults fetchObjectsResults = facebookClient.fetchObjects(Arrays.asList("me", "cocacola"), FetchObjectsResults.class);

        System.out.println("User name: " + fetchObjectsResults.me.getName());
        System.out.println("Page fan count: " + fetchObjectsResults.page.getFanCount());
    }

    void fetchDifferentDataTypesAsJsonObject() {
        System.out.println("* Fetching different types of data as JsonObject *");

        JsonObject btaylor = facebookClient.fetchObject("btaylor", JsonObject.class);
        System.out.println(btaylor.getString("name"));

        JsonObject photosConnection = facebookClient.fetchObject("me/photos", JsonObject.class);
        String firstPhotoUrl = photosConnection.getJsonArray("data").getJsonObject(0).getString("source");
        System.out.println(firstPhotoUrl);

        String query = "SELECT uid, name FROM user WHERE uid=220439 or uid=7901103";
        List<JsonObject> queryResults = facebookClient.executeQuery(query, JsonObject.class);
        System.out.println(queryResults.get(0).getString("name"));
    }

    /**
     * Holds results from a "fetchObjects" call.
     */
    public static class FetchObjectsResults {
        @Facebook
        User me;

        @Facebook(value = "cocacola")
        Page page;
    }

    void fetchConnections() {
        System.out.println("* Fetching connections *");

        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
        Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);

        System.out.println("Count of my friends: " + myFriends.getData().size());
        System.out.println("First item in my feed: " + myFeed.getData().get(0).getMessage());
    }

    void query() {
        System.out.println("* FQL Query *");

        List<FqlUser> users = facebookClient.executeQuery("SELECT uid, name FROM user WHERE uid=220439 or uid=7901103", FqlUser.class);

        System.out.println("User: " + users);
    }

    void multiquery() {
        System.out.println("* FQL Multiquery *");

        Map<String, String> queries = new HashMap<String, String>();
        queries.put("users", "SELECT uid, name FROM user WHERE uid=220439 OR uid=7901103");
        queries.put("likers", "SELECT user_id FROM like WHERE object_id=122788341354");

        MultiqueryResults multiqueryResults = facebookClient.executeMultiquery(queries, MultiqueryResults.class);

        System.out.println("Users: " + multiqueryResults.users);
        System.out.println("People who liked: " + multiqueryResults.likers);
    }

    /**
     * Holds results from an "executeQuery" call.
     * <p>
     * Be aware that FQL fields don't always map to Graph API Object fields.
     */
    public static class FqlUser {
        @Facebook
        String uid;

        @Facebook
        String name;

        @Override
        public String toString() {
            return name + "  " + uid;
        }
    }

    /**
     * Holds results from an "executeQuery" call.
     * <p>
     * Be aware that FQL fields don't always map to Graph API Object fields.
     */
    public static class FqlLiker {
        @Facebook("user_id")
        String userId;

        @Override
        public String toString() {
            return userId;
        }
    }

    /**
     * Holds results from a "multiquery" call.
     */
    public static class MultiqueryResults {
        @Facebook
        List<FqlUser>  users;

        @Facebook
        List<FqlLiker> likers;
    }

    void search() {
        System.out.println("* Searching connections *");

        Connection<Post> publicSearch = facebookClient.fetchConnection("search", Post.class, Parameter.with("q", "watermelon"), Parameter.with("type", "post"));

        // Connection<User> targetedSearch = facebookClient.fetchConnection("me/home", User.class, Parameter.with("q",
        // "Mark"), Parameter.with("type", "user"));
        Connection<User> targetedSearch = facebookClient.fetchConnection("me/home", User.class, Parameter.with("type", "user"));
        System.out.println("Public search: " + publicSearch.getData().get(0).getMessage());
        System.out.println("Posts on my wall by friends named Mark: " + targetedSearch.getData().size());
    }

    void metadata() {
        System.out.println("* Metadata *");

        User userWithMetadata = facebookClient.fetchObject("me", User.class, Parameter.with("metadata", 1));

        System.out.println("User metadata: has albums? " + userWithMetadata.getMetadata().getConnections().hasAlbums());
    }

    void paging() {
        System.out.println("* Paging support *");

        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
        Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);

        System.out.println("Count of my friends: " + myFriends.getData().size());
        System.out.println("First item in my feed: " + myFeed.getData().get(0));

        if (myFeed.hasNext()) {
            Connection<Post> myFeedPage2 = facebookClient.fetchConnectionPage(myFeed.getNextPageUrl(), Post.class);
            System.out.println("First item in page 2 of my feed: " + myFeedPage2.getData().get(0));
        }
    }

    void selection() {
        System.out.println("* Selecting specific fields *");

        User user = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id,name"));

        System.out.println("User name: " + user.getName());
    }

    void parameters() {
        System.out.println("* Parameter support *");

        Date oneWeekAgo = new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 24L * 7L);

        Connection<Post> filteredFeed = facebookClient.fetchConnection("me/feed", Post.class, Parameter.with("limit", 3), Parameter.with("until", "yesterday"), Parameter.with("since", oneWeekAgo));

        System.out.println("Filtered feed count: " + filteredFeed.getData().size());
    }

    void rawJsonResponse() {
        System.out.println("* Raw JSON *");
        System.out.println("User object JSON: " + facebookClient.fetchObject("me", String.class));
    }
}

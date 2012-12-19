import java.util.Date;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

public class TestFB2 {

    /**
     * RestFB Graph API client.
     */
    private final FacebookClient facebookClient;

    /**
     * Entry point. You must provide a single argument on the command line: a valid Graph API access token. In order for
     * publishing to succeed, you must use an access token for an application that has been granted stream_publish and
     * create_event rights.
     * 
     * @param args
     *            Command-line arguments.
     * @throws IllegalArgumentException
     *             If no command-line arguments are provided.
     */
    public static void main(String[] args) {
        new TestFB2("AAACEdEose0cBAJUI8O1nFA9DnbTiOqtv1jJv3Wy75tpjkEHwRdIL1BZCgs0pmQhM5Fazek3pOwynWbQFZAoBnJ4ZB2ZBYIdF6esSnuo5vsZBWH8B0CB4I").runEverything();
    }

    TestFB2(String accessToken) {
        facebookClient = new DefaultFacebookClient(accessToken);
    }

    void runEverything() {
//        String messageId = publishMessage();
//         delete(messageId);
        // String eventId = publishEvent();
        // delete(eventId);
         String photoId = publishPhoto();
       //  delete(photoId);
    }

    String publishMessage() {
        System.out.println("* Feed publishing *");

        FacebookType publishMessageResponse = facebookClient.publish("100001542382538/feed", FacebookType.class, Parameter.with("message", "test2222"));
        System.out.println("Published message ID: " + publishMessageResponse.getId());
        return publishMessageResponse.getId();
    }

    String publishEvent() {
        System.out.println("* Event publishing *");

        Date tomorrow = new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L);
        Date twoDaysFromNow = new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 48L);

        FacebookType publishEventResponse = facebookClient.publish("me/events", FacebookType.class, Parameter.with("name", "Party"), Parameter.with("start_time", tomorrow), Parameter.with("end_time", twoDaysFromNow));

        System.out.println("Published event ID: " + publishEventResponse.getId());
        return publishEventResponse.getId();
    }

    String publishPhoto() {
        System.out.println("* Binary file publishing *");
        // BinaryAttachment b= new BinaryAttachment("test",getClass().getResourceAsStream("/cat.png"));
        BinaryAttachment b = BinaryAttachment.with("test", getClass().getResourceAsStream("/a.jpg"));
        // FacebookType publishPhotoResponse = facebookClient.publish("me/photos", FacebookType.class,
        // getClass().getResourceAsStream("/cat.png"), Parameter.with("message", "Test cat"));
      //  FacebookType publishPhotoResponse = facebookClient.publish("me/photos", FacebookType.class, b, Parameter.with("message", "Test cat"));
        FacebookType publishPhotoResponse = facebookClient.publish("100001542382538/photos", FacebookType.class, b, Parameter.with("message", "Test cat"));

        //100001542382538
        
        System.out.println("Published photo ID: " + publishPhotoResponse.getId());
        return publishPhotoResponse.getId();
    }

    void delete(String objectId) {
        System.out.println("* Object deletion *");
        System.out.println("Deleted %s: %s" + objectId + facebookClient.deleteObject(objectId));
    }
}

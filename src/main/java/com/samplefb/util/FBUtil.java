package com.samplefb.util;

import java.io.InputStream;
import java.util.List;

import com.restfb.BinaryAttachment;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;

public class FBUtil {
    private final FacebookClient facebookClient;

    public FBUtil(String accessToken) {
        facebookClient = new DefaultFacebookClient(accessToken);
    }

    public User getMyInformation() {
        User user = facebookClient.fetchObject("me", User.class);
        return user;
    }

    public List getAllFriends() {
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
        return myFriends.getData();
    }

    public List getFriendsByPage(Integer limit, Integer offset) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class, Parameter.with("limit", limit), Parameter.with("offset", offset));
        return myFriends.getData();
    }

    public String publish2Wall(String userId, String message) {
        FacebookType publishMessageResponse = facebookClient.publish(userId + "/feed", FacebookType.class, Parameter.with("message", message));
        return publishMessageResponse.getId();
    }

    public String publishPhoto(String userId, String message, InputStream is) {
        FacebookType publishPhotoResponse = facebookClient.publish(userId + "/photos", FacebookType.class, BinaryAttachment.with("userId", is), Parameter.with("message", message));
        return publishPhotoResponse.getId();
    }

    public List getFeedList(String userId) {
        Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);
        return myFeed.getData();
    }
}

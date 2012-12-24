package com.samplefb.service;

import java.io.InputStream;
import java.util.List;

import com.restfb.types.User;

public interface IBaseSocialService {
    public User getMyInformation(String accessToken);

    public List getAllFriends(String accessToken);

    public List getFriendsByPage(String accessToken, Integer limit, Integer offset);

    public String publish(String accessToken, String userId, String message);

    public String publishPhoto(String accessToken, String userId, String message, InputStream is);

    public List getFeedList(String accessToken, String userId);
}

package com.samplefb.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.google.inject.Inject;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.samplefb.dao.ContactDao;

//[User[about=null bio=null birthday=null birthdayAsDate=null education=[] email=null favoriteAthletes=[] favoriteTeams=[] firstName=null gender=null hometown=null hometownName=null id=100001542382538 interestedIn=[] languages=[] lastName=null link=null locale=null location=null meetingFor=[] metadata=null middleName=null name=Woofgl Liang political=null quotes=null relationshipStatus=null religion=null significantOther=null sports=[] thirdPartyId=null timezone=null type=null updatedTime=null username=null verified=null website=null work=[]], ]
public class ContactWebHandler {
    @Inject
    private ContactDao contactDao;

    @WebModelHandler(startsWith = "/friends")
    public void getFriends(@WebParam("token") String token, @WebParam("limit") Integer limit,
                            @WebParam("offset") Integer offset, RequestContext rc) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class, Parameter.with("limit", limit), Parameter.with("offset", offset));
        List<User> users = new ArrayList<User>();

        for (User myFriend : myFriends.getData()) {
            users.add(myFriend);
        }
        rc.getWebModel().put("_jsonData", users);
    }

}

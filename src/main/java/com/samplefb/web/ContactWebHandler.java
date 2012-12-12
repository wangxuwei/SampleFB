package com.samplefb.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.handler.annotation.WebActionHandler;
import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.google.inject.Inject;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.samplefb.dao.ContactDao;
import com.samplefb.dao.GroupDao;
import com.samplefb.entity.Contact;
import com.samplefb.entity.Group;

//[User[about=null bio=null birthday=null birthdayAsDate=null education=[] email=null favoriteAthletes=[]
//favoriteTeams=[] firstName=null gender=null hometown=null hometownName=null id=100001542382538
//interestedIn=[] languages=[] lastName=null link=null locale=null location=null meetingFor=[] metadata=null 
//middleName=null name=Woofgl Liang political=null quotes=null relationshipStatus=null religion=null significantOther=null 
//sports=[] thirdPartyId=null timezone=null type=null updatedTime=null username=null verified=null website=null work=[]], ]
public class ContactWebHandler {
    @Inject
    private ContactDao contactDao;

    @Inject
    private GroupDao   groupDao;

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

    @WebActionHandler
    public Object addContact(@WebParam("groupId") Long groupId, @WebParam("fbid") String fbid,
                            @WebParam("name") String name, @WebParam("email") String email,
                            @WebParam("hometownName") String hometownName) {
        Contact c = contactDao.getContactByFbid(fbid);
        if (c == null) {
            c = new Contact();
        }
        c.setFbid(fbid);
        c.setName(name);
        c.setEmail(email);
        c.setHometownName(hometownName);
        if (c.getId() == null) {
            contactDao.save(c);
        } else {
            contactDao.update(c);
        }
        Group group = groupDao.get(groupId);
        Set contacts = group.getContactList();
        contacts.add(c);
        group.setContactList(contacts);
        groupDao.update(group);
        return group;
    }
}

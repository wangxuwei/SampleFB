package com.samplefb.web;

import java.util.List;
import java.util.Set;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.handler.annotation.WebActionHandler;
import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.google.inject.Inject;
import com.samplefb.dao.ContactDao;
import com.samplefb.dao.GroupDao;
import com.samplefb.entity.Contact;
import com.samplefb.entity.Group;
import com.samplefb.util.FBUtil;

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
        List ls = new FBUtil(token).getFriendsByPage(limit, offset);
        rc.getWebModel().put("_jsonData", ls);
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

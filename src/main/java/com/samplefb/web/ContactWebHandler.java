package com.samplefb.web;

import java.util.List;
import java.util.Set;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.handler.annotation.WebActionHandler;
import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.google.inject.Inject;
import com.restfb.types.User;
import com.samplefb.dao.ContactDao;
import com.samplefb.dao.ContactInfoDao;
import com.samplefb.dao.GroupDao;
import com.samplefb.entity.Contact;
import com.samplefb.entity.ContactInfo;
import com.samplefb.entity.Group;
import com.samplefb.service.FacebookService;

//[User[about=null bio=null birthday=null birthdayAsDate=null education=[] email=null favoriteAthletes=[]
//favoriteTeams=[] firstName=null gender=null hometown=null hometownName=null id=100001542382538
//interestedIn=[] languages=[] lastName=null link=null locale=null location=null meetingFor=[] metadata=null 
//middleName=null name=Woofgl Liang political=null quotes=null relationshipStatus=null religion=null significantOther=null 
//sports=[] thirdPartyId=null timezone=null type=null updatedTime=null username=null verified=null website=null work=[]], ]
public class ContactWebHandler {
    @Inject
    private ContactDao      contactDao;
    @Inject
    private ContactInfoDao  contactInfoDao;
    @Inject
    private GroupDao        groupDao;

    @Inject
    private FacebookService facebookService;

    @WebModelHandler(startsWith = "/friends")
    public void getFriends(@WebParam("token") String token, @WebParam("limit") Integer limit,
                            @WebParam("offset") Integer offset, RequestContext rc) {
        List ls = facebookService.getFriendsByPage(token, limit, offset);
        rc.getWebModel().put("_jsonData", ls);
    }

    @WebActionHandler
    public Object addContact(@WebParam("token") String token, @WebParam("groupId") Long groupId,
                            @WebParam("fbid") String fbid) {
        try {

            Contact c = contactDao.getContactByFbid(fbid);
            if (c == null) {
                c = new Contact();
            }
            c.setFbid(fbid);
            User user = facebookService.getFriendInformation(token, fbid);
            c.setName(user.getName());
            c.setEmail(user.getEmail());
            c.setHometownName(user.getHometownName());
            if (c.getId() == null) {
                contactDao.save(c);
            } else {
                contactDao.update(c);
            }
            ContactInfo info = contactDao.getContactInfo(c.getId());
            if (info == null) {
                info = new ContactInfo();
            }
            //
            info.setContactId(c.getId());
            info.setBirthday(user.getBirthday());
            info.setEducation(user.getEducation().toString());
            info.setFavoriteAthletes(user.getFavoriteAthletes().toString());
            info.setFirstName(user.getFirstName());
            info.setGender(user.getGender());
            info.setInterestedIn(user.getInterestedIn().toString());
            info.setLanguages(user.getLanguages().toString());
            info.setLastName(user.getLastName());
            info.setLink(user.getLink());
            info.setLocale(user.getLocale());
            info.setLocation(user.getLocation() == null ? "" : user.getLocation().getName());
            info.setMeetingFor(user.getMeetingFor().toString());
            info.setMiddleName(user.getMiddleName());
            info.setQuotes(user.getQuotes());
            info.setRelationshipStatus(user.getRelationshipStatus());
            info.setReligion(user.getReligion());
            info.setSignificantOther(user.getSignificantOther() == null ? "" : user.getSignificantOther().toString());
            info.setSports(user.getSports().toString());
            info.setThirdPartyId(user.getThirdPartyId());
            info.setTimezone(user.getTimezone() == null ? "" : user.getTimezone().toString());
            info.setType(user.getType());
            info.setUpdatedTime(user.getUpdatedTime() == null ? "" : user.getUpdatedTime().toString());
            info.setUsername(user.getUsername());
            info.setVerified(user.getVerified() == null ? "" : user.getVerified().toString());
            info.setWebsite(user.getWebsite());
            if (info.getId() == null) {
                contactInfoDao.save(info);
            } else {
                contactInfoDao.update(info);
            }
            //
            Group group = groupDao.get(groupId);
            Set contacts = group.getContactList();
            contacts.add(c);
            group.setContactList(contacts);
            groupDao.update(group);
            return group;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

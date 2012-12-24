package com.samplefb.dao;

import java.util.List;

import com.samplefb.entity.Contact;
import com.samplefb.entity.ContactInfo;

public class ContactDao extends BaseHibernateDao<Contact> {
    public Contact getContactByFbid(String fbid) {
        String hql = "from Contact where fbid=?";
        List ls = search(hql, fbid);
        if (ls.size() > 0) {
            return (Contact) ls.get(0);
        }
        return null;
    }

    public ContactInfo getContactInfo(Long contactId) {
        String hql = "from ContactInfo where contactId=?";
        List ls = search(hql, contactId);
        if (ls.size() > 0) {
            return (ContactInfo) ls.get(0);
        }
        return null;
    }
}

package com.samplefb.dao;

import java.util.List;

import com.samplefb.entity.Contact;

public class ContactDao extends BaseHibernateDao<Contact> {
    public Contact getContactByFbid(String fbid) {
        String hql = "from Contact where fbid=?";
        List ls = search(hql, fbid);
        if (ls.size() > 0) {
            return (Contact) ls.get(0);
        }
        return null;
    }
}

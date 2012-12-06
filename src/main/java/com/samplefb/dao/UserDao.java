package com.samplefb.dao;

import java.util.List;

import com.samplefb.entity.Socialdentity;
import com.samplefb.entity.User;

public class UserDao extends BaseHibernateDao<User> {

    public User getUser(String name) {
        List<User> users = search("from User u where u.username = ?", name);
        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public User getUserByFB(String userID) {
        List<Socialdentity> ls = (List<Socialdentity>) daoHelper.find(0, 5, "from Socialdentity s where s.fbid = ?", userID);
        if (ls.size() == 1) {
            Socialdentity s = ls.get(0);
            System.out.println(s.getFbid());
           // System.out.println(s.getFbSignedRequest());
            System.out.println(s.getFbToken());
            Long user_id = s.getUser_id();
            User user = get(user_id);
            return user;
        }
        return null;
    }


    @Override
    public User save(User user) {
        super.save(user);
        return user;
    }
}

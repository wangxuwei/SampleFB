package com.samplefb.web;

import java.util.Map;

import com.britesnow.snow.util.ObjectUtil;
import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.auth.AuthRequest;
import com.britesnow.snow.web.auth.AuthToken;
import com.britesnow.snow.web.handler.annotation.WebActionHandler;
import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.britesnow.snow.web.param.annotation.WebUser;
import com.google.common.base.Objects;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.samplefb.dao.SocialdentityDao;
import com.samplefb.dao.UserDao;
import com.samplefb.entity.Socialdentity;
import com.samplefb.entity.User;

public class SampleFBAuthRequest implements AuthRequest {
    @Inject
    private UserDao           userDao;

    @Inject
    private SocialdentityDao  socialdentityDao;


    @Inject
    private WebUtil           webUtil;

    @Override
    public AuthToken authRequest(RequestContext rc) {
        // Note: this is not the login logic, the login logic would be
        // @WebActionHandler that would generate the appropriate

        // Note: this is a simple stateless authentication scheme.
        // Security is medium-low, however, with little bit more logic
        // it can be as secure as statefull login while keeping it's scalability attributes

        // First, we get userId and userToken from cookie
        String userIdStr = rc.getCookie("userId");
        String userToken = rc.getCookie("userToken");

        if (userIdStr != null && userToken != null) {
            // get the User from the DAO
            String userId = ObjectUtil.getValue(userIdStr, String.class, null);
            User user = null;
            user = userDao.get(new Long(userId));
            if (user==null) {
                return null;
            }

            // Build the expectedUserToken from the user info
            // For this example, simplistic userToken (sha1(username,password))
            String expectedUserToken = Hashing.sha1().hashString(user.getUsername() + user.getId()).toString();
            if (Objects.equal(expectedUserToken, userToken)) {
                Socialdentity so = socialdentityDao.getSocialdentityByUserId(user.getId());
                rc.setAttribute("fbid", so.getFbid());
                rc.setAttribute("fbtoken", so.getFbToken());
//                System.out.println(so.getFbid());
//                System.out.println(so.getFbToken());
                
                // if valid, then, we create the AuthTocken with our User object
                AuthToken<User> authToken = new AuthToken<User>();
                user.setSocialdentity(so);
                authToken.setUser(user);
                //
                
                return authToken;

            } else {
                // otherwise, we could throw an exception, or just return null
                // In this example (and snowStarter, we just return null)
                return null;
            }
        } else {
            return null;
        }
    }

    @WebModelHandler(startsWith = "/")
    public void pageIndex(@WebModel Map m, @WebUser User user, RequestContext rc) {
        // gameTestManager.init();
        m.put("user", user);
    }

    @WebModelHandler(startsWith = "/logout")
    public void logout(@WebModel Map m, @WebUser User user, RequestContext rc) {
        if (user != null) {
            rc.removeCookie("userToken");
            rc.removeCookie("userId");

            //
        }
    }

    @WebActionHandler
    public Object login(@WebParam("userId") String userId, @WebParam("username") String username,
                            @WebParam("password") String password, RequestContext rc) {
        User user =null;

        if (user == null) {
            if (username != null && "heartsnow".equals(password)) {
                user = new User();
                user.setUsername(username);
                user.setPassword(password);
                userDao.save(user);
                setUserToSession(rc, user);
                return user;
            }
        } else if (authentication(user, password)) {
            setUserToSession(rc, user);
            return user;
        }
        return "null";
    }

    @WebActionHandler
    public Object fbLogin(@WebParam("accessToken") String accessToken, @WebParam("expiresIn") String expiresIn,
                            @WebParam("signedRequest") String signedRequest, @WebParam("userID") String userID,
                            RequestContext rc) {
        User user = userDao.getUserByFB(userID);
        if (user == null) {
            user = userDao.getUser(userID);
            if (user ==null) {
                user = new User();
                user.setUsername(userID);
                user.setPassword("heartsnow");
                user = userDao.save(user);
            }
            //
            Socialdentity s = new Socialdentity();
            s.setUser_id(user.getId());
            s.setFbid(userID);
            s.setFbToken(accessToken);
            s.setFbSignedRequest(signedRequest);
            socialdentityDao.save(s);
            System.out.println(s.getFbid());
            //System.out.println(s.getFbSignedRequest());
            System.out.println(s.getFbToken());
            //
            rc.setAttribute("fbid", s.getFbid());
            rc.setAttribute("fbtoken", s.getFbToken());
            setUserToSession(rc, user);
            return user;
        } else {
            //
            Socialdentity s = new Socialdentity();
            s.setUser_id(user.getId());
            s.setFbid(userID);
            s.setFbToken(accessToken);
            s.setFbSignedRequest(signedRequest);
            socialdentityDao.save(s);
            System.out.println(s.getFbid());
            //System.out.println(s.getFbSignedRequest());
            System.out.println(s.getFbToken());
            //
            rc.setAttribute("fbid", s.getFbid());
            rc.setAttribute("fbtoken", s.getFbToken());
            setUserToSession(rc, user);
            setUserToSession(rc, user);
            return user;
        }
    }

    // --------- Private Helpers --------- //
    // store the user in the session. If user == null, then, remove it.
    private void setUserToSession(RequestContext rc, User user) {
        // TODO: need to implement session less login (to easy loadbalancing)
        if (user != null) {
            String userToken = Hashing.sha1().hashString(user.getUsername() + user.getId()).toString();
            rc.setCookie("userToken", userToken);
            rc.setCookie("userId", user.getId());
            //
        }
    }

    private boolean authentication(User user, String password) {
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }
    // --------- /Private Helpers --------- //
}
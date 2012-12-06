package com.samplefb.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.britesnow.snow.web.RequestContext;
import com.google.inject.Singleton;
import com.samplefb.entity.User;

@Singleton
public class WebUtil {
    private static Logger log = LoggerFactory.getLogger(WebUtil.class);

    public User getUser(RequestContext rc) {
        return rc.getUser(User.class);
    }
}
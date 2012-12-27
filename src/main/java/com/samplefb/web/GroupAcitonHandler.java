package com.samplefb.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.handler.annotation.WebActionHandler;
import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.google.inject.Inject;
import com.samplefb.dao.GroupDao;
import com.samplefb.entity.Group;
import com.samplefb.entity.User;

/**
 * 
 * @author lizhe
 */
public class GroupAcitonHandler {
    @Inject
    private GroupDao groupDao;
    @Inject
    private WebUtil  webUtil;

    @WebModelHandler(startsWith = "/getGroupList")
    public void getGroupList(@WebModel Map m, RequestContext rc) {
        String hql = "from Group where user_id=? order by name ";
        User currentUser = webUtil.getUser(rc);
        List reList = groupDao.search(hql, currentUser.getId());
        m.put("result", reList);
    }

    @WebModelHandler(startsWith = "/getGroupById")
    public void getGroupById(@WebModel Map m, @WebParam("id") Long id) {
        Group po = groupDao.getGroupById(id);
        m.put("result", po);
    }

    @WebActionHandler(name = "createGroup")
    public Map createGroup(@WebParam("id") Long id, @WebParam("name") String name, RequestContext rc) {
        Map resultMap = new HashMap();
        try {
            User currentUser = webUtil.getUser(rc);
            Group po = new Group();
            if (id != null) {
                po = groupDao.get(id);
            }
            po.setName(name);
            po.setUser_id(currentUser.getId());
            if (po.getId() != null) {
                groupDao.update(po);
            } else {
                groupDao.save(po);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}

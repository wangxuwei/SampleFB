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

/**
 * 
 * @author lizhe
 */
public class GroupAcitonHandler {
    @Inject
    private GroupDao groupDao;

    @WebModelHandler(startsWith = "/getGroupList")
    public void getGroupList(@WebModel Map m) {
        String hql = "from Group";
        List reList = groupDao.search(hql);
        m.put("groupList", reList);
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
            Group po = new Group();
            if (id != null) {
                groupDao.get(id);
            }
            po.setName(name);
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

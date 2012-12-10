package com.samplefb.dao;

import com.samplefb.entity.Group;

public class GroupDao extends BaseHibernateDao<Group> {
    public Group getGroupById(Long id) {
        Group po = get(id);
        return po;
    }

    public Group saveProject(Group po) {
        super.save(po);

        return po;
    }

}

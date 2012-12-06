package com.samplefb.dao;

import com.samplefb.entity.Socialdentity;

public class SocialdentityDao extends BaseHibernateDao<Socialdentity> {
    @Override
    public Socialdentity save(Socialdentity s) {
        super.save(s);
        return s;
    }
}
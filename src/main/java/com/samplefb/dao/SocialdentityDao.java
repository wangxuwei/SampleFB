package com.samplefb.dao;

import java.util.List;

import com.samplefb.entity.Socialdentity;

public class SocialdentityDao extends BaseHibernateDao<Socialdentity> {
    @Override
    public Socialdentity save(Socialdentity s) {
        List<Socialdentity> ls = (List<Socialdentity>) daoHelper.find(0, 5, "from Socialdentity s where s.user_id = ?", s.getUser_id());
        for (int i = 0; i < ls.size(); i++) {
            System.out.println("delete:" + i);
            daoHelper.delete(ls.get(i));
        }
        super.save(s);
        return s;
    }

    public Socialdentity getSocialdentityByUserId(Long id) {
        List<Socialdentity> ls = (List<Socialdentity>) daoHelper.find(0, 5, "from Socialdentity s where s.user_id = ?", id);
        if (ls.size() > 0) {
            return ls.get(0);
        }
        return null;
    }
}
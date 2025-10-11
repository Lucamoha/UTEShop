package com.uteshop.dao.manager.impl;

import com.uteshop.dao.AbstractDao;

public class EntityDao<T> extends AbstractDao<T> {
    public EntityDao(Class<T> cls) {
        super(cls);
    }
}

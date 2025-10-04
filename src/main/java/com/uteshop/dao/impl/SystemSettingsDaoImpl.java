package com.uteshop.dao.impl;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.ISystemSettingsDao;
import com.uteshop.entity.SystemSettings;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


public class SystemSettingsDaoImpl implements ISystemSettingsDao {

	@Override
	public List<SystemSettings> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<SystemSettings> query = enma.createNamedQuery("SystemSettings.findAll", SystemSettings.class);
		return query.getResultList();
	}

}

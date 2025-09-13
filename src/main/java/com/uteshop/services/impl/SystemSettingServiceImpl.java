package com.uteshop.services.impl;

import java.util.List;

import com.uteshop.dao.ISystemSettingsDao;
import com.uteshop.dao.impl.SystemSettingsDaoImpl;
import com.uteshop.entities.SystemSettings;
import com.uteshop.services.ISystemSettingsService;

public class SystemSettingServiceImpl implements ISystemSettingsService {
	ISystemSettingsDao systemSettingDao = new SystemSettingsDaoImpl();
	@Override
	public List<SystemSettings> findAll() {
		return systemSettingDao.findAll();
	}

}

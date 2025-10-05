package com.uteshop.services;

import java.util.List;

import com.uteshop.entity.SystemSettings;

public interface ISystemSettingsService {
	public List<SystemSettings> findAll();
}

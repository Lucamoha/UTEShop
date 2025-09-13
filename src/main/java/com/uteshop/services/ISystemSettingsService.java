package com.uteshop.services;

import java.util.List;

import com.uteshop.entities.SystemSettings;

public interface ISystemSettingsService {
	public List<SystemSettings> findAll();
}

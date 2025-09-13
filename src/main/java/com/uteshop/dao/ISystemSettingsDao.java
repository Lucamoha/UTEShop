package com.uteshop.dao;

import java.util.List;

import com.uteshop.entities.SystemSettings;


public interface ISystemSettingsDao {
	List<SystemSettings> findAll();
}

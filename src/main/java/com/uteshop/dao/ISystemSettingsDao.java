package com.uteshop.dao;

import java.util.List;

import com.uteshop.entity.SystemSettings;


public interface ISystemSettingsDao {
	List<SystemSettings> findAll();
}

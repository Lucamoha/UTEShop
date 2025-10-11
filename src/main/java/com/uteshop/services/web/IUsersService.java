package com.uteshop.services.web;

public interface IUserService {
    boolean checkDuplicate(String email, String phone);
}

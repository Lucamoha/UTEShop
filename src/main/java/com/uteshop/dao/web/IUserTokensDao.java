package com.uteshop.dao.web;

import com.uteshop.entity.auth.UserTokens;

public interface IUserTokensDao {
    void insert(UserTokens token);

    UserTokens findByTokenAndType(String token, int type);

    void markAsUsed(Integer tokenId);

    void deleteExpiredTokens();

    void deleteByUserId(Integer userId);
}

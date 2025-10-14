package com.uteshop.filters;

import java.io.IOException;

import com.uteshop.configs.JPAConfigs;
import jakarta.servlet.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class OpenSessionInViewFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }

            chain.doFilter(request, response);

            if (tx.isActive()) {
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new ServletException(e);
        } finally {
            em.close(); //Đảm bảo đóng EntityManager sau khi JSP render xong
        }
    }
}

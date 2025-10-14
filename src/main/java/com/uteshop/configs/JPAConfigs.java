package com.uteshop.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManagerFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class JPAConfigs {

    private static EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "dataSource";

    // Khối khởi tạo tĩnh, chạy một lần duy nhất khi class được load
    static {
        try {
            Properties props = new Properties();
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
                if (is != null) {
                    props.load(is);
                } else {
                    System.err.println("config.properties not found!");
                }
            }
            
            HashMap<String, Object> overrides = new HashMap<>();
            overrides.put("jakarta.persistence.jdbc.url", props.getProperty("db.url"));
            overrides.put("jakarta.persistence.jdbc.user", props.getProperty("db.username"));
            overrides.put("jakarta.persistence.jdbc.password", props.getProperty("db.password"));

            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, overrides);

        } catch (Exception e) {
            System.err.println("Initial EntityManagerFactory creation failed.");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory has not been initialized.");
        }
        return entityManagerFactory.createEntityManager();
    }

    //Đóng EntityManagerFactory khi ứng dụng kết thúc
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static void main(String[] args) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em == null) {
                System.out.println("EntityManager is null");
            } else {
                System.out.println("EntityManager successfully created: " + em);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        shutdown();
    }
}

/*
 * @PersistenceContext public class JPAConfigs { public static EntityManager
 * getEntityManager() {
 * 
 * try (var is =
 * Thread.currentThread().getContextClassLoader().getResourceAsStream(
 * "config.properties")){ var props = new java.util.Properties();
 * props.load(is);
 * 
 * var overrides = new java.util.HashMap<String, Object>();
 * overrides.put("jakarta.persistence.jdbc.url", props.getProperty("db.url"));
 * overrides.put("jakarta.persistence.jdbc.user",
 * props.getProperty("db.username"));
 * overrides.put("jakarta.persistence.jdbc.password",
 * props.getProperty("db.password"));
 * 
 * return Persistence.createEntityManagerFactory("dataSource",
 * overrides).createEntityManager(); } catch (Exception e){ e.printStackTrace();
 * return null; }
 * 
 * 
 * 
 * EntityManagerFactory enmafact =
 * Persistence.createEntityManagerFactory("dataSource"); return
 * enmafact.createEntityManager();
 * 
 * }
 * 
 * public static void main(String[] args) { EntityManager em =
 * getEntityManager(); if (em == null) {
 * System.out.println("EntityManager is null"); } else { System.out.println(em);
 * } } }
 */
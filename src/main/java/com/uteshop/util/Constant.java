package com.uteshop.util;

import java.io.File;

public class Constant {
    public static final String DIR = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "webapp"
            + File.separator + "uploads";

    public static final String ADMIN = "admin";
        public static final String EMPLOYEE = "employee";
        public static final String USER_ = "user";

        public static void main(String[] args) {
            System.out.println("Current directory: " + DIR);
        }
}
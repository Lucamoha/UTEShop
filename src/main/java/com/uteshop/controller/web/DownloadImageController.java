package com.uteshop.controller.web;

import com.uteshop.util.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet(urlPatterns= {"/image"}) //fname: abc.png
public class DownloadImageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("fname");

        // Lấy thư mục uploads từ webapp thực tế
        String uploadPath = req.getServletContext().getRealPath("/uploads");
        File file = new File(uploadPath, fileName);

        if(!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Xác định Content-Type theo phần mở rộng
        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            resp.setContentType("image/jpeg");
        } else if(fileName.endsWith(".png")) {
            resp.setContentType("image/png");
        } else if(fileName.endsWith(".webp")) {
            resp.setContentType("image/webp");
        } else {
            resp.setContentType("application/octet-stream"); // file khác
        }

        // Ghi file ra output stream
        try (FileInputStream fis = new FileInputStream(file)) {
            IOUtils.copy(fis, resp.getOutputStream());
        }
    }
}


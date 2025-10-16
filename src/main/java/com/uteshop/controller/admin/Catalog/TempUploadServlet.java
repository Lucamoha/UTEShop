package com.uteshop.controller.admin.Catalog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/admin/Catalog/Products/image/tmpUpload")
@MultipartConfig
public class TempUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMP_DIR_NAME = "uploads" + File.separator + "tmp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[TempUploadServlet] Bắt đầu xử lý upload...");

        try {
            Part filePart = request.getPart("file");
            if (filePart == null || filePart.getSize() == 0) {
                System.out.println("[TempUploadServlet] Không nhận được file.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print("no-file");
                return;
            }

            String webAppRoot = request.getServletContext().getRealPath("/");
            Path uploadPath = Paths.get(webAppRoot, TEMP_DIR_NAME);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = filePart.getSubmittedFileName();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID().toString() + extension;
            System.out.println("[TempUploadServlet] Upload file: " + originalFilename + " -> " + newFilename);

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, uploadPath.resolve(newFilename));
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(newFilename);
            writer.flush();
            System.out.println("[TempUploadServlet] Upload thành công, trả về: " + newFilename);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("upload-error");
        }
    }
}

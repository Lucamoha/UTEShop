package com.uteshop.controller.web.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/image")
public class DownloadImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String dirParam = req.getParameter("dir");
        String fnameParam = req.getParameter("fname");

        System.out.println("[DownloadImageController] dir=" + dirParam + ", fname=" + fnameParam);

        // Kiểm tra bắt buộc có fname
        if (fnameParam == null || fnameParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số fname.");
            return;
        }

        String dir = "";
        if (dirParam != null && !dirParam.isEmpty()) {
            dir = java.net.URLDecoder.decode(dirParam, "UTF-8");
            if (dir.contains("..")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tên thư mục không hợp lệ.");
                return;
            }
        }

        String fname = java.net.URLDecoder.decode(fnameParam, "UTF-8");
        if (fname.contains("..")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tên file không hợp lệ.");
            return;
        }

        // Lấy đường dẫn uploads source (giống TempUploadServlet)
        String realPath = req.getServletContext().getRealPath("/uploads");
        String uploadRoot;
        
        if (realPath != null && realPath.contains(".metadata")) {
            // Xử lý đường dẫn Eclipse server
            int metadataIndex = realPath.indexOf(".metadata");
            String workspaceRoot = realPath.substring(0, metadataIndex);
            
            String projectName = "UTEShop";
            if (realPath.contains("wtpwebapps")) {
                int wtpIndex = realPath.indexOf("wtpwebapps") + "wtpwebapps".length() + 1;
                int uploadIndex = realPath.indexOf(File.separator + "uploads", wtpIndex);
                if (uploadIndex > wtpIndex) {
                    projectName = realPath.substring(wtpIndex, uploadIndex);
                }
            }
            
            uploadRoot = workspaceRoot + projectName + File.separator + "src" + File.separator + 
                       "main" + File.separator + "webapp" + File.separator + "uploads";
        } else if (realPath != null && realPath.contains("target")) {
            // Xử lý đường dẫn Maven target
            String projectRoot = realPath.substring(0, realPath.indexOf("target"));
            uploadRoot = projectRoot + "src" + File.separator + "main" + File.separator + 
                       "webapp" + File.separator + "uploads";
        } else {
            uploadRoot = realPath;
        }
        
        System.out.println("[DownloadImageController] Upload root: " + uploadRoot);
        File baseDir = new File(uploadRoot);
        File file;
        
        // Nếu fname bắt đầu với "products/", tìm file trong toàn bộ cây thư mục
        if (fname.startsWith("products" + File.separator) || fname.startsWith("products/")) {
            String fileName = fname.substring(fname.indexOf("/") + 1); // Lấy tên file sau "products/"
            file = findFileRecursively(baseDir, fileName);
            
            if (file == null) {
                System.out.println("[DownloadImageController] Không tìm thấy file: " + fileName);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File không tồn tại.");
                return;
            }
        } else {
            // Xử lý bình thường cho các file khác
            file = (dir.isEmpty()) ? new File(baseDir, fname) : new File(baseDir, dir + File.separator + fname);
            
            if (!file.exists() || file.isDirectory()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File không tồn tại.");
                return;
            }
        }

        String mimeType = req.getServletContext().getMimeType(file.getAbsolutePath());
        if (mimeType == null) mimeType = "application/octet-stream";

        resp.setContentType(mimeType);
        resp.setContentLengthLong(file.length());

        System.out.println("[DownloadImageController] Gửi file: " + file.getAbsolutePath());

        try (java.io.FileInputStream in = new java.io.FileInputStream(file);
             java.io.OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }
    
    /**
     * Tìm file đệ quy trong thư mục và các thư mục con
     * @param directory Thư mục gốc để tìm
     * @param fileName Tên file cần tìm
     * @return File nếu tìm thấy, null nếu không tìm thấy
     */
    private File findFileRecursively(File directory, String fileName) {
        if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }
        
        // Tìm trong thư mục hiện tại
        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName)) {
                return file;
            }
        }
        
        // Tìm đệ quy trong các thư mục con
        for (File file : files) {
            if (file.isDirectory()) {
                File found = findFileRecursively(file, fileName);
                if (found != null) {
                    return found;
                }
            }
        }
        
        return null;
    }
}


/*
 * @WebServlet("/image") public class DownloadImageController extends
 * HttpServlet { private static final long serialVersionUID = 1L;
 * 
 * @Override protected void doGet(HttpServletRequest req, HttpServletResponse
 * resp) throws ServletException, IOException {
 * 
 * // 1. Lấy tên file từ request String fileName = req.getParameter("fname");
 * 
 * // 2. KIỂM TRA BẢO MẬT: Ngăn chặn tấn công Directory Traversal if (fileName
 * == null || fileName.isEmpty() || fileName.contains("..") ||
 * fileName.contains("/") || fileName.contains("\\")) {
 * resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tên file không hợp lệ.");
 * return; }
 * 
 * // 3. Xây dựng đường dẫn đầy đủ và an toàn String uploadPath =
 * req.getServletContext().getRealPath("/uploads"); File file = new
 * File(uploadPath, fileName);
 * 
 * // 4. Kiểm tra file có tồn tại và không phải là thư mục if (!file.exists() ||
 * file.isDirectory()) { resp.sendError(HttpServletResponse.SC_NOT_FOUND);
 * return; }
 * 
 * // 5. CẢI THIỆN: Tự động xác định Content-Type (MIME Type) String mimeType =
 * req.getServletContext().getMimeType(file.getAbsolutePath()); if (mimeType ==
 * null) { mimeType = "application/octet-stream"; // Mặc định nếu không biết
 * kiểu } resp.setContentType(mimeType);
 * 
 * // 6. THÊM MỚI: Đặt header Content-Length
 * resp.setContentLengthLong(file.length());
 * 
 * // 7. Ghi file ra output stream (dùng try-with-resources để tự động đóng
 * stream) try (FileInputStream inStream = new FileInputStream(file);
 * OutputStream outStream = resp.getOutputStream()) {
 * 
 * byte[] buffer = new byte[4096]; int bytesRead; while ((bytesRead =
 * inStream.read(buffer)) != -1) { outStream.write(buffer, 0, bytesRead); } } }
 * }
 */

/*
 * @WebServlet(urlPatterns= {"/image"}) //fname: abc.png public class
 * DownloadImageController extends HttpServlet {
 * 
 * @Override protected void doGet(HttpServletRequest req, HttpServletResponse
 * resp) throws ServletException, IOException { String fileName =
 * req.getParameter("fname");
 * 
 * // Lấy thư mục uploads từ webapp thực tế String uploadPath =
 * req.getServletContext().getRealPath("/uploads"); File file = new
 * File(uploadPath, fileName);
 * 
 * if(!file.exists()) { resp.sendError(HttpServletResponse.SC_NOT_FOUND);
 * return; }
 * 
 * // Xác định Content-Type theo phần mở rộng if(fileName.endsWith(".jpg") ||
 * fileName.endsWith(".jpeg")) { resp.setContentType("image/jpeg"); } else
 * if(fileName.endsWith(".png")) { resp.setContentType("image/png"); } else
 * if(fileName.endsWith(".webp")) { resp.setContentType("image/webp"); } else {
 * resp.setContentType("application/octet-stream"); // file khác }
 * 
 * // Ghi file ra output stream try (FileInputStream fis = new
 * FileInputStream(file)) { IOUtils.copy(fis, resp.getOutputStream()); } } }
 */


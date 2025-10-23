package com.uteshop.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/guide")
public class GuideServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String mdUrl = "https://raw.githubusercontent.com/DuyVo-2005/Public-material/master/user_guide.md";
;
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new URL(mdUrl).openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        req.setAttribute("markdownContent", content.toString());
        req.getRequestDispatcher("/views/guide.jsp").forward(req, resp);
    }
}

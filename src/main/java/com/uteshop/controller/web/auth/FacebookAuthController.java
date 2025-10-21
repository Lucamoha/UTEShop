package com.uteshop.controller.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.uteshop.configs.AppConfig;
import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.Branches;
import com.uteshop.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {"/login/oauth/facebook",
                           "/login/oauth/facebook/callback"})
public class FacebookAuthController extends HttpServlet {
    String clientId = AppConfig.get("facebook.clientId");
    String clientSecret = AppConfig.get("facebook.clientSecret");
    String redirectUri = AppConfig.get("facebook.redirectUri");
    String scope = AppConfig.get("facebook.scope");

    private final ObjectMapper om = new ObjectMapper();
    EntityDaoImpl<Users> usersEntityDao = new EntityDaoImpl<>(Users.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.equals("/login/oauth/facebook")) {
            var service = new ServiceBuilder(clientId)
                    .apiSecret(clientSecret)
                    .defaultScope(scope)
                    .callback(redirectUri)
                    .build(FacebookApi.instance());

            String state = java.util.UUID.randomUUID().toString();
            req.getSession().setAttribute("oauth_state_facebook", state);
            req.getSession().setAttribute("redirect", req.getParameter("redirect"));

            resp.sendRedirect(service.getAuthorizationUrl(state));
        }
        else if (path.equals("/login/oauth/facebook/callback")) {
            String code  = req.getParameter("code");
            String state = req.getParameter("state");

            String expected = (String) req.getSession().getAttribute("oauth_state_facebook");
            req.getSession().removeAttribute("oauth_state_facebook");
            if (expected == null || !expected.equals(state)) {
                resp.sendError(400, "Invalid state");
                return;
            }

            var service = new ServiceBuilder(clientId)
                    .apiSecret(clientSecret)
                    .defaultScope(scope)
                    .callback(redirectUri)
                    .build(FacebookApi.instance());

            try {
                var accessToken = service.getAccessToken(code);

                var reqProfile = new OAuthRequest(Verb.GET,
                        "https://graph.facebook.com/v24.0/me?fields=id,name,email");
                service.signRequest(accessToken, reqProfile);
                var res = service.execute(reqProfile);
                Map<String, Object> me = om.readValue(res.getBody(), Map.class);

                String id = String.valueOf(me.get("id"));
                String name = (String) me.get("name");
                String email = (String) me.get("email");

                if (email == null) {
                    throw new Exception("Vui lòng liên kết email");
                }

                Users user = usersEntityDao.findByUnique("Email", email).orElse(null);

                if (user == null) {
                    user = new Users();
                    user.setEmail(email);
                    user.setFullName(name != null ? name: "User Facebook");
                    user.setUserRole(AppConfig.get("users.role.user"));
                    user.setPasswordHash(id);
                    usersEntityDao.insert(user);
                }

                String role = user.getUserRole() != null ? user.getUserRole() : AppConfig.get("users.role.user");
                String token = JWTUtil.generateToken(user.getEmail(), role);
                JWTUtil.addTokenToCookie(resp, token, true);

                req.getSession().setAttribute("user", user);
                req.getSession().setAttribute("email", user.getEmail());
                req.getSession().setAttribute("role", role);

                if ("MANAGER".equals(role)) {
                    EntityDaoImpl<Branches> branchesEntityDaoImpl = new EntityDaoImpl<>(Branches.class);
                    Branches branches = branchesEntityDaoImpl.findByUnique("manager", user).orElse(null);
                    if (branches != null) {
                        req.getSession().setAttribute("branchId", branches.getId());
                        req.getSession().setAttribute("branchName", branches.getName());
                    }
                }

                String redirectUrl = (String) req.getSession().getAttribute("redirect");
                req.getSession().removeAttribute("redirect");

                if (redirectUrl != null && !redirectUrl.isEmpty() && redirectUrl.startsWith(req.getContextPath())) {
                    resp.sendRedirect(redirectUrl);
                } else {
                    if ("ADMIN".equals(role)) {
                        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                    } else if ("MANAGER".equals(role)) {
                        // Nếu branch chưa set hoặc null, chuyển về home
                        Object branchId = req.getSession().getAttribute("branchId");
                        if (branchId == null) {
                            resp.sendRedirect(req.getContextPath() + "/home");
                        } else {
                            resp.sendRedirect(req.getContextPath() + "/manager/dashboard");
                        }
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/home");
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                req.setAttribute("error", "Đăng nhập bằng Facebook thất bại: " + ex.getMessage());
                req.getRequestDispatcher("/views/web/login.jsp").forward(req, resp);
            }
        }
    }
}
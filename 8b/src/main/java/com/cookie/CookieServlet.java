package com.cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("username");

        // ✅ handle empty input
        if (name == null || name.trim().isEmpty()) {
            name = "Guest";
        }

        int count = 1;

        Cookie[] cookies = request.getCookies();
        Cookie visitCookie = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("visitCount")) {
                    visitCookie = c;
                }
            }
        }

        if (visitCookie != null) {
            count = Integer.parseInt(visitCookie.getValue());
            count++;
        }

        // ✅ IMPORTANT FIX: encode value before storing
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());

        Cookie nameCookie = new Cookie("username", encodedName);
        Cookie countCookie = new Cookie("visitCount", String.valueOf(count));

        nameCookie.setMaxAge(60);
        countCookie.setMaxAge(60);

        response.addCookie(nameCookie);
        response.addCookie(countCookie);

        out.println("<html><body>");
        out.println("<h2>Welcome back " + name + "!</h2>");
        out.println("<h3>You have visited this page " + count + " times.</h3>");

        out.println("<h3>Cookie List:</h3>");

        Cookie[] allCookies = request.getCookies();

        if (allCookies != null) {
            for (Cookie c : allCookies) {

                String value = c.getValue();

                // ✅ decode username cookie
                if (c.getName().equals("username")) {
                    value = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
                }

                out.println("<p>" + c.getName() + " = " + value + "</p>");
            }
        }

        out.println("<p><b>Cookie Expiry:</b> 60 seconds</p>");
        out.println("</body></html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");
    }
}
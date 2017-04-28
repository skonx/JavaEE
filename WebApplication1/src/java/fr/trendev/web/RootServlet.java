/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
@WebServlet(name = "RootServlet", urlPatterns = {"/index.html"})
public class RootServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response, String msg)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RootServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Servlet RootServlet at " + request.getContextPath()
                    + "</h2>");
            out.println(msg);
            out.println("<a href=\"dashboard.html\">Try to click here</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Principal p = request.getUserPrincipal();
        if (p == null) {
            processRequest(request, response,
                    "<h1>I'm sorry but you are not authenticated !</h1>");
        } else {
            if ("jsie".equals(p.getName())) {
                request.getRequestDispatcher("/dashboard.html").include(request,
                        response);
            } else {
                processRequest(request, response,
                        "<h1>I'm sorry " + p.getName()
                        + " but you're not allowed to access to this resource through this link</h1>");
            }
        }

    }

}

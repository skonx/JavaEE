/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.MyBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@WebServlet(name = "MyServletTest", urlPatterns = {"/test"})
public class MyServletTest extends HttpServlet {

    private static int count = 0;

    private final String MYBEAN = "myBean";

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

        HttpSession session = request.getSession(false);

        MyBean mb = (MyBean) session.getAttribute(MYBEAN);

        /**
         * Session is killed after 2 minutes. Tuned in web.xml config file.
         */
        if (mb == null) {
            this.log("No bean in session " + session.getId());
            mb = new MyBean();
            count = 0;
            mb.setSvname(this.getServletName());
        }

        mb.setIter(++count);
        this.log("Adding myBean... Iteration #" + mb.getIter());
        session.setAttribute("myBean", mb);
        this.getServletContext().getRequestDispatcher("/WEB-INF/newjsp.jsp").
                forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {
    public Controller() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpContext context = new HttpContext(request, response);
        CommandResult result = CommandManager.findCommand(context).execute(context);
        if (result.isRedirection()) {
            response.sendRedirect(result.getPage());
        } else {
            getServletContext().getRequestDispatcher(result.getPage()).forward(request, response);
        }
    }
}
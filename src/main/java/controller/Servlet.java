package controller;

import common.LoggerLoader;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet extends HttpServlet {
    private static final Logger LOGGER = LoggerLoader.getLogger(Servlet.class);

    public Servlet() {
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
        SessionRequestContent context = new SessionRequestContent(request, response);
        LOGGER.info("New request: " + context.getRequestURLWithQuery());
        CommandResult result = CommandManager.findCommand(context).execute(context);
        LOGGER.info(result);
        if (result.isRedirection()) {
            LOGGER.info("Redirecting to: " + result.getPage());
            response.sendRedirect(result.getPage());
        } else {
            LOGGER.info("Forwarding to: " + result.getPage());
            getServletContext().getRequestDispatcher(result.getPage()).forward(request, response);
        }
        LOGGER.info("End of request process: " + request);
    }
}
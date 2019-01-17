package controller;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Command {
    CommandResult execute(HttpContext context) throws ServletException, IOException;
}

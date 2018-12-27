package controller;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Command {

    CommandResult execute(SessionRequestContent context) throws ServletException, IOException;

}

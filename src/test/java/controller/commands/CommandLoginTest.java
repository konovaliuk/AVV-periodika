package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import services.ServiceFactory;
import services.UserService;

import javax.servlet.ServletException;
import java.io.IOException;

import static common.ResourceManager.*;
import static common.ViewConstants.ATTR_NAME_LANGUAGE;
import static common.ViewConstants.ATTR_NAME_USER;
import static common.ViewConstants.INPUT_USER_LOGIN;
import static common.ViewConstants.INPUT_USER_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class CommandLoginTest {
    private static final String LANGUAGE_UK_UA = "uk-UA";
    private static final String SOME_PASSWORD = "";
    private static final String INCORRECT_LOGIN = "a";
    private static final String CORRECT_LOGIN = "admin";
    @Mock
    private UserService userServiceMock;
    @Mock
    private HttpContext contextMock;
    @Mock
    private User userMock;
    private Command tested = new CommandLogin();

    @Before
    public void setUp() {
        mockStatic(ServiceFactory.class);
        when(ServiceFactory.getUserService()).thenReturn(userServiceMock);
    }

    @Test
    public void returnToLoginPageIfNotValidated() throws ServletException, IOException {
        when(contextMock.getRequestParameter(INPUT_USER_LOGIN)).thenReturn(INCORRECT_LOGIN);
        when(contextMock.getRequestParameter(INPUT_USER_PASSWORD)).thenReturn(SOME_PASSWORD);
        when(contextMock.getSessionAttribute(ATTR_NAME_LANGUAGE)).thenReturn(LANGUAGE_UK_UA);
        CommandResult result = tested.execute(contextMock);
        assertEquals(result.getPage(), RM_VIEW_PAGES.get(URL_LOGIN));
        assertTrue(result.isRedirection());
    }

    @Test
    public void returnToLoginPageIfServeLoginReturnedNull() throws ServletException, IOException {
        when(userServiceMock.serveLogin(notNull())).thenReturn(null);
        when(contextMock.getRequestParameter(INPUT_USER_LOGIN)).thenReturn(CORRECT_LOGIN);
        when(contextMock.getRequestParameter(INPUT_USER_PASSWORD)).thenReturn("");
        when(contextMock.getSessionAttribute(ATTR_NAME_LANGUAGE)).thenReturn(LANGUAGE_UK_UA);
        CommandResult result = tested.execute(contextMock);
        verify(contextMock).setMessageWarning(MESSAGE_LOGIN_ERROR);
        assertEquals(result.getPage(), RM_VIEW_PAGES.get(URL_LOGIN));
        assertTrue(result.isRedirection());
    }

    @Test
    public void returnToLoginPageIfServeLoginSuccess() throws ServletException, IOException {
        when(userServiceMock.serveLogin(notNull())).thenReturn(userMock);
        when(contextMock.getRequestParameter(INPUT_USER_LOGIN)).thenReturn(CORRECT_LOGIN);
        when(contextMock.getRequestParameter(INPUT_USER_PASSWORD)).thenReturn(SOME_PASSWORD);
        when(contextMock.getSessionAttribute(ATTR_NAME_LANGUAGE)).thenReturn(LANGUAGE_UK_UA);
        CommandResult result = tested.execute(contextMock);
        verify(userServiceMock).serveLogin(notNull());
        verify(contextMock).setSessionAttribute(ATTR_NAME_USER, userMock);
        verify(contextMock).setMessageSuccess(MESSAGE_LOGIN_WELCOME);
        assertEquals(result.getPage(), RM_VIEW_PAGES.get(URL_CATALOG));
        assertTrue(result.isRedirection());
    }
}
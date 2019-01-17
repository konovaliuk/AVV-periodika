package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommandResult.class)
public class CommandLogoutTest {
    @Mock
    private HttpContext contextMock;
    private Command tested = new CommandLogout();

    @Test
    public void execute() throws ServletException, IOException {
        mockStatic(CommandResult.class);
        when(CommandResult.redirect(null)).thenReturn(null);
        CommandResult result = tested.execute(contextMock);
        verify(contextMock, times(1)).invalidateSession();
        assertNull(result);
    }
}
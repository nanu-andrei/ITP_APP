import com.example.itp_app.main.ItpAppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.Mockito.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ItpAppControllerTest extends ApplicationTest {

    @InjectMocks
    private ItpAppController controller;

    @Mock
    private FXMLLoader mockLoader;

    @Override
    public void start(Stage stage) throws Exception {
        // Load your root layout here
        Parent mainNode = new FXMLLoader(getClass().getResource("/path/to/your/ItpAppUI.fxml")).load();
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    public void testSwitchToggleChangesThemes() {
        // Assume toggleButton id is "switchButton"
        clickOn("#switchButton");
        verify(controller, times(1)).toggleSwitch();
        assertThat(controller.login.isVisible()).isFalse();
        assertThat(controller.login2.isVisible()).isTrue();
    }

    @Test
    public void testExitButtonClosesStage() {
        // Assume exit button id is "exit"
        clickOn("#exit");
        verify(controller, times(1)).exitButtonAction(any());
    }

    @Test
    public void testLoadContentWhenLoginPressed() {
        // Assume login button id is "login"
        clickOn("#login");
        verify(controller, times(1)).loadContent("login.fxml");
    }
}

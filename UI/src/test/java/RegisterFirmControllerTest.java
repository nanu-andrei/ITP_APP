
import com.example.itp_app.main.RegisterFirmController;
import com.example.itp_app.utilities.Requester;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;
import static org.testfx.assertions.api.Assertions.assertThat;

public class RegisterFirmControllerTest extends ApplicationTest {

    @Mock
    private Requester mockRequester;

    private RegisterFirmController controller;

    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.initMocks(this);

        // Load the FXML and set the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/RegisterFirmUI.fxml"));
        loader.setControllerFactory(cls -> {
            if (cls == RegisterFirmController.class) {
                controller = new RegisterFirmController();
                controller.requester = mockRequester; // Use mocked Requester
                return controller;
            } else {
                try {
                    return cls.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Parent mainNode = loader.load();
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setUp() {
        // Prepare responses or any other necessary setup before each test
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("{\"response\": \"Success\"}");
        when(mockResponse.statusCode()).thenReturn(200);
        doAnswer(invocation -> {
            ((RegisterFirmController) invocation.getArgument(2)).handleResponse(mockResponse);
            return null;
        }).when(mockRequester).sendPostRequest(anyString(), anyString(), any());
    }

    @Test
    public void testAddEmployee() {
        // Simulate adding an employee
        clickOn("#employeeUsernameField").write("newuser");
        clickOn("#roleComboBox").clickOn("ADMIN");
        clickOn("#addButton");
        assertThat(controller.employees).hasSize(1);
        assertThat(controller.firmAdminLabel.getText()).isEqualTo("newuser");
    }

    @Test
    public void testFirmRegistration() {
        // Trigger registration
        clickOn("#firmCifField").write("CIF123");
        clickOn("#registerButton");

        // Check interactions
        verify(mockRequester, times(1)).sendPostRequest(anyString(), anyString(), any());
    }
}

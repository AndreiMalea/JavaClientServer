package Controller;

import Domain.Employee;
import Observer.ServiceInterface;
import Service.Service;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import networking.ClientProxy;

import java.util.Properties;

public class LoginController {
    private Stage originalStage;
    private static ServiceInterface srv;
    private Properties prop;

    public void init(Properties prop, Stage stage) {
//    public void init(Stage stage) {
        this.prop = prop;
        originalStage = stage;
        srv = new ClientProxy(prop.getProperty("host"), Integer.parseInt(prop.getProperty("port")));
        originalStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                originalStage.close();
                srv.close();
            }
        });
    }
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;

    public void login() {
        try {
            if (userField.getText().equals("") && passwordField.getText().equals("")) {
                throw new Exception("Both fields are empty, you must complete them!");
            } else if (userField.getText().equals("")) {
                throw new Exception("Username field is empty!");
            } else if (passwordField.getText().equals("")) {
                throw new Exception("Password field is empty!");
            }

            try {
                if (srv.login(userField.getText().trim(), passwordField.getText().trim())!=null) {
                    System.out.println("Login reusit!");

                    Employee employee = srv.employeeByUser(userField.getText());

                    userField.clear();
                    passwordField.clear();

                    Stage primaryStage = new Stage();
                    FXMLLoader msgloader = new FXMLLoader();
                    msgloader.setLocation(getClass().getResource("/views/EmployeeMenu.fxml"));
                    AnchorPane root = new AnchorPane();
                    try {
                        root = msgloader.load();
                    }
                    catch (javafx.fxml.LoadException e) {
                        e.printStackTrace();
                    }
                    primaryStage.setScene(new Scene(root));
                    EmployeeController controller1 = msgloader.getController();
//                    controller1.init(srv, prop, originalStage, primaryStage, employee);
                    controller1.init(srv,  originalStage, primaryStage, employee);
                    primaryStage.show();

                    originalStage.close();

                } else {
                    throw new Exception("Incorrect crediterials!");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incorrect crediterials...");
                alert.setHeaderText("One or more problems with your crediterials!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed...");
            alert.setHeaderText("One or more problems with your login!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}

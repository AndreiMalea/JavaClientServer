import Controller.LoginController;
import Observer.ServiceInterface;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import networking.ClientProxy;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class GuiInit extends Application {

    private static ClientProxy client;

    private String[] args;

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader msgloader = new FXMLLoader();
        msgloader.setLocation(getClass().getResource("/views/Login.fxml"));
        AnchorPane root = new AnchorPane();
        try {
            root = msgloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));

        Properties prop = new Properties();
        String s = (new File("")).getAbsolutePath();

        try {
            prop.load(new FileReader(s+"/Client/src/main/resources/client.config"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((LoginController)msgloader.getController()).init(prop, primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

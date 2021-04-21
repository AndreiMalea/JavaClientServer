import Service.Service;
import Server.AbstractServer;
import Server.ConcurrentServer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Socket;
import java.util.Properties;

public class ServerMain {
    public static void main(String[] args) {
//        LoginController

        Properties prop = new Properties();
        String s = (new File("")).getAbsolutePath();
        try {
            prop.load(new FileReader(s+"/databases/db.config"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        Service srv = new Service(prop);

        Properties serverprops = new Properties();


        try {
            serverprops.load(new FileReader(s+"/Server/src/main/resources/server.config"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AbstractServer server = new ConcurrentServer(Integer.parseInt(serverprops.get("port").toString()), srv);

        try {
            server.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

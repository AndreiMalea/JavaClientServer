package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {

    private int port;
    private ServerSocket server = null;

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = new ServerSocket(port);

            System.out.println("local socket addr : "+server.getLocalSocketAddress());
            System.out.println("port : "+server.getLocalPort());
            System.out.println();
            System.out.println("inet addr : "+server.getInetAddress());

            System.out.println("Server started...");
            while (true) {

                System.out.println("Waiting for clients...");

                Socket cl = server.accept();
                System.out.println("Client connected...");

                Thread thread = this.getWorkerThread(cl);
                thread.start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.stop();
        }
    }

    protected abstract Thread getWorkerThread(Socket client);

    public void stop(){
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

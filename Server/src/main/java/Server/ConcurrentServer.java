package Server;

import Observer.ServiceInterface;
import Service.Service;
import networking.Worker;

import java.net.Socket;

public class ConcurrentServer extends AbstractServer{
    private final ServiceInterface srv;

    @Override
    protected Thread getWorkerThread(Socket client) {
        Worker w = new Worker(srv, client);
        srv.addObserver(w);
        return new Thread(w);
    }

    public ConcurrentServer(int port, Service srv) {
        super(port);
        this.srv = srv;
    }
}

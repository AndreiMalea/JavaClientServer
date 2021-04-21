package networking;

import Domain.Employee;
import Domain.ShowDTO;
import Observer.ObserverInterface;
import Observer.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Worker implements Runnable, ObserverInterface {
    private final ServiceInterface srv;
    private final Socket conn;
    private Boolean isConnected = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Worker(ServiceInterface  srv, Socket conn) {
        this.srv = srv;
        this.conn = conn;
        System.out.println(srv.getObserverList());
        try {
            in = new ObjectInputStream(conn.getInputStream());
            out = new ObjectOutputStream(conn.getOutputStream());
            out.flush();
            this.isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            srv.removeObserver(this);
            conn.close();
            isConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notified() throws IOException {
        send(new ReloadResponse());
    }

    private Response handle(Request req) throws Exception {
        Response rsp = null;

        if (req instanceof CloseRequest) {
            this.isConnected = false;
        } else if (req instanceof  LoginRequest) {
            Employee loginResult = srv.login( ((LoginRequest) req).getUser() , ((LoginRequest) req).getPass());

            if (loginResult == null) {
                return new Error("Incorrect crediterials!");
            }
            rsp = new LoginResponse(srv.employeeByUser(((LoginRequest) req).getUser()));
        } else if (req instanceof GetAllShowsRequest) {
            List<ShowDTO> lst = new ArrayList<>();
            srv.findAllShows().forEach(e->lst.add(new ShowDTO(e)));
            ShowDTO[] arr = new ShowDTO[lst.size()];
            lst.toArray(arr);
            rsp = new GetAllShowsRespons(arr);
        } else if (req instanceof GetFilteredShowsRequest) {
            List<ShowDTO> lst = new ArrayList<>();
            srv.filterShowsByDate(((GetFilteredShowsRequest) req).getDate()).forEach(e->lst.add(new ShowDTO(e)));
            ShowDTO[] arr = new ShowDTO[lst.size()];
            lst.toArray(arr);
            rsp = new GetFilteredShowsResponse(arr);
        } else if (req instanceof TicketBoughtRequest) {
            TicketBoughtRequest tb = (TicketBoughtRequest)req;
            Boolean resp = srv.buyTicket(tb.getShow(), tb.getTicketNumber(), tb.getClient());
            if (resp) {
                srv.myNotifyAll();
                rsp = new TicketBoughtResponse();
            } else rsp = new Error("No more tickets!");
        } else if (req instanceof GetEmployeeByUserRequest) {
            Employee e = srv.employeeByUser(((GetEmployeeByUserRequest) req).getUser());
            if (e == null) {
                rsp = new Error("User doesn't exist!");
            } else {
                rsp = new GetEmployeeByUserResponse(e);
            }
        }

        return rsp;
    }

    private void send(Response r) throws IOException {
        synchronized (out) {
            out.writeObject(r);
            out.flush();
        }
    }

    @Override
    public void run() {
        while (isConnected) {
            try {
                Object req = in.readObject();
                Response resp = handle((Request) req);
                if (resp!=null)
                    this.send(resp);
                Thread.sleep(500);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                this.isConnected = false;
            } catch (Exception e) {
                System.out.println("logged out");
            }
        }

        this.close();
    }
}

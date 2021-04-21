package networking;

import Domain.Employee;
import Domain.Show;
import Domain.ShowDTO;
import Observer.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientProxy implements ServiceInterface {
    private final String host;
    private final int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final BlockingDeque<Response> responses = new LinkedBlockingDeque<>();
    private Boolean ended;
    private Socket conn;

    public ClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.initConnection();
    }

    private void send(Request req) {
        try {
            out.writeObject(req);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Show> filterShowsByDate(LocalDate date) throws Exception {
        this.send(new GetFilteredShowsRequest(date));
        Response r = this.read();

        if (r instanceof Error) {
            throw new Exception(((Error) r).getMessage());
        }

        ShowDTO[] shows = ((GetFilteredShowsResponse)r).getShows();
        List<Show> lst = new ArrayList<>();

        Arrays.asList(shows).forEach(e-> {
            lst.add(ShowDTO.parseShow(e));
        });

        return lst;
    }

    @Override
    public Iterable<Show> findAllShows() throws Exception {
        this.send(new GetAllShowsRequest());
        Response r = this.read();

        if (r instanceof Error) {
            throw new Exception(((Error) r).getMessage());
        }

        if (r instanceof ReloadResponse) {
            r = this.read();
        }

        ShowDTO[] shows = ((GetAllShowsRespons)r).getShows();
        List<Show> lst = new ArrayList<>();

        Arrays.asList(shows).forEach(e-> {
            lst.add(ShowDTO.parseShow(e));
        });

        return lst;
    }

    @Override
    public Employee employeeByUser(String user) throws Exception {
        this.send(new GetEmployeeByUserRequest(user));
        Response r = this.read();

        if (r instanceof Error) {
            throw new Exception(((Error) r).getMessage());
        }

        return ((GetEmployeeByUserResponse)r).getEmp();
    }

    @Override
    public Employee login(String user, String pass) throws Exception {
        this.send(new LoginRequest(user, pass));
        Response r = this.read();

        if (r instanceof LoginResponse) {
            return ((LoginResponse) r).getEmp();
        } else if (r instanceof Error) {
            this.close();
            throw new Exception(((Error) r).getMessage());
        }

        return null;
    }

    @Override
    public void close() {
        this.send(new CloseRequest());

        ended = true;
        try {
            in.close();
            out.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean buyTicket(Show s, Integer no, String client) throws Exception {
        this.send(new TicketBoughtRequest(client, s, no));
        Response r = this.read();

        if (r instanceof TicketBoughtResponse) {
            return true;
        } else if (r instanceof Error) {
            throw new Exception(((Error) r).getMessage());
        }

        return null;
    }

    private Response read() {
        Response r = null;
        try {
            r = responses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return r;
    }

    private void initConnection() {
        try {
            conn = new Socket(host, port);

            out = new ObjectOutputStream(conn.getOutputStream());
            out.flush();
            in = new ObjectInputStream(conn.getInputStream());

            ended = false;
            Thread t = new Thread(new RThread());
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wrapper() {
        this.myNotifyAll();
    }

    private class RThread implements  Runnable{
        @Override
        public void run() {
            while(!ended) {
                try {
                    Object res = in.readObject();
                    if (res instanceof ReloadResponse) {
                        System.out.println("reload");
                        System.out.println(getObserverList());

                        wrapper();
                    } else {
                        try {
                            responses.put((Response) res);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }
}

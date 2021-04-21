package Observer;

import Domain.Employee;
import Domain.Show;

import java.time.LocalDate;
import java.util.List;

public interface ServiceInterface extends ObservableInterface {

    Iterable<Show> findAllShows() throws Exception;

    Employee employeeByUser(String user) throws Exception;

    Employee login(String user, String pass) throws Exception;

    List<Show> filterShowsByDate(LocalDate date) throws Exception;

    Boolean buyTicket(Show s, Integer no, String client) throws Exception;

    void close();
}

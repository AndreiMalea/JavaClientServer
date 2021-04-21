package Service;

import Domain.*;
import Observer.ServiceInterface;
import Repo.*;
import Interfaces.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class Service implements ServiceInterface {
    private final Properties conn;
    private ArtistRepo artistRepo = null;
    private EmployeeRepo employeeRepo = null;
    private OfficeRepo officeRepo = null;
    private ShowRepo showRepo = null;
    private TransactionDBRepo transactionDBRepo = null;

    public Service(Properties conn) {
        this.conn = conn;
        artistRepo = new ArtistDBRepo(this.conn);
        employeeRepo = new EmployeeDBRepo(this.conn);
        officeRepo = new OfficeDBRepo(this.conn);
        showRepo = new ShowDBRepo(this.conn);
        transactionDBRepo = new TransactionDBRepo(this.conn);
    }

    public synchronized Iterable<Show> findAllShows() {
        return showRepo.findAll();
    }

    public synchronized Employee employeeByUser(String user) {
        return employeeRepo.getEmployeeByUser(user);
    }

    public synchronized Employee login(String user, String pass){
        if (employeeRepo.usernameExists(user)) {
            if (employeeRepo.getPasswordByUser(user).equals(pass)) {
                return employeeRepo.getEmployeeByUser(user);
            } else {
                return null;
            }
        }
        return null;
    }

    public synchronized List<Show> filterShowsByDate(LocalDate date) {
        return showRepo.filterByDate(date);
    }

    public synchronized Boolean buyTicket(Show s, Integer no, String client) throws Exception {
        if (s.getTicketNumber()==0) return false;
        s.decreaseTicketNumber(no);
        transactionDBRepo.save(new Transaction(client, s, LocalDate.now(), no));
        showRepo.update(s);
        return true;
    }

    @Override
    public void close() {

    }

//    public Artist findOneArtist(long id) {
//        return artistRepo.findOne(id);
//    }
//
//    public Iterable<Artist> findAllArtist() {
//        return artistRepo.findAll();
//    }
//
//    public Artist saveArtist(List<String> parameteres) {
//        return artistRepo.save(new Artist(Long.parseLong(parameteres.get(0)), parameteres.get(1), parameteres.get(2)));
//    }
//
//    public Artist deleteArtist(long id) {
//        return artistRepo.delete(id);
//    }
//
//    public Artist updateArtist(List<String> parameteres) {
//        return artistRepo.update(new Artist(Long.parseLong(parameteres.get(0)), parameteres.get(1), parameteres.get(2)));
//    }
//
//    public List<Artist> filterByNameArtist(String name) {
//        return artistRepo.filterByName(name);
//    }
//
//    public List<Artist> filterByGenreArtist(String genre) {
//        return artistRepo.filterByGenre(genre);
//    }
//
//
//    public Employee findOneEmployee(Long id) {
//        return employeeRepo.findOne(id);
//    }
//
//    public Iterable<Employee> findAllEmployees() {
//        return employeeRepo.findAll();
//    }
//
//    public Employee saveEmployee(List<String> parameteres) {
//        return employeeRepo.save(new Employee(Long.parseLong(parameteres.get(0)),
//                parameteres.get(1),
//                parameteres.get(2), new Office(Long.parseLong(parameteres.get(3)),
//                parameteres.get(4)), parameteres.get(5), parameteres.get(6)));
//    }
//
//    public Employee deleteEmployee(long id) {
//        return employeeRepo.delete(id);
//    }
//
//    public Employee updateEmployee(List<String> parameteres) {
//        return employeeRepo.update(new Employee(Long.parseLong(parameteres.get(0)),
//                parameteres.get(1),
//                parameteres.get(2), new Office(Long.parseLong(parameteres.get(3)),
//                parameteres.get(4)), parameteres.get(5), parameteres.get(6)));
//    }
//
//    public List<Employee> filterByNameEmployees(String name) {
//        return employeeRepo.filterByName(name);
//    }
//
//    public List<Employee> filterByPositionEmployees(String position) {
//        return employeeRepo.filterByPosition(position);
//    }
//
//    public List<Employee> filterByOffice(long office) {
//        return employeeRepo.filterByOffice(office);
//    }
//
//
//    public Show findOneShow(long id) {
//        return showRepo.findOne(id);
//    }

//    public Show saveShow(List<String> parameteres) {
//        return showRepo.save(new Show(Long.parseLong(parameteres.get(0)),
//                new Artist(Long.parseLong(parameteres.get(1)),
//                        parameteres.get(2),
//                        parameteres.get(3)),
//                Integer.parseInt(parameteres.get(4)),
//                LocalDate.parse(parameteres.get(5).trim())));
//    }
//
//    public Show deleteShow(Long id) {
//        return showRepo.delete(id);
//    }
//
//    public Show updateShow(List<String> parameteres) {
//        return showRepo.update(new Show(Long.parseLong(parameteres.get(0)),
//                new Artist(Long.parseLong(parameteres.get(1)),
//                        parameteres.get(2),
//                        parameteres.get(3)),
//                Integer.parseInt(parameteres.get(4)),
//                LocalDate.parse(parameteres.get(5).trim())));
//    }
//
//    public List<Show> filterByArtist(List<String> parameteres) {
//        return showRepo.filterByArtist(new Artist(Long.parseLong(parameteres.get(0)),
//                parameteres.get(1),
//                parameteres.get(2)));
//    }
//
//
//    public Office findOneOffice(Long id) {
//        return officeRepo.findOne(id);
//    }
//
//    public Iterable<Office> findAllOffices() {
//        return officeRepo.findAll();
//    }
//
//    public Office saveOffice(List<String> parameteres) {
//        return officeRepo.save(new Office(Long.parseLong(parameteres.get(0)), parameteres.get(1)));
//    }
//
//    public Office deleteOffice(Long id) {
//        return officeRepo.delete(id);
//    }
//
//    public Office updateOffice(List<String> parameteres) {
//        return officeRepo.update(new Office(Long.parseLong(parameteres.get(0)), parameteres.get(1)));
//    }
//
//    public List<Office> filterByLocation(String office) {
//        return officeRepo.filterByLocation(office);
//    }




}
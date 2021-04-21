package Repo;

import Domain.Artist;
import Domain.Show;
import Domain.Validator.ShowValidator;
import Domain.Validator.ValidationException;
import Domain.Validator.Validator;
import Interfaces.ArtistRepo;
import Interfaces.ShowRepo;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShowDBRepo implements ShowRepo {
    private static final Logger log = LogManager.getLogger(ArtistRepo.class);

    private final JdbcUtils dbUtils;
    private Validator<Show> validator = new ShowValidator();

    public ShowDBRepo(Properties props) {
        this.dbUtils = new JdbcUtils(props);
        log.info("Initializing EmployeeDBRepo with properties : {}", props);
    }

    @Override
    public List<Show> filterByDate(LocalDate date) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select S.id, A.id aid, A.name, A.genre, S.ticketNumber, S.date date from Shows S inner join Artists A on A.id = S.artist where S.date=?")) {

            preStm.setString(1,date.toString());
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Show sh = new Show(result.getLong("id"),
                            new Artist(result.getLong("aid"),
                                    result.getString("name"),
                                    result.getString("genre")),
                            result.getInt("ticketNumber"),
                            LocalDate.parse(result.getString("date")));
                    shows.add(sh);
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(shows);
        return shows;
    }

    @Override
    public Show findOne(Long aLong) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Show employee = null;
        try (PreparedStatement preStm = con.prepareStatement("select S.id, A.id aid, A.name, A.genre, S.ticketNumber, S.date date from Shows S inner join Artists A on A.id = S.artist where S.id=?")) {
            preStm.setLong(1, aLong);
            try (ResultSet result = preStm.executeQuery()) {
                employee = new Show(result.getLong("id"),
                        new Artist(result.getLong("aid"),
                                result.getString("name"),
                                result.getString("genre")),
                        result.getInt("ticketNumber"),
                        LocalDate.parse(result.getString("date")));
            } catch (SQLException e) {
                log.error(e);
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(employee);
        return employee;
    }

    @Override
    public Iterable<Show> findAll() {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> employees = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select S.id, A.id aid, A.name, A.genre, S.ticketNumber, S.date date from Shows S inner join Artists A on A.id = S.artist")) {
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {

                    employees.add(new Show(result.getLong("id"),
                            new Artist(result.getLong("aid"),
                                    result.getString("name"),
                                    result.getString("genre")),
                            result.getInt("ticketNumber"),
                            LocalDate.parse(result.getString("date"))));
//                            result.getDate("date")));
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
            e.getStackTrace();
        }
        log.traceExit(employees);
        return employees;
    }

    @Override
    public Show save(Show entity) {
        validator.Validate(entity);

        Show office = this.findOne(entity.getId());
        if (office != null) return entity;

        int result = -1;
        log.traceEntry("saving Show {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into Shows (id, artist, ticketNumber, date) values (?,?,?,?)")) {
            preStm.setLong(1, entity.getId());
            preStm.setLong(2, entity.getArtist().getId());
            preStm.setInt(3, entity.getTicketNumber());
            preStm.setString(4, entity.getDate().toString());
            result = preStm.executeUpdate();
            con.close();
            log.trace("Saved {} instances", result);
        } catch (SQLException e) {
            log.error(e);
            e.getStackTrace();
        }
        log.traceExit();
        if (result==0) return entity;
        return null;
    }

    @Override
    public Show delete(Long aLong) {
        if (aLong==null)
            throw new ValidationException("Entity doesn't exist");

        Show art = this.findOne(aLong);
        if (art == null) return null;

        int result = -1;
        log.traceEntry("deleting Employee {}", art);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Shows where id=?")) {
            preStm.setLong(1, aLong);
            result = preStm.executeUpdate();
            con.close();
            log.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            log.error(e);
            e.getStackTrace();
        }
        log.traceExit();
        if (result==1) return art;
        return null;
    }

    @Override
    public Show update(Show entity) {
//        if (entity==null) throw new IllegalArgumentException("Entity is null");
        validator.Validate(entity);

        Show office = this.findOne(entity.getId());
        if (office == null) return entity;

        int result = -1;
        log.traceEntry("modifying Office {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update Shows set artist=?, ticketNumber=?, date=? where id=?")) {
            preStm.setLong(1, entity.getArtist().getId());
            preStm.setInt(2, entity.getTicketNumber());
            preStm.setString(3, entity.getDate().toString());
            preStm.setLong(4, entity.getId());

            result = preStm.executeUpdate();
            con.close();
            log.trace("Modified {} instances", result);
        } catch (SQLException e) {
            log.error(e);
            e.getStackTrace();
        }
        log.traceExit();
        if (result==0) return entity;
        return null;
    }

    @Override
    public List<Show> filterByArtist(Artist artist) {
        return null;
    }
}

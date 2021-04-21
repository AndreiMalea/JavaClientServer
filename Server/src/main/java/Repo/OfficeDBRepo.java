package Repo;

import Domain.Office;
import Domain.Validator.OfficeValidator;
import Domain.Validator.ValidationException;
import Domain.Validator.Validator;
import Interfaces.ArtistRepo;
import Interfaces.OfficeRepo;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OfficeDBRepo implements OfficeRepo {
    private static final Logger log = LogManager.getLogger(ArtistRepo.class);

    private final JdbcUtils dbUtils;
    private Validator<Office> validator = new OfficeValidator();

    public OfficeDBRepo(Properties props) {
        this.dbUtils = new JdbcUtils(props);
        log.info("Initializing OfficeDBRepo with properties : {}", props);
    }

    @Override
    public List<Office> filterByLocation(String location) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Office> offices = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  * from Offices where location=?")) {
            preStm.setString(1,location);
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String name = result.getString("location");
                    Office office = new Office(id, location);
                    offices.add(office);
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(offices);
        return offices;
    }

    @Override
    public Office findOne(Long aLong) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Office office = null;
        try (PreparedStatement preStm = con.prepareStatement("select * from Offices where id=?")) {
            preStm.setLong(1, aLong);
            try (ResultSet result = preStm.executeQuery()) {
                office = new Office(result.getLong("id"), result.getString("location"));
            } catch (SQLException e) {
                log.error(e);
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(office);
        return office;
    }

    @Override
    public Iterable<Office> findAll() {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Office> offices = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  * from Offices")) {
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String location = result.getString("location");
                    Office office = new Office(id, location);
                    offices.add(office);
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
            e.getStackTrace();
        }
        log.traceExit(offices);
        return offices;
    }

    @Override
    public Office save(Office entity) {
//        if (entity==null)
//            throw new IllegalArgumentException("Entity is null");
        validator.Validate(entity);

        Office office = this.findOne(entity.getId());
        if (office != null) return entity;

        int result = -1;
        log.traceEntry("saving task {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into Offices (id, location) values (?,?)")) {
            preStm.setLong(1, entity.getId());
            preStm.setString(2, entity.getLocation());

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
    public Office delete(Long aLong) {
        if (aLong==null)
            throw new ValidationException("Entity doesn't exist");

        Office art = this.findOne(aLong);
        if (art == null) return null;

        int result = -1;
        log.traceEntry("deleting Office {}", art);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Offices where id=?")) {
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
    public Office update(Office entity) {
//        if (entity==null) throw new IllegalArgumentException("Entity is null");
        validator.Validate(entity);

        Office office = this.findOne(entity.getId());
        if (office == null) return entity;

        int result = -1;
        log.traceEntry("modifying Office {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update Offices set location=? where id=?")) {
            preStm.setString(1, entity.getLocation());
            preStm.setLong(2, entity.getId());

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
}

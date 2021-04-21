package Repo;

import Domain.Artist;
import Domain.Validator.ArtistValidator;
import Domain.Validator.ValidationException;
import Domain.Validator.Validator;
import Interfaces.ArtistRepo;
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


public class ArtistDBRepo implements ArtistRepo {
    private static final Logger log = LogManager.getLogger(ArtistRepo.class);

    private final JdbcUtils dbUtils;
    private Validator<Artist> validator = new ArtistValidator();

    public ArtistDBRepo(Properties props) {
        dbUtils = new JdbcUtils(props);
        log.info("Initializing ArtistDBRepo with properties : {}", props);
    }

    @Override
    public Artist save(Artist entity) {
//        if (entity==null)
//            throw new IllegalArgumentException("Entity is null");
        validator.Validate(entity);

        Artist art = this.findOne(entity.getId());
        if (art != null) return entity;

        int result = -1;
        log.traceEntry("saving task {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into Artists(id, name, genre) values (?,?,?)")) {
            preStm.setLong(1, entity.getId());
            preStm.setString(2, entity.getName());
            preStm.setString(3, entity.getGenre());

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
    public Artist delete(Long aLong) {
        if (aLong==null)
            throw new ValidationException("Entity doesn't exist");

        Artist art = this.findOne(aLong);
        if (art == null) return null;

        int result = -1;
        log.traceEntry("deleting Artist {}", art);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Artists where id=?")) {
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
    public Artist update(Artist entity) {
//        if (entity==null) throw new IllegalArgumentException("Entity is null");
        validator.Validate(entity);

        Artist art = this.findOne(entity.getId());
        if (art == null) return entity;

        int result = -1;
        log.traceEntry("modifying task {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update Artists set name=?, genre=? where id=?")) {
            preStm.setString(1, entity.getName());
            preStm.setString(2, entity.getGenre());
            preStm.setLong(3, entity.getId());

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
    public Artist findOne(Long aLong) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Artist artist = null;
        try (PreparedStatement preStm = con.prepareStatement("select * from Artists where id=?")) {
            preStm.setLong(1, aLong);
            try (ResultSet result = preStm.executeQuery()) {
                artist = new Artist(result.getLong("id"), result.getString("name"), result.getString("genre"));
            } catch (SQLException e) {
                log.error(e);
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(artist);
        return artist;
    }

    @Override
    public Iterable<Artist> findAll() {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Artist> artists = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  * from Artists")) {
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String name = result.getString("name");
                    String genre = result.getString("genre");
                    Artist art = new Artist(id, name, genre);
                    artists.add(art);
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(artists);
        return artists;
    }

    @Override
    public List<Artist> filterByGenre(String genre) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Artist> artists = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  * from Artists where genre=?")) {
            preStm.setString(1,genre);
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String name = result.getString("name");
                    Artist art = new Artist(id, name, genre);
                    artists.add(art);
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(artists);
        return artists;
    }

    @Override
    public List<Artist> filterByName(String name) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Artist> artists = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  * from Artists where name=?")) {
            preStm.setString(1,name);
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String genre = result.getString("genre");
                    Artist art = new Artist(id, name, genre);
                    artists.add(art);
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(artists);
        return artists;
    }
}

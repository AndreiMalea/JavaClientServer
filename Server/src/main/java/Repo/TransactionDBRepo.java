package Repo;

import Domain.Artist;
import Domain.Show;
import Domain.Transaction;
import Domain.Validator.ValidationException;
import Domain.Validator.Validator;
import Interfaces.ArtistRepo;
import Interfaces.TransactionRepo;
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

public class TransactionDBRepo implements TransactionRepo {
    private static final Logger log = LogManager.getLogger(ArtistRepo.class);
    private final JdbcUtils dbUtils;
    private final Validator<Transaction> validator = new Validator<Transaction>() {
        @Override
        public void Validate(Transaction transaction) {
            if (transaction == null || transaction.getClient()==null || transaction.getTicketNumber()<=0 || transaction.getDate()==null)
                throw new ValidationException("Invalid transaction!");
        }
    };

    public TransactionDBRepo(Properties props) {
        this.dbUtils = new JdbcUtils(props);
        log.info("Initializing TransactionDBRepo with props : {}", props);

        Transaction.initIds(this.findAll());
        System.out.println(Transaction.idList);
    }

    @Override
    public Transaction findOne(Integer integer) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Transaction trans = null;
        try (PreparedStatement preStm = con.prepareStatement("select T.id, T.client, S.id sid, A.id aid, A.name, A.genre, S.ticketNumber stn, S.date sdate, T.ticketNumber, T.date from Transactions T inner join Shows S on S.id = T.show inner join Artists A on A.id = S.artist where T.id=?")) {
            preStm.setLong(1, integer);
            try (ResultSet result = preStm.executeQuery()) {
                if (result.next()) {
                    trans = new Transaction(result.getInt("id"),
                            result.getString("client"),

                            new Show(result.getLong("sid"),
                                    new Artist(result.getLong("aid"),
                                            result.getString("name"),
                                            result.getString("genre")),
                                    result.getInt("stn"),
                                    LocalDate.parse(result.getString("sdate"))),

                            LocalDate.parse(result.getString("date")),

                            result.getInt("ticketNumber")
                    );
                }
            } catch (SQLException e) {
                log.error(e);
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(trans);
        return trans;
    }

    @Override
    public Iterable<Transaction> findAll() {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Transaction> transactions = new ArrayList<>();
//        try (PreparedStatement preStm = con.prepareStatement("select S.id, A.id aid, A.name, A.genre, S.ticketNumber, S.date date from Shows S inner join Artists A on A.id = S.artist")) {
        try (PreparedStatement preStm = con.prepareStatement("select T.id, T.client, S.id sid, A.id aid, A.name, A.genre, S.ticketNumber stn, S.date sdate, T.ticketNumber, T.date from Transactions T inner join Shows S on S.id = T.show inner join Artists A on A.id = S.artist")) {
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {

                    transactions.add(
                            new Transaction(result.getInt("id"),
                                    result.getString("client"),

                                    new Show(result.getLong("sid"),
                                            new Artist(result.getLong("aid"),
                                                    result.getString("name"),
                                                    result.getString("genre")),
                                            result.getInt("stn"),
                                            LocalDate.parse(result.getString("sdate"))),

                                    LocalDate.parse(result.getString("date")),

                                    result.getInt("ticketNumber")
                            ));
                }
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
            e.getStackTrace();
        }
        log.traceExit(transactions);
        return transactions;
    }

    @Override
    public Transaction save(Transaction entity) {
        validator.Validate(entity);

        Transaction trans = this.findOne(entity.getId());
        if (trans != null) return entity;

        int result = -1;
        log.traceEntry("saving Transaction {}", entity);
        Connection con = dbUtils.getConnection();
//        try (PreparedStatement preStm = con.prepareStatement("insert into Shows (id, artist, ticketNumber, date) values (?,?,?,?)")) {
        try (PreparedStatement preStm = con.prepareStatement("insert into Transactions (id, client, show, date, ticketNumber) VALUES (?,?,?,?,?)")) {
            preStm.setInt(1, entity.getId());
            preStm.setString(2, entity.getClient());
            preStm.setLong(3, entity.getShow().getId());
            preStm.setString(4, entity.getDate().toString());
            preStm.setInt(5, entity.getTicketNumber());
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
    public Transaction delete(Integer integer) {
        if (integer==null)
            throw new ValidationException("Entity doesn't exist");

        Transaction trans = this.findOne(integer);
        if (trans == null) return null;

        int result = -1;
        log.traceEntry("deleting Transaction {}", trans);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Transactions where id=?")) {
            preStm.setInt(1, integer);
            result = preStm.executeUpdate();
            con.close();
            log.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            log.error(e);
            e.getStackTrace();
        }
        log.traceExit();
        if (result==1) return trans;
        return null;
    }

    @Override
    public Transaction update(Transaction entity) {
        validator.Validate(entity);

        Transaction trans = this.findOne(entity.getId());
        if (trans == null) return entity;

        int result = -1;
        log.traceEntry("modifying Transaction {}", entity);
        Connection con = dbUtils.getConnection();
//        try (PreparedStatement preStm = con.prepareStatement("update Shows set artist=?, ticketNumber=?, date=? where id=?")) {
        try (PreparedStatement preStm = con.prepareStatement("update Transactions set ticketNumber=?, client=?, show=?, date=? where id=?")) {
//            preStm.setLong(1, entity.getArtist().getId());
//            preStm.setInt(2, entity.getTicketNumber());
//            preStm.setString(3, entity.getDate().toString());
//            preStm.setLong(4, entity.getId());

            preStm.setInt(1, entity.getTicketNumber());
            preStm.setString(2, entity.getClient());
            preStm.setLong(3, entity.getShow().getId());
            preStm.setString(4, entity.getDate().toString());
            preStm.setInt(5, entity.getId());

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

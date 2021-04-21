package Repo;

import Domain.Employee;
import Domain.Office;
import Domain.Validator.EmployeeValidator;
import Domain.Validator.ValidationException;
import Domain.Validator.Validator;
import Interfaces.ArtistRepo;
import Interfaces.EmployeeRepo;
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

public class EmployeeDBRepo implements EmployeeRepo {

    private static final Logger log = LogManager.getLogger(ArtistRepo.class);

    private final JdbcUtils dbUtils;
    private Validator<Employee> validator = new EmployeeValidator();

    public EmployeeDBRepo(Properties props) {
        this.dbUtils = new JdbcUtils(props);
        log.info("Initializing EmployeeDBRepo with properties : {}", props);
    }

    @Override
    public Employee getEmployeeByUser(String user) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Employee employee = null;
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where E.username=?")) {
            preStm.setString(1, user);
            try (ResultSet result = preStm.executeQuery()) {
                employee = new Employee(result.getLong("id"),
                        result.getString("name"),
                        result.getString("position"),
                        new Office(result.getLong("office"), result.getString("location")),
                        result.getString("username"),
                        result.getString("password"));
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
    public String getPasswordByUser(String user) {
        if (!usernameExists(user)) return null;
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        String pass = null;
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where E.username=?")) {
            preStm.setString(1, user);
            try (ResultSet result = preStm.executeQuery()) {
                pass = result.getString("password");
            } catch (SQLException e) {
                log.error(e);
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(pass);
        return pass;
    }

    public Boolean usernameExists(String user) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Boolean res=false;
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where E.username=?")) {
            preStm.setString(1, user);
            try (ResultSet result = preStm.executeQuery()) {

                if (result.next()) {
                    res = true;
                }
            } catch (SQLException e) {
                log.error(e);
            }
            con.close();
        } catch (SQLException e) {
            log.error(e);
            System.err.println("Error DB " + e);
        }
        log.traceExit(res);

        return res;
    }

    @Override
    public List<Employee> filterByPosition(String position) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> offices = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where position=?")) {
            preStm.setString(1,position);
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    offices.add(new Employee(result.getLong("id"),
                            result.getString("name"),
                            result.getString("position"),
                            new Office(result.getLong("office"), result.getString("location")),
                            result.getString("username"),
                            result.getString("password")));
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
    public List<Employee> filterByOffice(Long off) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> offices = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where office=?")) {
            preStm.setLong(1,off);
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    offices.add(new Employee(result.getLong("id"),
                            result.getString("name"),
                            result.getString("position"),
                            new Office(result.getLong("office"), result.getString("location")),
                            result.getString("username"),
                            result.getString("password")));
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
    public List<Employee> filterByName(String name) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> offices = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where name=?")) {
            preStm.setString(1,name);
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    offices.add(new Employee(result.getLong("id"),
                            result.getString("name"),
                            result.getString("position"),
                            new Office(result.getLong("office"), result.getString("location")),
                            result.getString("username"),
                            result.getString("password")));
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
    public Employee findOne(Long aLong) {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        Employee employee = null;
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office where E.id=?")) {
            preStm.setLong(1, aLong);
            try (ResultSet result = preStm.executeQuery()) {
                employee = new Employee(result.getLong("id"),
                        result.getString("name"),
                        result.getString("position"),
                        new Office(result.getLong("office"), result.getString("location")),
                        result.getString("username"),
                        result.getString("password"));
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
    public Iterable<Employee> findAll() {
        log.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement preStm = con.prepareStatement("select  E.id, name, position, office, location, username, password  from Employees E inner join Offices O on O.id = E.office")) {
            try (ResultSet result = preStm.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String name = result.getString("name");
                    String position = result.getString("position");
                    Employee employee = new Employee(id, name, position,
                            new Office(result.getLong("office"), result.getString("location")),
                            result.getString("username"),
                            result.getString("password"));
                    employees.add(employee);
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
    public Employee save(Employee entity) {
//        if (entity==null)
//            throw new IllegalArgumentException("Entity is null");
        validator.Validate(entity);
        if (usernameExists(entity.getUsername())) {
            throw new ValidationException("Username already in use!");
        }
        Employee office = this.findOne(entity.getId());
        if (office != null) return entity;

        int result = -1;
        log.traceEntry("saving Employee {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into Employees (id, name, position, office) values (?,?,?,?)")) {
            preStm.setLong(1, entity.getId());
            preStm.setString(2, entity.getName());
            preStm.setString(3, entity.getPosition());
            preStm.setLong(4, entity.getOffice().getId());

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
    public Employee delete(Long aLong) {
        if (aLong==null)
            throw new ValidationException("Entity doesn't exist");

        Employee art = this.findOne(aLong);
        if (art == null) return null;

        int result = -1;
        log.traceEntry("deleting Employee {}", art);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Employees where id=?")) {
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
    public Employee update(Employee entity) {
        validator.Validate(entity);

        if (usernameExists(entity.getUsername())) {
            throw new ValidationException("Username already in use!");
        }

        Employee office = this.findOne(entity.getId());
        if (office == null) return entity;

        int result = -1;
        log.traceEntry("modifying Office {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update Employees set name=?, position=?, office=? where id=?")) {
            preStm.setString(1, entity.getName());
            preStm.setString(2, entity.getPosition());
            preStm.setLong(3, entity.getOffice().getId());
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
}

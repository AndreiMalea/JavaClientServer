package Interfaces;

import Domain.Employee;

import java.util.List;

public interface EmployeeRepo extends Repository<Long, Employee>{
    Employee getEmployeeByUser(String user);
    String getPasswordByUser(String user);
    Boolean usernameExists(String user);
    List<Employee> filterByPosition(String position);
    List<Employee> filterByOffice(Long off);
    List<Employee> filterByName(String name);

}

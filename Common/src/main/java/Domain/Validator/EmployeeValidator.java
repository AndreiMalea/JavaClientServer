package Domain.Validator;

import Domain.Employee;

public class EmployeeValidator implements Validator<Employee> {
    @Override
    public void Validate(Employee employee) {
        if (employee==null || employee.getId() == null || employee.getName() == null || employee.getOffice() == null || employee.getPosition() == null) {
            throw new ValidationException("Invalid employee!");
        }
    }
}

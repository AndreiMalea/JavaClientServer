package networking;

import Domain.Employee;

public class GetEmployeeByUserResponse implements Response{
    private Employee emp;

    public GetEmployeeByUserResponse(Employee emp) {
        this.emp = emp;
    }

    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }
}

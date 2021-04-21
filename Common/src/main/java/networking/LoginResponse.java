package networking;

import Domain.Employee;

public class LoginResponse implements Response{
    private final Employee emp;

    public LoginResponse(Employee emp) {
        this.emp = emp;
    }

    public Employee getEmp() {
        return emp;
    }
}

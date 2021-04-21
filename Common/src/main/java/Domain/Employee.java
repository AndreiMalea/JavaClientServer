package Domain;

public class Employee extends Entity<Long>{
    private String name;
    private String position;
    private Office office;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee(Long id, String name, String position, Office office, String username, String password) {
        this.setId(id);
        this.name = name;
        this.position = position;
        this.office = office;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Override
    public String toString() {
        return "Employee{ id=" + this.getId() +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", office=" + office.toString() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

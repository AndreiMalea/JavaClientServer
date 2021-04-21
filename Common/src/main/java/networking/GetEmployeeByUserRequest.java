package networking;

public class GetEmployeeByUserRequest implements Request{
    private String user;

    public GetEmployeeByUserRequest(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

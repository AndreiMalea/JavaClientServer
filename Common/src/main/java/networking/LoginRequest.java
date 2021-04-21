package networking;

public class LoginRequest implements Request{
    private final String user;
    private final String pass;

    public LoginRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}

package networking;

public class Error implements Response{
    private final String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package pro.sky.telegrambot.model;

public class ErrorDetails {
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorDetails(String message) {
        this.message = message;
    }
}

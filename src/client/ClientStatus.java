package client;

import main.User;

public class ClientStatus {
    private User user;
    private String status;

    public ClientStatus(User user, String status) {
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }
}

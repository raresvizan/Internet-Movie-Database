package org.example;

import java.time.LocalDateTime;

public class Request {
    private RequestType type;
    private LocalDateTime date;
    String titleOrActor;
    String description;
    String userRequest;
    String userResolve;

    public Request(RequestType type, LocalDateTime date, String titleOrActor, String description, String userRequest,
                   String userResolve) {
        this.type = type;
        this.date = date;
        this.titleOrActor = titleOrActor;
        this.description = description;
        this.userRequest = userRequest;
        this.userResolve = userResolve;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

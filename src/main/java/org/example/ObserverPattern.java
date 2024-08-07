package org.example;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String notification);
}

interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String notification);
}

class Notification implements Subject {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void rejectedRequest(){
        notifyObservers("Your request has been rejected.");
    }

    public void solvedRequest(){
        notifyObservers("Your request has been solved.");
    }

    public void reviewProduction(){
        notifyObservers("A production that you reviewed, received another review.");
    }

    public void receivedRequest(){
        notifyObservers("You received a new request to solve.");
    }

    public void reviewProdAdded(){
        notifyObservers("A production that you added, received a new review");
    }

    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}

class UserObserver implements Observer {
    private String username;

    public UserObserver(String username) {
        this.username = username;
    }

    public void update(String notification) {
        for (User user : IMDB.getInstance().users){
            if(user.username.equals(username)){
                user.notifications.add(notification);
            }
        }
    }
}

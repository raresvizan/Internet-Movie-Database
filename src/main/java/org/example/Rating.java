package org.example;

public class Rating {
    String user;
    Long rating;
    String comments;
    public Rating(String user, Long rating, String comments) {
        this.user = user;
        this.rating = rating;
        this.comments = comments;
    }
    public void displayInfo() {
        System.out.println("    -"+user +" - "+ rating+" - " + comments);
    }
}

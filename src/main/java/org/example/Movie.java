package org.example;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Movie extends Production{
    String duration;
    Long releaseYear;

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings,
                 String subject, Double totalRating, String duration, Long releaseYear) {
        super(title, directors, actors, genres, ratings, subject, totalRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }
    @Override
    public void displayInfo() {
        System.out.println("Type: Movie");
        System.out.println("Title: " + super.title);
        System.out.println("Directors: " + super.directors);
        System.out.println("Actors: " + super.actors);
        System.out.println("Genres: " + super.genres);
        System.out.println("Subject: " + super.subject);
        System.out.println("Rating: " + super.totalRating);
        System.out.println("Ratings: ");

        Comparator<Rating> compareXp = Comparator.comparingInt(rating -> findUserXp(rating.user));
        if(super.ratings.isEmpty()){
            System.out.println("No ratings yet");
        } else {
            super.ratings.sort(compareXp.reversed());
            for (Rating rating : super.ratings) {
                rating.displayInfo();
            }
        }

        if (releaseYear != null) {
            System.out.println("Release Year: " + releaseYear);
        }
        if (duration != null) {
            System.out.println("Duration: " + duration);
        }
    }
    private static int findUserXp(String username){
        for (User user : IMDB.getInstance().users) {
            if (user.username.equals(username)) {
                return user.xp;
            }
        }
        return 0;
    }
}

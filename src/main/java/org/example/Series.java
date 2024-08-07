package org.example;

import java.util.*;

public class Series extends Production{
    Long releaseYear;
    Long numberOfSeasons;
    private Map<String, List<Episode>> seasonsMap = new HashMap<>();
    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings,
                  String subject, Double totalRating, Long releaseYear, Long numberOfSeasons, Map<String,
            List<Episode>> seasonsMap) {
        super(title, directors, actors, genres, ratings, subject, totalRating);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.seasonsMap = seasonsMap;
    }

    @Override
    public void displayInfo() {
        System.out.println("Type: Series");
        System.out.println("Title: " + super.title);
        System.out.println("Directors: " + super.directors);
        System.out.println("Actors: " + super.actors);
        System.out.println("Genres: " + super.genres);
        System.out.println("Subject: " + super.subject);
        System.out.println("Rating: " + super.totalRating);
        System.out.println("Ratings: ");
        Comparator<Rating> compareXp = Comparator.comparingInt(rating -> findUserXp(rating.user));
        super.ratings.sort(compareXp.reversed());
        for (Rating rating : super.ratings) {
            rating.displayInfo();
        }
        if(releaseYear!=null){
            System.out.println("Release Year: " + releaseYear);
        }
        if(numberOfSeasons!=null) {
            System.out.println("Number of Seasons: " + numberOfSeasons);
        }
        if(seasonsMap!=null && !seasonsMap.isEmpty()){
            System.out.println("Seasons and Episodes:");
            for (Map.Entry<String, List<Episode>> entry : seasonsMap.entrySet()) {
                System.out.println("    " + entry.getKey() + ":");
                List<Episode> episodes = entry.getValue();
                for (Episode e : episodes) {
                    System.out.println("        " + e.name+" "+e.duration);
                }
            }
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
    public Map<String, List<Episode>> getSeasonsMap() {
        return seasonsMap;
    }
}

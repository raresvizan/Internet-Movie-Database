package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Production implements Comparable<Object> {
    String title;
    List<String> directors = new ArrayList<>();
    List<String> actors = new ArrayList<>();
    List<Genre> genres = new ArrayList<>();
    List<Rating> ratings = new ArrayList<>();
    String subject;
    Double totalRating;

    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres,
                      List<Rating> ratings, String subject, Double totalRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.subject = subject;
        this.totalRating = totalRating;
    }

    public abstract void displayInfo();
    public void calculateRating(){
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.rating;
        }
        totalRating = sum/ratings.size();
        totalRating = Math.round(totalRating * 10.0) / 10.0;
    }
    public int compareByRating(Production p){
        return this.totalRating.compareTo(p.totalRating);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if(o instanceof Production production) {
            return this.title.compareTo(production.title);
        }
        if(o instanceof Actor a){
            return this.title.compareTo(a.name);
        }
        return 0;
    }

}

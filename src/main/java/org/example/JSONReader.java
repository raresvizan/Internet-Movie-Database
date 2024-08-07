package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JSONReader {
    public List<Actor> readActorsFromFile(){
        List<Actor> actorList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String file = "C:\\Users\\rares\\Desktop\\Facultate\\POO\\TemaPOO\\src\\main\\resources\\input\\actors.json";
        try{
            Object obj = parser.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                JSONObject actorObj = (JSONObject) o;
                JSONArray projArray = (JSONArray) actorObj.get("performances");
                Map<String,String> projects = new HashMap<>();
                for (Object ob : projArray) {
                    JSONObject projObj = (JSONObject) ob;
                    projects.put((String) projObj.get("title"),(String) projObj.get("type"));
                }
                Actor actor = new Actor((String) actorObj.get("name"),projects,(String) actorObj.get("biography"));
                actorList.add(actor);
            }
        } catch (Exception e){
            //idk
        }
        return actorList;
    }
    public List<Production> readProductionsFromFile(List<Actor> existingActors){
        List<Production> prodList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String file = "C:\\Users\\rares\\Desktop\\Facultate\\POO\\TemaPOO\\src\\main\\resources\\input\\production.json";
        try{
            Object obj = parser.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                JSONObject prodObj = (JSONObject) o;
                List<String> directors = new ArrayList<>();
                JSONArray dirArray = (JSONArray) prodObj.get("directors");
                for (Object ob : dirArray) {
                    directors.add((String) ob);
                }
                List<String> actors = new ArrayList<>();
                JSONArray actorArray = (JSONArray) prodObj.get("actors");
                for (Object ob : actorArray) {
                    String actorName = (String) ob;
                    boolean found = false;
                    for (Actor a : existingActors) {
                        if (a.name.equals(actorName)){
                            found = true;
                            break;
                        }
                    }
                    if (!found){
                        Map<String,String> projects = new HashMap<>();
                        projects.put((String) prodObj.get("title"),(String) prodObj.get("type"));
                        Actor newActor = new Actor(actorName,projects,null);
                        existingActors.add(newActor);
                        LocalDateTime date = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        date = LocalDateTime.parse(date.format(formatter), formatter);
                        Request request = new Request(RequestType.ACTOR_ISSUE,date,actorName,
                                "Actor information is incomplete.",null,"ADMIN");
                        Admin.RequestHolder.addRequest(request);
                    }
                    actors.add(actorName);
                }
                List<Genre> genres = new ArrayList<>();
                JSONArray genreArray = (JSONArray) prodObj.get("genres");
                for (Object ob : genreArray) {
                    String s = ((String) ob).toUpperCase();
                    genres.add(Genre.valueOf(s));
                }
                List<Rating> ratings = new ArrayList<>();
                JSONArray ratArray = (JSONArray) prodObj.get("ratings");
                for (Object ob : ratArray) {
                    JSONObject rObj = (JSONObject) ob;
                    Rating rating = new Rating((String) rObj.get("username"), (Long) rObj.get("rating"),
                            (String) rObj.get("comment"));
                    ratings.add(rating);
                }
                if (((String) prodObj.get("type")).equals("Movie")) {
                    Movie movie = new Movie((String) prodObj.get("title"), directors, actors, genres, ratings,
                            (String) prodObj.get("plot"), (Double) prodObj.get("averageRating"),
                            (String) prodObj.get("duration"), (Long) prodObj.get("releaseYear"));
                    prodList.add(movie);
                } else if (((String) prodObj.get("type")).equals("Series")) {
                    Map<String, List<Episode>> seasonsMap = new HashMap<>();
                    JSONObject sObj = (JSONObject) prodObj.get("seasons");
                    for (Object ob : sObj.keySet()) {
                        String sName = (String) ob;
                        JSONArray epArray = (JSONArray) sObj.get(sName);
                        List<Episode> episodes = new ArrayList<>();
                        for (Object object : epArray) {
                            JSONObject epObj = (JSONObject) object;
                            String epName = (String) epObj.get("episodeName");
                            String epDuration = (String) epObj.get("duration");
                            Episode episode = new Episode(epName, epDuration);
                            episodes.add(episode);
                        }
                        seasonsMap.put(sName, episodes);
                    }
                    Series series = new Series((String) prodObj.get("title"), directors, actors, genres, ratings,
                            (String) prodObj.get("plot"), (Double) prodObj.get("averageRating"),
                            (Long) prodObj.get("releaseYear"), (Long) prodObj.get("numSeasons"), seasonsMap);
                    prodList.add(series);
                }
            }
        } catch (Exception e){
            //idk
        }
        return prodList;
    }
    public List<User> readUsersFromFile(List<Production> productions, List<Actor> actors){
        List<User> userList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String file = "C:\\Users\\rares\\Desktop\\Facultate\\POO\\TemaPOO\\src\\main\\resources\\input\\accounts.json";
        try{
            Object obj = parser.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                JSONObject userObj = (JSONObject) o;
                User user = makeUser(userObj,productions,actors);
                userList.add(user);
            }
        } catch (Exception e){
            //idk
        }
        return userList;
    }
    private User makeUser(JSONObject userObj,List<Production> productions, List<Actor> actors) {
        JSONObject infoObj = (JSONObject) userObj.get("information");
        JSONObject crdObj = (JSONObject) infoObj.get("credentials");
        Credentials credentials = new Credentials((String) crdObj.get("email"), (String) crdObj.get("password"));
        String date = (String) infoObj.get("birthDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDateTime birthDate = localDate.atStartOfDay();
        User.Information info = new User.Information.InformationBuilder(credentials, (String) infoObj.get("name"))
                .country((String) infoObj.get("country"))
                .age(((Long) infoObj.get("age")).intValue())
                .gender(((String) infoObj.get("gender")).charAt(0))
                .birthDate(birthDate)
                .build();
        String type = ((String) userObj.get("userType")).toUpperCase();
        List<String> notifications = new ArrayList<>();
        JSONArray notifArray = (JSONArray) userObj.get("notifications");
        if(notifArray!=null) {
            for (Object o : notifArray) {
                String s = (String) o;
                notifications.add(s);
            }
        }
        SortedSet<Object> preferences = new TreeSet<>();
        JSONArray favProdArray = (JSONArray) userObj.get("favoriteProductions");
        if(favProdArray!=null) {
            for (Object o : favProdArray) {
                String s = (String) o;
                for (Production p : productions) {
                    if (p.title.equals(s)) {
                        preferences.add(p);
                        break;
                    }
                }
            }
        }
        JSONArray favActorsArray = (JSONArray) userObj.get("favoriteActors");
        if(favActorsArray!=null) {
            for (Object o : favActorsArray) {
                String s = (String) o;
                for (Actor a : actors) {
                    if (a.name.equals(s)) {
                        preferences.add(a);
                        break;
                    }
                }
            }
        }
        SortedSet<Object> contributions = new TreeSet<>();
        JSONArray prodContArray = (JSONArray) userObj.get("productionsContribution");
        if(prodContArray!=null) {
            for (Object o : prodContArray) {
                String s = (String) o;
                for (Production p : productions) {
                    if (p.title.equals(s)) {
                        contributions.add(p);
                        break;
                    }
                }
            }
        }
        JSONArray actorContArray = (JSONArray) userObj.get("actorsContribution");
        if(actorContArray!=null) {
            for (Object o : actorContArray) {
                String s = (String) o;
                for (Actor a : actors) {
                    if (a.name.equals(s)) {
                        contributions.add(a);
                        break;
                    }
                }
            }
        }
        int xp=999999999;
        Object xpObj = userObj.get("experience");
        if(xpObj!=null) {
            if (xpObj instanceof String) {
                xp = Integer.parseInt((String) xpObj);
            } else if (xpObj instanceof Long) {
                xp = ((Long) xpObj).intValue();
            }
        }
        User user = UserFactory.createUser(info,AccountType.valueOf(type),(String) userObj.get("username"), xp,
                notifications,preferences,contributions);
        return user;
    }
    public List<Request> readRequestsFromFile(List<User> users){
        List<Request> requestList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String file = "C:\\Users\\rares\\Desktop\\Facultate\\POO\\TemaPOO\\src\\main\\resources\\input\\requests.json";
        try{
            Object obj = parser.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                JSONObject reqObj = (JSONObject) o;
                String date = (String) reqObj.get("createdDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime createdDate = LocalDateTime.parse(date, formatter);
                String titleOrActor = (String) reqObj.get("movieTitle");
                if(titleOrActor==null){
                    titleOrActor = (String) reqObj.get("actorName");
                }
                RequestType type = RequestType.valueOf((String) reqObj.get("type"));
                Request request = new Request(type, createdDate, titleOrActor,(String) reqObj.get("description"),
                        (String) reqObj.get("username"), (String) reqObj.get("to"));
                requestList.add(request);
                switch (type) {
                    case DELETE_ACCOUNT, OTHERS -> {
                        Admin.RequestHolder.addRequest(request);
                    }
                    case ACTOR_ISSUE, MOVIE_ISSUE -> {
                        for (User u : users) {
                            if (u.username.equals((String) reqObj.get("to"))) {
                                ((Staff) u).requests.add(request);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            //idk
        }
        return requestList;
    }
}

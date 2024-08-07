package org.example;

import java.util.*;

public class Admin extends Staff{
    public Admin(Information info, AccountType type, String username, Integer xp, List<String> notifications,
                 SortedSet<Object> preferences, List<Request> requests, SortedSet<Object> contributions) {
        super(info, type, username, xp, notifications, preferences, requests, contributions);
    }
    public void addUser(String email, String name, AccountType type){
        // generate random number
        Random rand = new Random();
        int rand_int = rand.nextInt(1000);
        String username;
        while(true) {
            username = name.toLowerCase() + "_" + rand_int;
            boolean found = false;
            for (User user : IMDB.getInstance().users) {
                if (user.username.equals(username)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
        }
        String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = rand.nextInt(allChars.length());
            password.append(allChars.charAt(index));
        }
        Credentials credentials = new Credentials(email, password.toString());
        User.Information info = new User.Information.InformationBuilder(credentials, name).build();
        List<String> notifications = new ArrayList<>();
        SortedSet<Object> preferences = new TreeSet<>();
        SortedSet<Object> contributions = new TreeSet<>();
        User user = UserFactory.createUser(info, type,username,0,notifications,preferences,contributions);
        IMDB.getInstance().users.add(user);
        System.out.println(user.info.getCredentials().getPassword());
    }
    public void removeUser(String username){
        for(User u: IMDB.getInstance().users){
            if(u.username.equals(username)){
                for(Object o : ((Staff) u).contributions){
                    for(User user : IMDB.getInstance().users){
                        if(user instanceof Admin admin){
                            admin.contributions.add(o);
                        }
                    }
                }
                IMDB.getInstance().users.remove(u);
                break;
            }
        }
        for(Production p : IMDB.getInstance().productions){
            for(Rating r : p.ratings){
                if(r.user.equals(username)){
                    p.ratings.remove(r);
                    break;
                }
            }
        }

    }
    public static class RequestHolder {
        static List<Request> requests = new ArrayList<>();
        public static void addRequest(Request request){
            requests.add(request);
        }
        public static void removeRequest(Request request){
            requests.remove(request);
        }

    }
}

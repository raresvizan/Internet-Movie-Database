package org.example;

import java.util.List;
import java.util.SortedSet;

public class Contributor extends Staff implements RequestManager{
    public Contributor(Information info, AccountType type, String username, Integer xp, List<String> notifications,
                       SortedSet<Object> preferences, List<Request> requests, SortedSet<Object> contributions) {
        super(info, type, username, xp, notifications, preferences, requests, contributions);
    }
    @Override
    public void createRequest(Request r) {
        IMDB.getInstance().requests.add(r);
        if(r.userResolve.equals("ADMIN")){
            Admin.RequestHolder.addRequest(r);
        } else {
            for (User u : IMDB.getInstance().users) {
                if (u.username.equals(r.userResolve)) {
                    if (u instanceof Contributor contributor) {
                        contributor.requests.add(r);
                    } else {
                        ((Admin) u).requests.add(r);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.getInstance().requests.remove(r);
        if(r.userResolve.equals("ADMIN")){
            Admin.RequestHolder.removeRequest(r);
        } else {
            for (User u : IMDB.getInstance().users) {
                if (u.username.equals(r.userResolve)) {
                    if (u instanceof Contributor contributor) {
                        contributor.requests.remove(r);
                    } else {
                        ((Admin) u).requests.remove(r);
                    }
                    break;
                }
            }
        }
    }
}

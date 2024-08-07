package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class UserFactory {
    public static User createUser(User.Information info, AccountType type, String username, Integer xp,
                                  List<String> notifications, SortedSet<Object> preferences,
                                  SortedSet<Object> contributions) {
        switch (type){
            case CONTRIBUTOR -> {
                List<Request> requests = new ArrayList<>();
                return new Contributor(info, type, username, xp, notifications, preferences, requests, contributions);
            }
            case REGULAR -> {
                return new Regular(info, type, username, xp, notifications, preferences);
            }
            case ADMIN -> {
                List<Request> requests = new ArrayList<>();
                return new Admin(info, type, username, 999999999, notifications, preferences, requests,contributions);
            }
            default -> {
                return null;
            }
        }
    }
}

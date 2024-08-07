package org.example;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IMDB {
    List<User> users = new ArrayList<>();
    List<Actor> actors = new ArrayList<>();
    List<Request> requests = new ArrayList<>();
    List<Production> productions = new ArrayList<>();
    private static IMDB obj = null;
    private IMDB(){

    }
    public static IMDB getInstance(){
        if (obj == null)
            obj = new IMDB();
        return obj;
    }
    public void run() {
        JSONReader reader = new JSONReader();
        actors = reader.readActorsFromFile();
        productions = reader.readProductionsFromFile(actors);
        users = reader.readUsersFromFile(productions,actors);
        requests = reader.readRequestsFromFile(users);

        System.out.println("Welcome to IMDB!");
        System.out.println("How do you want to use the application?");
        System.out.println("    1. Terminal");
        System.out.println("    2. Graphic Interface");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Option: ");
            try {
                switch (scanner.nextLine()) {
                    case "1" -> {
                        runTerminal();
                    }
                    case "2" -> {
                        GUI gui = new GUI();
                        gui.run();
                    }
                    default -> {
                        throw new InvalidCommandException("Invalid command! Please write 1 or 2");
                    }
                }
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void runTerminal() {
        System.out.println("Enter your credentials:");
        Scanner scanner = new Scanner(System.in);
        System.out.print("  Email: ");
        String email = scanner.nextLine();
        System.out.print("  Password: ");
        String password = scanner.nextLine();
        boolean found = false;
        for (User u : users){
            Credentials c = u.info.getCredentials();
            if (c.getEmail().equals(email) && c.getPassword().equals(password)){
                found = true;
                System.out.println("Welcome, " + u.username + "!");
                switch (u.type){
                    case ADMIN -> runAdminTerminal((Admin) u);
                    case CONTRIBUTOR -> runContributorTerminal((Contributor) u);
                    case REGULAR -> runRegularTerminal((Regular) u);
                }
                break;
            }
        }
        if (!found){
            System.out.println("Invalid credentials! Please try again");
            runTerminal();
        }
    }

    private void runRegularTerminal(Regular regular) {
        System.out.println("User experience: " + regular.xp);
        System.out.println("What do you want to do?");
        System.out.println("    1. View productions details");
        System.out.println("    2. View actors details");
        System.out.println("    3. View notifications");
        System.out.println("    4. Search for actor/movie/series");
        System.out.println("    5. Add/Delete actor/movie/series to/from favourites");
        System.out.println("    6. Create/Delete a request");
        System.out.println("    7. Add/Delete rating to/from movie/series");
        System.out.println("    8. Logout");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equals("1") && !option.equals("2") && !option.equals("3") && !option.equals("4") &&
                        !option.equals("5") && !option.equals("6") && !option.equals("7") && !option.equals("8")){
                    throw new InvalidCommandException("Invalid command! Please write a number between 1 and 10");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        switch (option){
            case "1" -> {
                viewProductionsDetails();
            }
            case "2" -> {
                viewActorsDetails();
            }
            case "3" -> {
                viewNotifications(regular);
            }
            case "4" -> {
                searchMovieActorSeries();
            }
            case "5" -> {
                addDeleteFavourites(regular);
            }
            case "6" -> {
                createDeleteRequest(regular);
            }
            case "7" -> {
                addDeleteRating(regular);
            }
            case "8" -> {
                regular.logout();
                runExit();
                return;
            }
        }
        System.out.println("\nPress ENTER to continue");
        scanner.nextLine();
        runRegularTerminal(regular);
    }

    private void runContributorTerminal(Contributor contributor) {
        System.out.println("User experience: " + contributor.xp);
        System.out.println("What do you want to do?");
        System.out.println("    1. View productions details");
        System.out.println("    2. View actors details");
        System.out.println("    3. View notifications");
        System.out.println("    4. Search for actor/movie/series");
        System.out.println("    5. Add/Delete actor/movie/series to/from favourites");
        System.out.println("    6. Create/Delete a request");
        System.out.println("    7. Add/Delete actor/movie/series from system");
        System.out.println("    8. Update actor/movie/series details");
        System.out.println("    9. Solve a request");
        System.out.println("    10. Logout");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equals("1") && !option.equals("2") && !option.equals("3") && !option.equals("4") &&
                        !option.equals("5") && !option.equals("6") && !option.equals("7") && !option.equals("8") &&
                        !option.equals("9") && !option.equals("10")){
                    throw new InvalidCommandException("Invalid command! Please write a number between 1 and 10");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        switch (option){
            case "1" -> {
                viewProductionsDetails();
            }
            case "2" -> {
                viewActorsDetails();
            }
            case "3" -> {
                viewNotifications(contributor);
            }
            case "4" -> {
                searchMovieActorSeries();
            }
            case "5" -> {
                addDeleteFavourites(contributor);
            }
            case "6" -> {
                createDeleteRequest(contributor);
            }
            case "7" -> {
                addDeleteProdActor(contributor);
            }
            case "8" -> {
                updateProdActor(contributor);
            }
            case "9" -> {
                solveRequest(contributor);
            }
            case "10" -> {
                contributor.logout();
                runExit();
                return;
            }
        }
        System.out.println("\nPress ENTER to continue");
        scanner.nextLine();
        runContributorTerminal(contributor);
    }

    private void runAdminTerminal(Admin admin) {
        System.out.println("What do you want to do?");
        System.out.println("    1. View productions details");
        System.out.println("    2. View actors details");
        System.out.println("    3. View notifications");
        System.out.println("    4. Search for actor/movie/series");
        System.out.println("    5. Add/Delete actor/movie/series to/from favourites");
        System.out.println("    6. Add/Delete user");
        System.out.println("    7. Add/Delete actor/movie/series from system");
        System.out.println("    8. Update actor/movie/series details");
        System.out.println("    9. Solve a request");
        System.out.println("    10. Logout");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Option: ");
            try {
                switch (scanner.nextLine()) {
                    case "1" -> {
                        viewProductionsDetails();
                    }
                    case "2" -> {
                        viewActorsDetails();
                    }
                    case "3" -> {
                        viewNotifications(admin);
                    }
                    case "4" -> {
                        searchMovieActorSeries();
                    }
                    case "5" -> {
                        addDeleteFavourites(admin);
                    }
                    case "6" -> {
                        addDeleteUser(admin);
                    }
                    case "7" -> {
                        addDeleteProdActor(admin);
                    }
                    case "8" -> {
                        updateProdActor(admin);
                    }
                    case "9" -> {
                        solveRequest(admin);
                    }
                    case "10" -> {
                        admin.logout();
                        runExit();
                        return;
                    }
                    default -> {
                        throw new InvalidCommandException("Invalid command! Please write a number between 1 and 10");
                    }
                }
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("\nPress ENTER to continue");
        scanner.nextLine();
        runAdminTerminal(admin);
    }

    private void runExit() {
        System.out.println("Do you want to exit the application? (Yes/No)");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equalsIgnoreCase("YES") && !option.equalsIgnoreCase("NO")){
                    throw new InvalidCommandException("Invalid command! Please write Yes or No");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        if(option.equalsIgnoreCase("YES")){
            // save data to files
            /*JSONWriter writer = new JSONWriter();
            writer.writeActorsToFile(actors);
            writer.writeProductionsToFile(productions);
            writer.writeUsersToFile(users);
            writer.writeRequestsToFile(requests);*/
            System.out.println("Goodbye!");
        }
        else{
            System.out.println();
            runTerminal();
        }
    }

    void notificationRequestStatus(String userRequest, boolean solved){
        Notification notification = new Notification();
        UserObserver userObserver = new UserObserver(userRequest);
        notification.addObserver(userObserver);
        if(solved){
            notification.solvedRequest();
        } else{
            notification.rejectedRequest();
        }
    }

    void notificationRequestStaff(String userResolve) {
        Notification notification = new Notification();
        if(userResolve.equals("ADMIN")){
            for(User user : users){
                if(user instanceof Admin a){
                    UserObserver userObserver = new UserObserver(a.username);
                    notification.addObserver(userObserver);
                }
            }
        }
        else{
            UserObserver userObserver = new UserObserver(userResolve);
            notification.addObserver(userObserver);
        }
        notification.receivedRequest();
    }

    void notificationReviewRegular(Production p, String username) {
        Notification notification = new Notification();
        for(Rating r : p.ratings){
            if(!r.user.equals(username)) {
                UserObserver userObserver = new UserObserver(r.user);
                notification.addObserver(userObserver);
            }
        }
        notification.reviewProduction();
    }

    void notificationReviewStaff(Production p) {
        boolean fnd = false;
        for(User user : users){
            if(user instanceof Contributor c){
                for(Object o : c.contributions){
                    if(o instanceof Production pp){
                        if(pp.title.equals(p.title)){
                            Notification notification = new Notification();
                            UserObserver userObserver = new UserObserver(c.username);
                            notification.addObserver(userObserver);
                            notification.reviewProdAdded();
                            fnd = true;
                            break;
                        }
                    }
                }
            } else if(user instanceof Admin a){
                for(Object o : a.contributions){
                    if(o instanceof Production pp){
                        if(pp.title.equals(p.title)){
                            Notification notification = new Notification();
                            UserObserver userObserver = new UserObserver(a.username);
                            notification.addObserver(userObserver);
                            notification.reviewProdAdded();
                            fnd=true;
                            break;
                        }
                    }
                }
            }
            if(fnd){
                break;
            }
        }
    }

    private void createDeleteRequest(User user) {
        System.out.println("Do you want to create or delete a request? (Create/Delete)");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equalsIgnoreCase("CREATE") &&
                        !option.equalsIgnoreCase("DELETE")){
                    throw new InvalidCommandException("Invalid command! Please write Create or Delete");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        if(option.equalsIgnoreCase("CREATE")) {
            System.out.println("Choose a request type:");
            System.out.println("    DELETE_ACCOUNT");
            System.out.println("    ACTOR_ISSUE");
            System.out.println("    MOVIE_ISSUE");
            System.out.println("    OTHERS");
            String type;
            while(true) {
                System.out.print("Request type: ");
                type = scanner.nextLine();
                if (type.equals("DELETE_ACCOUNT") || type.equals("ACTOR_ISSUE") || type.equals("MOVIE_ISSUE") ||
                        type.equals("OTHERS")) {
                    break;
                } else {
                    System.out.println("Invalid request type! Please try again");
                }
            }
            String description;
            while(true) {
                System.out.print("Description: ");
                description = scanner.nextLine();
                if (description.isEmpty()) {
                    System.out.println("Invalid description! Please write something");
                } else {
                    break;
                }
            }
            switch (type) {
                case "DELETE_ACCOUNT" -> {
                    LocalDateTime date = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    date = LocalDateTime.parse(date.format(formatter), formatter);
                    Request r = new Request(RequestType.DELETE_ACCOUNT, date, null, description,
                            user.username, "ADMIN");
                    if(user instanceof Regular regular){
                        regular.createRequest(r);
                    }
                    else{
                        ((Contributor) user).createRequest(r);
                    }
                    notificationRequestStaff("ADMIN");
                }
                case "ACTOR_ISSUE" -> {
                    String actorName = null;
                    for (Actor a : actors) {
                        if (description.contains(a.name)) {
                            actorName = a.name;
                            break;
                        }
                    }
                    if (actorName == null) {
                        System.out.println("Actor not found! Please try again");
                        return;
                    } else {
                        if (user instanceof Contributor contributor) {
                            for (Object o : contributor.contributions) {
                                if (o instanceof Actor a) {
                                    if (a.name.equals(actorName)) {
                                        System.out.println("You cannot create a request for an actor that you added!");
                                        return;
                                    }
                                }
                            }
                        }
                        String userResolve = null;
                        for (User u : users) {
                            if (u instanceof Contributor contributor) {
                                for (Object o : contributor.contributions) {
                                    if (o instanceof Actor a) {
                                        if (a.name.equals(actorName)) {
                                            userResolve = u.username;
                                            break;
                                        }
                                    }
                                }
                            } else if (u instanceof Admin admin) {
                                for (Object o : admin.contributions) {
                                    if (o instanceof Actor a) {
                                        if (a.name.equals(actorName)) {
                                            userResolve = u.username;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        LocalDateTime date = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        date = LocalDateTime.parse(date.format(formatter), formatter);
                        Request r = new Request(RequestType.ACTOR_ISSUE, date, actorName, description, user.username,
                                userResolve);
                        if(user instanceof Regular regular){
                            regular.createRequest(r);
                        }
                        else{
                            ((Contributor) user).createRequest(r);
                        }
                        notificationRequestStaff(userResolve);
                    }
                }
                case "MOVIE_ISSUE" -> {
                    String prodName = null;
                    for (Production p : productions) {
                        if (description.contains(p.title)) {
                            prodName = p.title;
                            break;
                        }
                    }
                    if (prodName == null) {
                        System.out.println("Production not found! Please try again");
                        return;
                    } else {
                        if (user instanceof Contributor contributor) {
                            for (Object o : contributor.contributions) {
                                if (o instanceof Production p) {
                                    if (p.title.equals(prodName)) {
                                        System.out.println("You cannot create a request for a production that you added!");
                                        return;
                                    }
                                }
                            }
                        }
                        String userResolve = null;
                        for (User u : users) {
                            if (u instanceof Contributor contributor) {
                                for (Object o : contributor.contributions) {
                                    if (o instanceof Production p) {
                                        if (p.title.equals(prodName)) {
                                            userResolve = u.username;
                                            break;
                                        }
                                    }
                                }
                            } else if (u instanceof Admin admin) {
                                for (Object o : admin.contributions) {
                                    if (o instanceof Production p) {
                                        if (p.title.equals(prodName)) {
                                            userResolve = u.username;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        LocalDateTime date = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        date = LocalDateTime.parse(date.format(formatter), formatter);
                        Request r = new Request(RequestType.MOVIE_ISSUE, date, prodName, description, user.username,
                                userResolve);
                        if(user instanceof Regular regular){
                            regular.createRequest(r);
                        }
                        else{
                            ((Contributor) user).createRequest(r);
                        }
                        notificationRequestStaff(userResolve);
                    }
                }
                case "OTHERS" -> {
                    LocalDateTime date = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    date = LocalDateTime.parse(date.format(formatter), formatter);
                    Request r = new Request(RequestType.OTHERS, date, null, description, user.username,
                            "ADMIN");
                    if(user instanceof Regular regular){
                        regular.createRequest(r);
                    }
                    else{
                        ((Contributor) user).createRequest(r);
                    }
                    notificationRequestStaff("ADMIN");
                }
            }
            System.out.println("Request created!");
        }
        else{
            System.out.println("Choose a request:");
            boolean exists = false;
            for(Request r : requests){
                if(r.userRequest.equals(user.username)){
                    if(r.titleOrActor == null)
                        System.out.println("    " + r.getType() + ": " +r.getDate() + " | " +r.description);
                    else
                        System.out.println("    " + r.titleOrActor + " - " + r.getType() + ": " +r.getDate() + " | " +r.description);
                    exists = true;
                }
            }
            if(!exists){
                System.out.println("    No requests");
            }
            else{
                LocalDateTime createdDate;
                while(true){
                    System.out.print("Request date: ");
                    String date = scanner.nextLine();
                    try{
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        createdDate = LocalDateTime.parse(date, formatter);
                        boolean found = false;
                        for(Request r : requests) {
                            if(r.getDate().equals(createdDate) && r.userRequest.equals(user.username)) {
                                found = true;
                                if(user instanceof Regular regular){
                                    regular.removeRequest(r);
                                }
                                else{
                                    ((Contributor) user).removeRequest(r);
                                }
                                break;
                            }
                        }
                        if(!found){
                            System.out.println("Request not found! Please try again");
                        }
                        else{
                            break;
                        }
                    } catch (Exception e){
                        System.out.println("Invalid date! Please write a date in the format yyyy-MM-ddTHH:mm:ss");
                    }
                }
                System.out.println("Request deleted!");
            }
        }
    }

    private void addDeleteRating(Regular regular) {
        System.out.println("Do you want to add or delete a rating? (Add/Delete)");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equalsIgnoreCase("ADD") &&
                        !option.equalsIgnoreCase("DELETE")){
                    throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        if(option.equalsIgnoreCase("ADD")){
            System.out.println("Choose a production:");
            for(Production p : productions){
                boolean exists = false;
                for (Rating r : p.ratings){
                    if(r.user.equals(regular.username)){
                        exists = true;
                        break;
                    }
                }
                if(!exists){
                    System.out.println("    - " + p.title);
                }
            }
            while(true) {
                System.out.print("Production name: ");
                String name = scanner.nextLine();
                boolean found = false;
                for (Production p : productions) {
                    for(Rating r : p.ratings) {
                        if(r.user.equals(regular.username) && p.title.equalsIgnoreCase(name)) {
                            System.out.println("You already rated this production!");
                            r.displayInfo();
                            found = true;
                            break;
                        }
                        if (p.title.equalsIgnoreCase(name)) {
                            found = true;
                            System.out.println("Enter rating details:");
                            Long rating;
                            while (true) {
                                System.out.print("  Rating (1-10): ");
                                try {
                                    rating = Long.parseLong(scanner.nextLine());
                                    if (rating < 1 || rating > 10) {
                                        throw new InvalidCommandException("Invalid rating! Please write a number between 1 and 10");
                                    }
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid rating! Please write a number between 1 and 10");
                                } catch (InvalidCommandException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            String comments;
                            while (true) {
                                System.out.print("  Comments: ");
                                comments = scanner.nextLine();
                                if (comments.isEmpty()) {
                                    System.out.println("Invalid comments! Please write something");
                                } else {
                                    break;
                                }
                            }
                            Rating rObj = new Rating(regular.username, rating, comments);
                            p.ratings.add(rObj);
                            regular.addRating();
                            p.calculateRating();
                            System.out.println("Rating added!");
                            notificationReviewRegular(p,regular.username);
                            notificationReviewStaff(p);
                            rObj.displayInfo();
                            break;
                        }
                    }
                    if(found){
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Production not found! Please try again");
                } else {
                    break;
                }
            }
        }
        else{
            System.out.println("Choose a production that you rated:");
            boolean exists = false;
            for(Production p : productions){
                for(Rating r : p.ratings){
                    if(r.user.equals(regular.username)){
                        exists = true;
                        System.out.println("    - " + p.title);
                        break;
                    }
                }
            }
            if (!exists){
                System.out.println("    - No productions");
            }
            else {
                while (true) {
                    System.out.print("Production name: ");
                    String name = scanner.nextLine();
                    boolean found = false;
                    for (Production p : productions) {
                        if (p.title.equalsIgnoreCase(name)) {
                            for (Rating r : p.ratings) {
                                if (r.user.equals(regular.username)) {
                                    p.ratings.remove(r);
                                    regular.removeRating();
                                    p.calculateRating();
                                    System.out.println("Rating removed!");
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Production not found! Please try again");
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void solveRequest(Staff staff) {
        System.out.println("Choose a request:");
        boolean exists = false;
        if(staff instanceof Admin){
            for(Request r : Admin.RequestHolder.requests){
                if(r.titleOrActor == null)
                    System.out.println("    " + r.getType() + ": " +r.getDate() + " | " +r.description);
                else
                    System.out.println("    " + r.titleOrActor + " - " + r.getType() + ": " +r.getDate() + " | " +r.description);
                exists = true;
            }
        }
        for(Request r : staff.requests){
            if(r.titleOrActor == null)
                System.out.println("    " + r.getType() + ": " +r.getDate() + " | " +r.description);
            else
                System.out.println("    " + r.titleOrActor + " - " + r.getType() + ": " +r.getDate() + " | " +r.description);
            exists = true;
        }
        if(!exists){
            System.out.println("    No requests");
        }
        else{
            Scanner scanner = new Scanner(System.in);
            LocalDateTime createdDate;
            while(true){
                System.out.print("Request date: ");
                String date = scanner.nextLine();
                try{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    createdDate = LocalDateTime.parse(date, formatter);
                    boolean found = false;
                    for(Request r : Admin.RequestHolder.requests) {
                        if(r.getDate().equals(createdDate)){
                            System.out.println("Mark as solved or rejected? (Solved/Rejected)");
                            String option;
                            while (true) {
                                try {
                                    System.out.print("Option: ");
                                    option = scanner.nextLine();
                                    if (option.equalsIgnoreCase("SOLVED") ||
                                            option.equalsIgnoreCase("REJECTED")) {
                                        break;
                                    } else {
                                        throw new InvalidCommandException("Invalid command! Please write Solved or Rejected");
                                    }
                                } catch (InvalidCommandException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            if(option.equalsIgnoreCase("SOLVED")) {
                                System.out.println("Request solved!");
                                notificationRequestStatus(r.userRequest,true);
                            }
                            else{
                                System.out.println("Request rejected!");
                                notificationRequestStatus(r.userRequest,false);
                            }
                            for (User user : users) {
                                if (user.username.equals(r.userRequest)) {
                                    if(user instanceof Contributor c){
                                        c.removeRequest(r);
                                    } else{
                                        ((Regular) user).removeRequest(r);
                                    }
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        for(Request r : staff.requests) {
                            if(r.getDate().equals(createdDate)){
                                System.out.println("Mark as solved or rejected? (Solved/Rejected)");
                                String option;
                                while (true) {
                                    try {
                                        System.out.print("Option: ");
                                        option = scanner.nextLine();
                                        if (option.equalsIgnoreCase("SOLVED") ||
                                                option.equalsIgnoreCase("REJECTED")) {
                                            break;
                                        } else {
                                            throw new InvalidCommandException("Invalid command! Please write Solved or Rejected");
                                        }
                                    } catch (InvalidCommandException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                                if(option.equalsIgnoreCase("SOLVED")) {
                                    System.out.println("Request solved!");
                                    for (User user : users) {
                                        if (user.username.equals(r.userRequest)) {
                                            Context context = new Context(new AddRequestXp());
                                            user.xp += context.executeStrategy();
                                            if(user instanceof Contributor c){
                                                c.removeRequest(r);
                                            } else{
                                                ((Regular) user).removeRequest(r);
                                            }
                                            break;
                                        }
                                    }
                                    notificationRequestStatus(r.userRequest,true);
                                }
                                else{
                                    System.out.println("Request rejected!");
                                    for (User user : users) {
                                        if (user.username.equals(r.userRequest)) {
                                            if(user instanceof Contributor c){
                                                c.removeRequest(r);
                                            } else{
                                                ((Regular) user).removeRequest(r);
                                            }
                                            break;
                                        }
                                    }
                                    notificationRequestStatus(r.userRequest,false);
                                }
                                found = true;
                                break;
                            }
                        }
                    }
                    if(!found){
                        System.out.println("Request not found! Please try again");
                    }
                    else{
                        break;
                    }
                } catch (Exception e){
                    System.out.println("Invalid date! Please write a date in the format yyyy-MM-ddTHH:mm:ss");
                }
            }
        }

    }

    private void updateProdActor(Staff staff) {
        System.out.println("What do you want to update?");
        System.out.println("    1. Actor");
        System.out.println("    2. Movie or Series");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equals("1") && !option.equals("2")){
                    throw new InvalidCommandException("Invalid command! Please write 1 or 2");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        if(option.equals("1")){
            System.out.println("Choose an actor:");
            boolean exists = false;
            for (Object o : staff.contributions) {
                if (o instanceof Actor) {
                    System.out.println("    - " + ((Actor) o).name);
                    exists = true;
                }
            }
            if(!exists){
                System.out.println("    - No actors");
            }
            else {
                String option2;
                Actor actor = null;
                while (true) {
                    System.out.print("Actor name: ");
                    String name = scanner.nextLine();
                    boolean found = false;
                    for (Object o : staff.contributions) {
                        if (o instanceof Actor a) {
                            if (a.name.equalsIgnoreCase(name)) {
                                found = true;
                                actor = a;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println("Actor not found! Please try again");
                    } else {
                        break;
                    }
                }
                System.out.println("What do you want to update?");
                System.out.println("    1. Name");
                System.out.println("    2. Biography");
                System.out.println("    3. Projects");
                while (true) {
                    System.out.print("Option: ");
                    try {
                        option2 = scanner.nextLine();
                        if (!option2.equals("1") && !option2.equals("2") && !option2.equals("3")) {
                            throw new InvalidCommandException("Invalid command! Please write 1, 2 or 3");
                        }
                        break;
                    } catch (InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                }
                switch (option2) {
                    case "1" -> {
                        String name;
                        while (true) {
                            System.out.print("New name: ");
                            name = scanner.nextLine();
                            if (!name.matches("[a-zA-Z ]+")) {
                                System.out.println("Invalid name! Please write only letters and spaces (ex: John Doe)");
                            } else {
                                break;
                            }
                        }
                        actor.name = name;
                        System.out.println("Name updated!");
                    }
                    case "2" -> {
                        String biography;
                        while (true) {
                            System.out.print("New biography: ");
                            biography = scanner.nextLine();
                            if (biography.isEmpty()) {
                                System.out.println("Invalid biography! Please write something");
                            } else {
                                break;
                            }
                        }
                        actor.biography = biography;
                        System.out.println("Biography updated!");
                    }
                    case "3" -> {
                        System.out.println("Do you want to add or delete a project? (Add/Delete)");
                        String option3;
                        while (true) {
                            try {
                                System.out.print("Option: ");
                                option3 = scanner.nextLine();
                                if (option3.equalsIgnoreCase("ADD") ||
                                        option3.equalsIgnoreCase("DELETE")) {
                                    break;
                                } else {
                                    throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                                }
                            } catch (InvalidCommandException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        if(option3.equalsIgnoreCase("ADD")){
                            System.out.print("Project name: ");
                            String projectName;
                            while(true){
                                projectName = scanner.nextLine();
                                if(projectName.isEmpty()){
                                    System.out.println("Invalid project name! Please write something");
                                }
                                else{
                                    break;
                                }
                            }
                            String projectType;
                            while(true){
                                System.out.print("Project type (Movie/Series): ");
                                projectType = scanner.nextLine();
                                if(projectType.equalsIgnoreCase("Movie") ||
                                        projectType.equalsIgnoreCase("Series")){
                                    break;
                                }
                                else{
                                    System.out.println("Invalid project type! Please try again");
                                }
                            }
                            actor.projects.put(projectName,projectType);
                        }
                        else{
                            System.out.println("Choose a project:");
                            boolean exists2 = false;
                            for (Map.Entry<String,String> entry : actor.projects.entrySet()){
                                System.out.println("    - " + entry.getKey()+" : "+entry.getValue());
                                exists2 = true;
                            }
                            if(!exists2){
                                System.out.println("    - No projects");
                            }
                            else {
                                while (true) {
                                    System.out.print("Project name: ");
                                    String projectName = scanner.nextLine();
                                    boolean found = false;
                                    for (Map.Entry<String, String> entry : actor.projects.entrySet()) {
                                        if (entry.getKey().equalsIgnoreCase(projectName)) {
                                            actor.projects.remove(entry.getKey());
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        System.out.println("Project not found! Please try again");
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        System.out.println("Projects updated!");
                    }
                }
            }
        }
        else {
            System.out.println("Choose a production:");
            boolean exists = false;
            for (Object o : staff.contributions) {
                if (o instanceof Production) {
                    System.out.println("    - " + ((Production) o).title);
                    exists = true;
                }
            }
            if(!exists){
                System.out.println("    - No productions");
            }
            else{
                String option2;
                Production production = null;
                while (true) {
                    System.out.print("Production name: ");
                    String name = scanner.nextLine();
                    boolean found = false;
                    for (Object o : staff.contributions) {
                        if (o instanceof Production p) {
                            if (p.title.equalsIgnoreCase(name)) {
                                found = true;
                                production = p;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println("Production not found! Please try again");
                    } else {
                        break;
                    }
                }
                System.out.println("What do you want to update?");
                System.out.println("    1. Title");
                System.out.println("    2. Directors");
                System.out.println("    3. Actors");
                System.out.println("    4. Genres");
                System.out.println("    5. Subject");
                System.out.println("    6. Release Year");
                if(production instanceof Movie){
                    System.out.println("    7. Duration");
                }
                else{
                    System.out.println("    7. Number of Seasons");
                    System.out.println("    8. Seasons and Episodes");
                }
                String option3;
                while (true) {
                    System.out.print("Option: ");
                    try {
                        option3 = scanner.nextLine();
                        if(production instanceof Movie){
                            if(!option3.equals("1") && !option3.equals("2") && !option3.equals("3") &&
                                    !option3.equals("4") && !option3.equals("5") && !option3.equals("6") &&
                                    !option3.equals("7")){
                                throw new InvalidCommandException("Invalid command! Please write a number between 1 and 7");
                            }
                        } else{
                            if(!option3.equals("1") && !option3.equals("2") && !option3.equals("3") &&
                                    !option3.equals("4") && !option3.equals("5") && !option3.equals("6") &&
                                    !option3.equals("7") && !option3.equals("8")){
                                throw new InvalidCommandException("Invalid command! Please write a number between 1 and 8");
                            }
                        }
                        break;
                    } catch (InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                }
                switch (option3){
                    case "1" -> {
                        String title;
                        while (true) {
                            System.out.print("New title: ");
                            title = scanner.nextLine();
                            if (title.isEmpty()) {
                                System.out.println("Invalid title! Please write something");
                            } else {
                                break;
                            }
                        }
                        production.title = title;
                        System.out.println("Title updated!");
                    }
                    case "2" -> {
                        List<String> directors = new ArrayList<>();
                        while (true) {
                            System.out.print("New directors (ex: Name, Name, Name...): ");
                            String names = scanner.nextLine();
                            if (!names.isEmpty()) {
                                production.directors.addAll(Arrays.asList(names.split(", ")));
                                break;
                            } else {
                                System.out.println("Invalid directors! Please write something");
                            }
                        }
                        System.out.println("Directors updated!");
                    }
                    case "3" -> {
                        List<String> actors = new ArrayList<>();
                        while (true) {
                            System.out.print("New actors (ex: Name, Name, Name): ");
                            String names = scanner.nextLine();
                            if (!names.isEmpty()) {
                                production.actors.addAll(Arrays.asList(names.split(", ")));
                                break;
                            } else {
                                System.out.println("Invalid actors! Please write something");
                            }
                        }
                        production.actors = actors;
                        System.out.println("Actors updated!");
                    }
                    case "4" -> {
                        List<Genre> genres = new ArrayList<>();
                        System.out.println("Choose genres:");
                        for (Genre g : Genre.values()) {
                            System.out.println("    - " + g);
                        }
                        while (true) {
                            System.out.print("New genres (ex: Name, Name, Name): ");
                            String names = scanner.nextLine();
                            if (!names.isEmpty()) {
                                for (String s : names.split(", ")) {
                                    try {
                                        genres.add(Genre.valueOf(s.toUpperCase()));
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid genre! Please try again");
                                    }
                                }
                                break;
                            } else {
                                System.out.println("Invalid genres! Please write something");
                            }
                        }
                        production.genres = genres;
                        System.out.println("Genres updated!");
                    }
                    case "5" -> {
                        String subject;
                        while (true) {
                            System.out.print("New subject: ");
                            subject = scanner.nextLine();
                            if (subject.isEmpty()) {
                                System.out.println("Invalid subject! Please write something");
                            } else {
                                break;
                            }
                        }
                        production.subject = subject;
                        System.out.println("Subject updated!");
                    }
                    case "6" -> {
                        Long releaseYear;
                        while (true) {
                            System.out.print("New release year: ");
                            try {
                                releaseYear = Long.parseLong(scanner.nextLine());
                                if(releaseYear>0){
                                    break;
                                }
                                else {
                                    System.out.println("Invalid release year! Please write a positive number");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid release year! Please write a positive number");
                            }
                        }
                        if(production instanceof Movie){
                            ((Movie) production).releaseYear = releaseYear;
                        }
                        else{
                            ((Series) production).releaseYear = releaseYear;
                        }
                        System.out.println("Release year updated!");
                    }
                    case "7" -> {
                        if(production instanceof Movie){
                            String duration;
                            while (true) {
                                System.out.print("New duration: ");
                                duration = scanner.nextLine();
                                if (duration.isEmpty()) {
                                    System.out.println("Invalid duration! Please write something");
                                } else {
                                    break;
                                }
                            }
                            ((Movie) production).duration = duration;
                            System.out.println("Duration updated!");
                        }
                        else{
                            Long numberOfSeasons;
                            while (true) {
                                System.out.print("New number of seasons: ");
                                try {
                                    numberOfSeasons = Long.parseLong(scanner.nextLine());
                                    if(numberOfSeasons>0){
                                        break;
                                    }
                                    else {
                                        System.out.println("Invalid number of seasons! Please write a positive number");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid number of seasons! Please write a positive number");
                                }
                            }
                            ((Series) production).numberOfSeasons = numberOfSeasons;
                            System.out.println("Number of seasons updated!");
                        }
                    }
                    case "8" -> {
                        int i = ((Series) production).getSeasonsMap().size();
                        while (i < ((Series) production).numberOfSeasons) {
                            String seasonName;
                            while (true) {
                                System.out.print("New season name: ");
                                seasonName = scanner.nextLine();
                                if (seasonName.isEmpty()) {
                                    System.out.println("Invalid season name! Please write something");
                                } else {
                                    break;
                                }
                            }
                            List<Episode> episodes = new ArrayList<>();
                            while (true) {
                                System.out.print("  Episodes and duration (ex: Name - 40 minutes, Name - 40 minutes...): ");
                                String names = scanner.nextLine();
                                if (!names.isEmpty()) {
                                    try {
                                        for (String s : names.split(", ")) {
                                            String[] split = s.split(" - ");
                                            episodes.add(new Episode(split[0], split[1]));
                                        }
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("Invalid episodes! Please try again");
                                    }
                                } else {
                                    System.out.println("Invalid episode! Please write something");
                                }
                            }
                            ((Series) production).getSeasonsMap().put(seasonName, episodes);
                            i++;
                        }
                        System.out.println("Seasons and episodes updated!");
                    }
                }
            }
        }
    }

    private void addDeleteProdActor(Staff staff) {
        System.out.println("What do you want to add/delete from system?");
        System.out.println("    1. Actor");
        System.out.println("    2. Movie or Series");
        Scanner scanner = new Scanner(System.in);
        String option1;
        while(true){
            try {
                System.out.print("Option: ");
                option1 = scanner.nextLine();
                if (option1.equals("1") || option1.equals("2")) {
                    break;
                } else {
                    throw new InvalidCommandException("Invalid command! Please write 1 or 2");
                }
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        if(option1.equals("1")) {
            String option2;
            System.out.println("Do you want to add or delete an actor? (Add/Delete)");
            while (true) {
                try {
                    System.out.print("Option: ");
                    option2 = scanner.nextLine();
                    if (option2.equalsIgnoreCase("ADD") ||
                            option2.equalsIgnoreCase("DELETE")) {
                        break;
                    } else {
                        throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (option2.equalsIgnoreCase("ADD")){
                System.out.println("Enter actor details:");
                String name;
                while(true) {
                    System.out.print("  Name: ");
                    name = scanner.nextLine();
                    if (!name.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid name! Please write only letters and spaces (ex: John Doe)");
                    } else {
                        break;
                    }
                }
                String biography;
                while(true) {
                    System.out.print("  Biography: ");
                    biography = scanner.nextLine();
                    if (biography.isEmpty()) {
                        System.out.println("Invalid biography! Please write something");
                    } else {
                        break;
                    }
                }
                Map<String,String> projects = new HashMap<>();
                while(true){
                    System.out.print("  Project name: ");
                    String projectName;
                    while(true){
                        projectName = scanner.nextLine();
                        if(projectName.isEmpty()){
                            System.out.println("Invalid project name! Please write something");
                        }
                        else{
                            break;
                        }
                    }
                    String projectType;
                    while(true){
                        System.out.print("  Project type (Movie/Series): ");
                        projectType = scanner.nextLine();
                        if(projectType.equalsIgnoreCase("Movie") ||
                                projectType.equalsIgnoreCase("Series")){
                            break;
                        }
                        else{
                            System.out.println("Invalid project type! Please try again");
                        }
                    }
                    projects.put(projectName,projectType);
                    System.out.println("Do you want to add another project? (Yes/No)");
                    String option;
                    while(true) {
                        System.out.print("Option: ");
                        try {
                            option = scanner.nextLine();
                            if (option.equalsIgnoreCase("YES") ||
                                    option.equalsIgnoreCase("NO")) {
                                break;
                            } else {
                                throw new InvalidCommandException("Invalid command! Please write Yes or No");
                            }
                        } catch (InvalidCommandException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if(option.equalsIgnoreCase("NO")){
                        break;
                    }
                }
                Actor actor = new Actor(name,projects,biography);
                staff.addActorSystem(actor);
                System.out.println("Actor added!");
                if(staff instanceof Contributor){
                    Context context = new Context(new AddProdOrActorXp());
                    staff.xp += context.executeStrategy();
                }
            }
            else {
                System.out.println("Choose an actor:");
                boolean exists = false;
                for (Object o : staff.contributions) {
                    if (o instanceof Actor) {
                        System.out.println("    - " + ((Actor) o).name);
                        exists = true;
                    }
                }
                if(!exists) {
                    System.out.println("    - No actors");
                }
                else {
                    while (true) {
                        System.out.print("Delete actor: ");
                        String name = scanner.nextLine();
                        boolean found = false;
                        for (Object o : staff.contributions) {
                            if (o instanceof Actor a) {
                                if (a.name.equals(name)) {
                                    staff.removeActorSystem(name);
                                    System.out.println("Actor removed!");
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            System.out.println("Actor not found! Please try again");
                        } else {
                            break;
                        }
                    }
                }
            }
            System.out.println("Actors added by you:");
            for (Object o : staff.contributions){
                if(o instanceof Actor a){
                    System.out.println("    - " + a.name);
                }
            }
        } else {
            String option2;
            System.out.println("Do you want to add or delete a movie/series? (Add/Delete)");
            while (true) {
                try {
                    System.out.print("Option: ");
                    option2 = scanner.nextLine();
                    if (option2.equalsIgnoreCase("ADD") ||
                            option2.equalsIgnoreCase("DELETE")) {
                        break;
                    } else {
                        throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (option2.equalsIgnoreCase("ADD")) {
                System.out.println("Enter production details:");
                String title;
                while (true) {
                    System.out.print("  Title: ");
                    title = scanner.nextLine();
                    if (title.isEmpty()) {
                        System.out.println("Invalid title! Please write something");
                    } else {
                        break;
                    }
                }
                String subject;
                while (true) {
                    System.out.print("  Subject: ");
                    subject = scanner.nextLine();
                    if (subject.isEmpty()) {
                        System.out.println("Invalid subject! Please write something");
                    } else {
                        break;
                    }
                }
                List<String> directors = new ArrayList<>();
                while (true) {
                    System.out.print("  Directors (ex: Name, Name, Name...): ");
                    String names = scanner.nextLine();
                    if (!names.isEmpty()) {
                        directors.addAll(Arrays.asList(names.split(", ")));
                        break;
                    } else {
                        System.out.println("Invalid directors! Please write something");
                    }
                }
                List<String> prodActors = new ArrayList<>();
                while (true) {
                    System.out.print("  Actors (ex: Name, Name, Name): ");
                    String names = scanner.nextLine();
                    if (!names.isEmpty()) {
                        prodActors.addAll(Arrays.asList(names.split(", ")));
                        break;
                    } else {
                        System.out.println("Invalid actors! Please write something");
                    }
                }
                List<Genre> genres = new ArrayList<>();
                System.out.println("Choose genres:");
                for (Genre g : Genre.values()) {
                    System.out.println("    - " + g);
                }
                while (true) {
                    System.out.print("  Genres (ex: Name, Name, Name): ");
                    String names = scanner.nextLine();
                    if (!names.isEmpty()) {
                        for (String s : names.split(", ")) {
                            try {
                                genres.add(Genre.valueOf(s.toUpperCase()));
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid genre!");
                            }
                        }
                        break;
                    } else {
                        System.out.println("Invalid genres! Please write something");
                    }
                }
                String type;
                while (true) {
                    System.out.print("  Production type (Movie/Series): ");
                    type = scanner.nextLine();
                    if (type.equalsIgnoreCase("Movie") ||
                            type.equalsIgnoreCase("Series")) {
                        break;
                    } else {
                        System.out.println("Invalid production type! Please try again");
                    }
                }
                if (type.equalsIgnoreCase("MOVIE")) {
                    System.out.print("Duration (ex: 120 minutes or empty if not known): ");
                    String duration = scanner.nextLine();
                    if (duration.isEmpty()) {
                        duration = null;
                    }
                    Long releaseYear;
                    while (true) {
                        System.out.print("Release year (ex: 2020 or empty if not known): ");
                        String year = scanner.nextLine();
                        if (year.isEmpty()) {
                            releaseYear = null;
                            break;
                        } else {
                            try {
                                releaseYear = Long.parseLong(year);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid year! Please try again");
                            }
                        }
                    }
                    Movie movie = new Movie(title, directors, prodActors, genres, new ArrayList<>(), subject, 0.0,
                            duration, releaseYear);
                    staff.addProductionSystem(movie);
                } else {
                    Long releaseYear;
                    while (true) {
                        System.out.print("Release year (ex: 2020 or empty if not known): ");
                        String year = scanner.nextLine();
                        if (year.isEmpty()) {
                            releaseYear = null;
                            break;
                        } else {
                            try {
                                releaseYear = Long.parseLong(year);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid year! Please try again");
                            }
                        }
                    }
                    Long numberOfSeasons;
                    while (true) {
                        System.out.print("Number of seasons (ex: 5 or empty if not known): ");
                        String num = scanner.nextLine();
                        if (num.isEmpty()) {
                            numberOfSeasons = null;
                            break;
                        } else {
                            try {
                                numberOfSeasons = Long.parseLong(num);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number! Please try again");
                            }
                        }
                    }
                    Map<String, List<Episode>> seasonsMap = new HashMap<>();
                    int i = 0;
                    if(numberOfSeasons!=null) {
                        while (i < numberOfSeasons) {
                            String seasonName;
                            while (true) {
                                System.out.print("    Season name: ");
                                seasonName = scanner.nextLine();
                                if (seasonName.isEmpty()) {
                                    System.out.println("Invalid season name! Please write something");
                                } else {
                                    break;
                                }
                            }
                            List<Episode> episodes = new ArrayList<>();
                            while (true) {
                                System.out.print("  Episodes and duration (ex: Name - 40 minutes, Name - 40 minutes...): ");
                                String names = scanner.nextLine();
                                if (!names.isEmpty()) {
                                    try {
                                        for (String s : names.split(", ")) {
                                            String[] split = s.split(" - ");
                                            episodes.add(new Episode(split[0], split[1]));
                                        }
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("Invalid episodes! Please try again");
                                    }
                                } else {
                                    System.out.println("Invalid episode! Please write something");
                                }
                            }
                            seasonsMap.put(seasonName, episodes);
                            i++;
                        }
                        if (seasonsMap.isEmpty()) {
                            seasonsMap = null;
                        }
                    }
                    Series series = new Series(title, directors, prodActors, genres, new ArrayList<>(), subject, 0.0,
                            releaseYear, numberOfSeasons, seasonsMap);
                    staff.addProductionSystem(series);
                }
                System.out.println("Production added!");
                if(staff instanceof Contributor){
                    Context context = new Context(new AddProdOrActorXp());
                    staff.xp += context.executeStrategy();
                }
            }
            else {
                System.out.println("Choose a production:");
                boolean exists = false;
                for (Object o : staff.contributions) {
                    if (o instanceof Production) {
                        System.out.println("    - " + ((Production) o).title);
                        exists = true;
                    }
                }
                if(!exists) {
                    System.out.println("    - No productions");
                }
                else {
                    while (true) {
                        System.out.print("Delete production: ");
                        String title = scanner.nextLine();
                        boolean found = false;
                        for (Object o : staff.contributions) {
                            if (o instanceof Production p) {
                                if (p.title.equals(title)) {
                                    staff.removeProductionSystem(title);
                                    System.out.println("Production removed!");
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            System.out.println("Production not found! Please try again");
                        } else {
                            break;
                        }
                    }
                }
            }
            System.out.println("Productions added by you:");
            for (Object o : staff.contributions){
                if(o instanceof Production p){
                    System.out.println("    - " + p.title);
                }
            }
        }
    }

    private void addDeleteUser(Admin admin) {
        System.out.println("Do you want to add or delete a user? (Add/Delete)");
        Scanner scanner = new Scanner(System.in);
        String option;
        while(true){
            System.out.print("Option: ");
            try{
                option = scanner.nextLine();
                if (!option.equalsIgnoreCase("ADD") &&
                        !option.equalsIgnoreCase("DELETE")){
                    throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
        if(option.equalsIgnoreCase("ADD")){
            System.out.println("Enter user details:");
            String email;
            while(true) {
                System.out.print("  Email: ");
                email = scanner.nextLine();
                if (!email.contains("@") || email.contains(" ")) {
                    System.out.println("Invalid email! Please try again");
                } else {
                    boolean found = false;
                    for (User u : users) {
                        if (u.info.getCredentials().getEmail().equals(email)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        System.out.println("Email already in use! Please try again");
                    } else {
                        break;
                    }
                }
            }
            String name;
            while(true) {
                System.out.print("  Name: ");
                name = scanner.nextLine();
                if (!name.matches("[a-zA-Z]+")) {
                    System.out.println("Invalid name! Please write only letters (ex: John)");
                } else {
                    break;
                }
            }
            String accType;
            while (true){
                System.out.print("  Account type (Regular/Contributor/Admin): ");
                accType= scanner.nextLine();
                if(accType.equalsIgnoreCase("Regular") ||
                        accType.equalsIgnoreCase("Contributor") ||
                        accType.equalsIgnoreCase("Admin")){
                    break;
                }
                else{
                    System.out.println("Invalid account type! Please try again");
                }
            }
            AccountType type = AccountType.valueOf(accType.toUpperCase());
            admin.addUser(email,name,type);
            System.out.println("New user added!");
        }
        else{
            System.out.println("Choose a user:");
            for (User u : users){
                System.out.println("    - " + u.username);
            }
            while(true) {
                System.out.print("Delete user: ");
                String username = scanner.nextLine();
                boolean found = false;
                for (User u : users) {
                    if (u.username.equals(username)) {
                        admin.removeUser(username);
                        System.out.println("User removed!");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("User not found! Please try again");
                }
                else{
                    break;
                }
            }
        }
        System.out.println("All users:");
        for (User u : users){
            System.out.println("    - " + u.username);
        }
    }

    private void addDeleteFavourites(User user) {
        System.out.println("What do you want to add/delete from favourites?");
        System.out.println("    1. Actor");
        System.out.println("    2. Movie or Series");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Option: ");
            try {
                switch (scanner.nextLine()) {
                    case "1" -> {
                        System.out.println("Your favourite actors:");
                        boolean exists = false;
                        for (Object o : user.preferences) {
                            if (o instanceof Actor) {
                                System.out.println("    - " + ((Actor) o).name);
                                exists = true;
                            }
                        }
                        if(!exists) {
                            System.out.println("    - No actors");
                        }
                        System.out.println("Do you want to add or delete an actor? (Add/Delete)");
                        String option;
                        while (true) {
                            System.out.print("Option: ");
                            try {
                                option = scanner.nextLine();
                                if (!option.equalsIgnoreCase("ADD") &&
                                        !option.equalsIgnoreCase("DELETE")) {
                                    throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                                }
                                break;
                            } catch (InvalidCommandException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        while (true) {
                            System.out.print("Actor name: ");
                            String name = scanner.nextLine();
                            boolean found = false;
                            if(option.equalsIgnoreCase("ADD")) {
                                for (Actor a : actors) {
                                    if (a.name.equalsIgnoreCase(name)) {
                                        if(!user.preferences.contains(a)) {
                                            user.addPreference(a);
                                            System.out.println("Actor added to favourites!");
                                        }
                                        else{
                                            System.out.println("Actor already in favourites!");
                                        }
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            else{
                                for (Object o : user.preferences) {
                                    if (o instanceof Actor) {
                                        if (((Actor) o).name.equalsIgnoreCase(name)) {
                                            user.removePreference(o);
                                            System.out.println("Actor removed from favourites!");
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (!found) {
                                System.out.println("Actor not found! Please try again");
                            } else {
                                break;
                            }
                        }
                        System.out.println("Your favourite actors:");
                        for (Object o : user.preferences) {
                            if (o instanceof Actor)
                                System.out.println("    - " + ((Actor) o).name);
                        }
                    }
                    case "2" -> {
                        System.out.println("Your favourite movies and series:");
                        boolean exists = false;
                        for (Object o : user.preferences) {
                            if (o instanceof Production) {
                                System.out.println("    - " + ((Production) o).title);
                                exists = true;
                            }
                        }
                        if(!exists) {
                            System.out.println("    - No movies or series");
                        }
                        System.out.println("Do you want to add or delete a movie/series? (Add/Delete)");
                        String option;
                        while (true) {
                            System.out.print("Option: ");
                            try {
                                option = scanner.nextLine();
                                if (!option.equalsIgnoreCase("ADD") &&
                                        !option.equalsIgnoreCase("DELETE")) {
                                    throw new InvalidCommandException("Invalid command! Please write Add or Delete");
                                }
                                break;
                            } catch (InvalidCommandException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        while (true) {
                            System.out.print("Movie/Series name: ");
                            String name = scanner.nextLine();
                            boolean found = false;
                            if(option.equalsIgnoreCase("ADD")) {
                                for (Production p : productions) {
                                    if (p.title.equalsIgnoreCase(name)) {
                                        if(!user.preferences.contains(p)) {
                                            user.addPreference(p);
                                            if(p instanceof Movie){
                                                System.out.println("Movie added to favourites!");
                                            }
                                            else{
                                                System.out.println("Series added to favourites!");
                                            }
                                        }
                                        else{
                                            if(p instanceof Movie){
                                                System.out.println("Movie already in favourites!");
                                            }
                                            else{
                                                System.out.println("Series already in favourites!");
                                            }
                                        }
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            else{
                                for (Object o : user.preferences) {
                                    if (o instanceof Production) {
                                        if (((Production) o).title.equalsIgnoreCase(name)) {
                                            user.removePreference(o);
                                            if(o instanceof Movie){
                                                System.out.println("Movie removed from favourites!");
                                            }
                                            else{
                                                System.out.println("Series removed from favourites!");
                                            }
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (!found) {
                                System.out.println("Movie/Series not found! Please try again");
                            } else {
                                break;
                            }
                        }
                        System.out.println("Your favourite movies and series:");
                        for (Object o : user.preferences) {
                            if (o instanceof Production)
                                System.out.println("    - " + ((Production) o).title);
                        }
                    }
                    default -> {
                        throw new InvalidCommandException("Invalid command! Please write 1 or 2");
                    }
                }
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void searchMovieActorSeries() {
        System.out.print("Search for: ");
        Scanner scanner = new Scanner(System.in);
        String search = scanner.nextLine();
        while(search.isEmpty()){
            System.out.println("Please write something");
            System.out.print("Search for: ");
            search = scanner.nextLine();
        }
        System.out.println("Search results:");
        System.out.println("    - Actors:");
        boolean actorsFound = false;
        for (Actor a : actors){
            if (a.name.toLowerCase().startsWith(search.toLowerCase())){
                System.out.println("        - " + a.name);
                actorsFound = true;
            }
        }
        if(!actorsFound){
            System.out.println("        - No actors found");
        }
        System.out.println("    - Movies:");
        boolean moviesFound = false;
        for (Production p : productions){
            if(p instanceof Movie){
                if (p.title.toLowerCase().startsWith(search.toLowerCase())){
                    moviesFound = true;
                    System.out.println("        - " + p.title);
                }
            }
        }
        if(!moviesFound){
            System.out.println("        - No movies found");
        }
        System.out.println("    - Series:");
        boolean seriesFound = false;
        for (Production p : productions){
            if(p instanceof Series){
                if (p.title.toLowerCase().startsWith(search.toLowerCase())){
                    System.out.println("        - " + p.title);
                    seriesFound = true;
                }
            }
        }if (!seriesFound){
            System.out.println("        - No series found");
        }
        if(actorsFound || moviesFound || seriesFound) {
            while(true) {
                System.out.print("Display info for: ");
                String name = scanner.nextLine();
                boolean found = false;
                if (actorsFound) {
                    for (Actor a : actors) {
                        if (a.name.equalsIgnoreCase(name)) {
                            a.displayInfo();
                            found = true;
                            break;
                        }
                    }
                }
                if (moviesFound && !found) {
                    for (Production p : productions) {
                        if (p instanceof Movie) {
                            if (p.title.equalsIgnoreCase(name)) {
                                p.displayInfo();
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (seriesFound && !found) {
                    for (Production p : productions) {
                        if (p instanceof Series) {
                            if (p.title.equalsIgnoreCase(name)) {
                                p.displayInfo();
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (!found) {
                    System.out.println("Not found! Please try again");
                }
                else{
                    break;
                }
            }
        }
    }

    private void viewNotifications(User user) {
        System.out.println("Notifications:");
        if(user.notifications.isEmpty()){
            System.out.println("    - No notifications");
        }
        else {
            for (String notification : user.notifications){
                System.out.println("    - " + notification);
            }
        }
    }
    private void viewActorsDetails() {
        System.out.println("Do you want to sort actors by name? (Yes/No)");
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Option: ");
            try{
                String option = scanner.nextLine();
                if (option.equalsIgnoreCase("YES")) {
                    actors.sort(Actor::compareTo);
                } else if(!option.equalsIgnoreCase("NO")) {
                    throw new InvalidCommandException("Invalid command! Please write Yes or No");
                }
                System.out.println("Choose an actor:");
                for (Actor a : actors) {
                    System.out.println("    - " + a.name);
                }
                while(true) {
                    System.out.print("Actor name: ");
                    String name = scanner.nextLine();
                    boolean found = false;
                    for (Actor a : actors) {
                        if (a.name.equalsIgnoreCase(name)) {
                            a.displayInfo();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Actor not found! Please try again");
                    } else {
                        break;
                    }
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void viewProductionsDetails() {
        System.out.println("Do you want to filter productions? (Yes/No)");
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Option: ");
            try{
                switch (scanner.nextLine().toUpperCase()){
                    case "YES" -> {
                        filterProductions();
                    }
                    case "NO" -> {
                        System.out.println("Choose a production:");
                        for (Production p : productions) {
                            System.out.println("    - " + p.title);
                        }
                    }
                    default -> {
                        throw new InvalidCommandException("Invalid command! Please write Yes or No");
                    }
                }
                while(true) {
                    System.out.print("Production name: ");
                    String title = scanner.nextLine();
                    boolean found = false;
                    for (Production p : productions) {
                        if (p.title.equalsIgnoreCase(title)) {
                            p.displayInfo();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Production not found! Please try again");
                    } else {
                        break;
                    }
                }
                break;
            } catch (InvalidCommandException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void filterProductions(){
        System.out.println("1. Filter by rating");
        System.out.println("2. Filter by genre");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("  Option: ");
            try {
                switch (scanner.nextLine()) {
                    case "1" -> {
                        System.out.println("    1. Higher to lower");
                        System.out.println("    2. Lower to higher");
                        while (true) {
                            System.out.print("      Option: ");
                            try {
                                switch (scanner.nextLine()) {
                                    case "1" -> {
                                        productions.sort((p1, p2) -> p2.compareByRating(p1));
                                    }
                                    case "2" -> {
                                        productions.sort(Production::compareByRating);
                                    }
                                    default -> {
                                        throw new InvalidCommandException("Invalid command! Please write 1 or 2");
                                    }
                                }
                                break;
                            } catch (InvalidCommandException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        for (Production p : productions) {
                            System.out.println("    - " + p.title);
                        }
                    }
                    case "2" -> {
                        System.out.println("Choose a genre:");
                        for (Genre g : Genre.values()) {
                            System.out.println("    - " + g);
                        }
                        Genre genre;
                        while (true) {
                            System.out.print("Option: ");
                            try {
                                genre = Genre.valueOf(scanner.next().toUpperCase());
                                break;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid genre! Please try again");
                            }
                        }
                        for (Production p : productions) {
                            if (p.genres.contains(genre)) {
                                System.out.println("    - " + p.title);
                            }
                        }
                    }
                    default -> {
                        throw new InvalidCommandException("Invalid command! Please write 1 or 2");
                    }
                }
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        IMDB.getInstance().run();
    }
}

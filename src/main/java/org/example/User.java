package org.example;

import java.time.LocalDateTime;
import java.util.*;

public abstract class User {
    Information info;
    AccountType type;
    String username;
    int xp;
    List<String> notifications = new ArrayList<>();
    SortedSet<Object> preferences = new TreeSet<>();

    public User(Information info, AccountType type, String username, int xp, List<String> notifications, SortedSet<Object> preferences) {
        this.info = info;
        this.type = type;
        this.username = username;
        this.xp = xp;
        this.notifications = notifications;
        this.preferences = preferences;
    }
    public static class Information{
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDateTime birthDate;
        private Information(InformationBuilder builder){
            this.credentials=builder.credentials;
            this.name= builder.name;
            this.country= builder.country;
            this.age=builder.age;
            this.gender= builder.gender;
            this.birthDate=builder.birthDate;
        }
        public static class InformationBuilder{
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private char gender;
            private LocalDateTime birthDate;
            public InformationBuilder(Credentials credentials, String name) {
                this.credentials = credentials;
                this.name = name;
            }
            public InformationBuilder country(String country){
                this.country=country;
                return this;
            }
            public InformationBuilder age(int age){
                this.age=age;
                return this;
            }
            public InformationBuilder gender(char gender){
                this.gender=gender;
                return this;
            }
            public InformationBuilder birthDate(LocalDateTime birthDate){
                this.birthDate=birthDate;
                return this;
            }
            public Information build(){
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public char getGender() {
            return gender;
        }

        public void setGender(char gender) {
            this.gender = gender;
        }

        public LocalDateTime getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDateTime birthDate) {
            this.birthDate = birthDate;
        }
    }
    public void addPreference(Object preference){
        preferences.add(preference);
    }
    public void removePreference(Object preference){
        preferences.remove(preference);
    }
    public void updateExperience(){

    }
    public void logout(){
        System.out.println("Logging out...");
    }
}

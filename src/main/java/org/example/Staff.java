package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff extends User implements StaffInterface{
    List<Request> requests = new ArrayList<>();
    SortedSet<Object> contributions = new TreeSet<>();
    public Staff(Information info, AccountType type, String username, Integer xp, List<String> notifications,
                 SortedSet<Object> preferences, List<Request> requests, SortedSet<Object> contributions) {
        super(info, type, username, xp, notifications, preferences);
        this.requests = requests;
        this.contributions = contributions;
    }

    @Override
    public void addProductionSystem(Production p) {
        IMDB.getInstance().productions.add(p);
        this.contributions.add(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        IMDB.getInstance().actors.add(a);
        this.contributions.add(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        for(Production p: IMDB.getInstance().productions){
            if(p.title.equals(name)){
                IMDB.getInstance().productions.remove(p);
                break;
            }
        }
        for(Object o : this.contributions){
            if(o instanceof Production p){
                if(p.title.equals(name)){
                    this.contributions.remove(p);
                    break;
                }
            }
        }
    }

    @Override
    public void removeActorSystem(String name) {
        for(Actor a: IMDB.getInstance().actors){
            if(a.name.equals(name)){
                IMDB.getInstance().actors.remove(a);
                break;
            }
        }
        for(Object o : this.contributions){
            if(o instanceof Actor a){
                if(a.name.equals(name)){
                    this.contributions.remove(a);
                    break;
                }
            }
        }
    }

    @Override
    public void updateProduction(Production p) {
        for(Production production: IMDB.getInstance().productions){
            if(production.title.equals(p.title)){
                production = p;
                break;
            }
        }
    }

    @Override
    public void updateActor(Actor a) {
        for(Actor actor: IMDB.getInstance().actors){
            if(actor.name.equals(a.name)){
                actor = a;
                break;
            }
        }
    }
}

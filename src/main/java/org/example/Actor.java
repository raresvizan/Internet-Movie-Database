package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Actor implements Comparable<Object>{
    String name;
    Map<String,String> projects = new HashMap<>();
    String biography;

    public Actor(String name, Map<String, String> projects, String biography) {
        this.name = name;
        this.projects = projects;
        this.biography = biography;
    }
    @Override
    public int compareTo(@NotNull Object o) {
        if(o instanceof Production production) {
            return this.name.compareTo(production.title);
        }
        if(o instanceof Actor a){
            return this.name.compareTo(a.name);
        }
        return 0;
    }
    public void displayInfo(){
        System.out.println("Name: "+name);
        System.out.println("Biography: "+biography);
        System.out.println("Projects: ");
        for (Map.Entry<String,String> entry : projects.entrySet()){
            System.out.println("    " + entry.getKey()+" : "+entry.getValue());
        }
    }
}

package org.example;

interface ExperienceStrategy {
    public int calculateExperience();
}
class AddRatingXp implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 5;
    }
}

class RemoveRatingXp implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return -5;
    }
}
class AddRequestXp implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 25;
    }
}

class AddProdOrActorXp implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 50;
    }
}

class Context {
    private ExperienceStrategy strategy;

    public Context(ExperienceStrategy strategy) {
        this.strategy = strategy;
    }

    public int executeStrategy() {
        return strategy.calculateExperience();
    }
}

public class StrategyPattern{

}

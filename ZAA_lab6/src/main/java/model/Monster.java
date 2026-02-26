package model;

import java.util.Map;

public class Monster implements Entity {
    private final String name;
    private final String index;
    private final double challengeRating;
    private final int hitPoints;
    private final int armorClass;
    private final Map<String, Integer> stats;
    private final String damageDice;

    public Monster(String name, String index, double challengeRating,
                   int hitPoints, int armorClass,
                   Map<String, Integer> stats,
                   String damageDice) {
        this.name = name;
        this.index = index;
        this.challengeRating = challengeRating;
        this.hitPoints = hitPoints;
        this.armorClass = armorClass;
        this.stats = stats;
        this.damageDice = damageDice;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIndex() {
        return index;
    }

    public double getChallengeRating() { return challengeRating; }
    public int getHitPoints() { return hitPoints; }
    public int getArmorClass() { return armorClass; }
    public Map<String, Integer> getStats() { return stats; }
    public String getDamageDice() { return damageDice; }

    @Override
    public String toString() {
        return "MONSTER CARD\n" +
                "Name: " + name + "\n" +
                "Index: " + index + "\n" +
                "CR: " + challengeRating + "\n" +
                "HP: " + hitPoints + "\n" +
                "AC: " + armorClass + "\n" +
                "Damage dice: " + damageDice + "\n" +
                "Stats: " + stats + "\n";
    }

}

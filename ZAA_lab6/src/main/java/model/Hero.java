package model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Hero extends GameCharacter {
    private int level;
    private String raceName;
    private String className;

    public Hero(String name, String index, int level, String raceName, String className,
                Map<String, Integer> stats,
                List<String> skills,
                List<String> equipment,
                int hitPoints, int armorClass) {
        this.name = name;
        this.index = index;
        this.level = level;
        this.raceName = raceName;
        this.className = className;
        this.stats = stats;
        this.skills = skills;
        this.equipment = equipment;
        this.hitPoints = hitPoints;
        this.armorClass = armorClass;
    }

    public int getLevel() { return level; }
    public String getRaceName() { return raceName; }
    public String getClassName() { return className; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HERO CARD\n");
        sb.append(String.format("Name : %-27s\n", name));
        sb.append(String.format("Race : %-27s\n", raceName));
        sb.append(String.format("Class : %-27s\n", className));
        sb.append(String.format("Level : %-27d\n", level));
        sb.append(String.format("HP : %-27d\n", hitPoints));
        sb.append(String.format("AC : %-27d\n", armorClass));
        sb.append("--------------------------------\n");
        sb.append(String.format("STR=%-2d DEX=%-2d CON=%-2d INT=%-2d WIS=%-2d CHA=%-2d   \n",
                stats.get("STR"), stats.get("DEX"), stats.get("CON"),
                stats.get("INT"), stats.get("WIS"), stats.get("CHA")));
        sb.append("--------------------------------\n");
        sb.append("Skills:                         \n");
        for (String s : skills) sb.append(" - ").append(s).append("\n");
        sb.append("--------------------------------\n");
        sb.append("Equipment (Iterator):           \n");
        Iterator<String> it = equipment.iterator();
        while (it.hasNext()) sb.append(" - ").append(it.next()).append("\n");
        sb.append("--------------------------------\n");
        return sb.toString();
    }
}

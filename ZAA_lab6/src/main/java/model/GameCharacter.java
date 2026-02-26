package model;

import java.util.List;
import java.util.Map;

public abstract class GameCharacter implements Entity {
    protected String name;
    protected String index;

    protected Map<String, Integer> stats;
    protected List<String> skills;
    protected List<String> equipment;

    protected int hitPoints;
    protected int armorClass;

    @Override public String getName() { return name; }
    @Override public String getIndex() { return index; }

    public Map<String, Integer> getStats() { return stats; }
    public List<String> getSkills() { return skills; }
    public List<String> getEquipment() { return equipment; }
    public int getHitPoints() { return hitPoints; }
    public int getArmorClass() { return armorClass; }
}

package generator;

import api.DndService;
import model.Hero;
import util.Dice;

import java.util.*;

public class HeroGenerator {
    private final DndService service;

    private static final String[] NAMES = {
            "Aldric","Mira","Thorne","Elowen","Garrick","Lyra","Borin","Selene","Kael","Nyx",
            "Rowan","Eira","Dorian","Vex","Isolde","Rurik","Fenn","Cassia","Orin","Talia",
            "Sable","Finnian","Maeve","Kira","Blaine"
    };

    public HeroGenerator(DndService service) {
        this.service = service;
    }

    public Hero generate() {
        List<String> races = service.getAllRaceIndexes();
        List<String> classes = service.getAllClassIndexes();

        String raceIndex = Dice.pickRandom(races);
        String classIndex = Dice.pickRandom(classes);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("STR", Dice.rollDropLowest(4, 6));
        stats.put("DEX", Dice.rollDropLowest(4, 6));
        stats.put("CON", Dice.rollDropLowest(4, 6));
        stats.put("INT", Dice.rollDropLowest(4, 6));
        stats.put("WIS", Dice.rollDropLowest(4, 6));
        stats.put("CHA", Dice.rollDropLowest(4, 6));

        Map<String, Integer> bonuses = service.getRaceBonuses(raceIndex);
        for (Map.Entry<String, Integer> e : bonuses.entrySet()) {
            stats.put(e.getKey(), stats.getOrDefault(e.getKey(), 0) + e.getValue());
        }

        List<String> allSkills = service.getClassSkills(classIndex);
        List<String> skills = new ArrayList<>(Dice.pickRandom(allSkills, 3));
        if (skills.isEmpty() && !allSkills.isEmpty()) {
            skills.addAll(Dice.pickRandom(allSkills, Math.min(3, allSkills.size())));
        }

        List<String> equipment = service.getStartingEquipment(classIndex);

        int hitDie = service.getClassHitDie(classIndex);
        int hp = hitDie + modifier(stats.get("CON"));

        int ac = 10 + modifier(stats.get("DEX"));

        String name = NAMES[new Random().nextInt(NAMES.length)];

        String raceName = service.getRaceName(raceIndex);
        String className = service.getClassName(classIndex);

        return new Hero(
                name,
                "hero-" + name.toLowerCase(),
                1,
                raceName,
                className,
                stats,
                skills,
                equipment,
                hp,
                ac
        );
    }

    private static int modifier(int value) {
        return (value - 10) / 2;
    }
}
package combat;

import model.Monster;
import util.Dice;

public class MonsterCombatant implements Combatant {
    private final Monster monster;
    private int currentHp;

    public MonsterCombatant(Monster monster) {
        this.monster = monster;
        this.currentHp = monster.getHitPoints();
    }

    @Override public String getName() { return monster.getName(); }
    @Override public int getCurrentHp() { return currentHp; }
    @Override public int getMaxHp() { return monster.getHitPoints(); }
    @Override public int getArmorClass() { return monster.getArmorClass(); }
    @Override public boolean isAlive() { return currentHp > 0; }

    @Override
    public void takeDamage(int dmg) {
        currentHp -= Math.max(0, dmg);
    }

    @Override
    public int getDexMod() {
        return modifier(monster.getStats().get("DEX"));
    }

    @Override
    public int rollAttack() {
        int mod = modifier(monster.getStats().get("DEX"));
        return Dice.roll(20) + mod;
    }

    @Override
    public int rollDamage() {
        return rollDiceString(monster.getDamageDice());
    }

    private static int rollDiceString(String s) {
        if (s == null) return Dice.roll(6);
        s = s.trim().toLowerCase();

        int dPos = s.indexOf('d');
        if (dPos < 0) return Dice.roll(6);

        int count = 1;
        int sides = 6;

        try {
            String left = s.substring(0, dPos).trim();
            String right = s.substring(dPos + 1).trim();
            if (!left.isEmpty()) count = Integer.parseInt(left);
            if (!right.isEmpty()) sides = Integer.parseInt(right);
        } catch (Exception ignored) { }

        int sum = 0;
        for (int i = 0; i < count; i++) sum += Dice.roll(sides);
        return sum;
    }

    private static int modifier(int value) {
        return (value - 10) / 2;
    }
}

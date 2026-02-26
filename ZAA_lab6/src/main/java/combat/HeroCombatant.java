package combat;

import model.Hero;
import util.Dice;

public class HeroCombatant implements Combatant {
    private final Hero hero;
    private int currentHp;

    public HeroCombatant(Hero hero) {
        this.hero = hero;
        this.currentHp = hero.getHitPoints();
    }

    @Override public String getName() { return hero.getName(); }
    @Override public int getCurrentHp() { return currentHp; }
    @Override public int getMaxHp() { return hero.getHitPoints(); }
    @Override public int getArmorClass() { return hero.getArmorClass(); }
    @Override public boolean isAlive() { return currentHp > 0; }

    @Override
    public void takeDamage(int dmg) {
        currentHp -= Math.max(0, dmg);
    }

    @Override
    public int getDexMod() {
        return modifier(hero.getStats().get("DEX"));
    }

    @Override
    public int rollAttack() {
        int mod = modifier(hero.getStats().get("STR"));
        return Dice.roll(20) + mod;
    }

    @Override
    public int rollDamage() {
        int mod = modifier(hero.getStats().get("STR"));
        return Dice.roll(8) + mod;
    }

    private static int modifier(int value) {
        return (value - 10) / 2;
    }
}
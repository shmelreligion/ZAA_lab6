package combat;

public interface Combatant {
    String getName();
    int getCurrentHp();
    int getMaxHp();
    int getArmorClass();

    int getDexMod();
    int rollAttack();
    int rollDamage();

    boolean isAlive();
    void takeDamage(int dmg);
}

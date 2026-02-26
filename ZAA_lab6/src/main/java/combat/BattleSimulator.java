package combat;

import util.Dice;

public class BattleSimulator {

    public BattleResult fight(Combatant hero, Combatant monster, CombatLog<Combatant> log) {
        int heroInit = Dice.roll(20) + hero.getDexMod();
        int monsterInit = Dice.roll(20) + monster.getDexMod();

        boolean heroFirst = heroInit >= monsterInit;

        int rounds = 0;
        int heroTotalDmg = 0;
        int monsterTotalDmg = 0;

        while (hero.isAlive() && monster.isAlive() && rounds < 50) {
            rounds++;
            log.log("Раунд " + rounds);

            if (heroFirst) {
                heroTotalDmg += doAttack(hero, monster, log, true);
                if (monster.isAlive()) {
                    monsterTotalDmg += doAttack(monster, hero, log, false);
                }
            } else {
                monsterTotalDmg += doAttack(monster, hero, log, false);
                if (hero.isAlive()) {
                    heroTotalDmg += doAttack(hero, monster, log, true);
                }
            }

            log.log("---");
        }

        boolean draw = rounds >= 50 && hero.isAlive() && monster.isAlive();
        String winner;
        if (draw) winner = "НИЧЬЯ";
        else winner = hero.isAlive() ? hero.getName() : monster.getName();

        return new BattleResult(winner, rounds, heroTotalDmg, monsterTotalDmg, draw);
    }

    private int doAttack(Combatant attacker, Combatant defender, CombatLog<Combatant> log, boolean attackerIsHero) {
        int attackRoll = attacker.rollAttack();
        int ac = defender.getArmorClass();

        if (attackRoll == 1) {
            log.log("[" + attacker.getName() + "] бросает атаку: 1 | АС противника: " + ac + " - ПРОМАХ");
            return 0;
        }

        boolean crit = attackRoll == 20;
        boolean hit = attackRoll >= ac;

        if (!hit) {
            log.log("[" + attacker.getName() + "] бросает атаку: " + attackRoll + " | АС противника: " + ac + " - ПРОМАХ");
            return 0;
        }

        int dmg = attacker.rollDamage();
        if (crit) dmg += attacker.rollDamage();

        log.log("[" + attacker.getName() + "] бросает атаку: " + attackRoll + " | АС противника: " + ac + (crit ? " - КРИТ" : " - ПОПАДАНИЕ"));

        defender.takeDamage(dmg);

        String who = attackerIsHero ? "монстра" : "героя";
        log.log("[" + attacker.getName() + "] наносит " + dmg + " урона. HP " + who + ": " + defender.getCurrentHp() + " / " + defender.getMaxHp());

        return dmg;
    }
}

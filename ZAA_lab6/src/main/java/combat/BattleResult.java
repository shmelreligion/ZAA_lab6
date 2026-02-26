package combat;


public class BattleResult {
    public final String winner;
    public final int rounds;
    public final int heroDamage;
    public final int monsterDamage;
    public final boolean draw;

    public BattleResult(String winner, int rounds, int heroDamage, int monsterDamage, boolean draw) {
        this.winner = winner;
        this.rounds = rounds;
        this.heroDamage =heroDamage;
        this.monsterDamage = monsterDamage;
        this.draw = draw;
    }
}

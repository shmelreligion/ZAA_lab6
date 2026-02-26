package org.example;

import api.DndApiClient;
import api.DndService;
import combat.*;
import generator.HeroGenerator;
import model.Hero;
import model.Monster;
import util.JsonStorage;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        DndApiClient client = new DndApiClient();
        DndService service = new DndService(client);

        HeroGenerator generator = new HeroGenerator(service);
        Hero hero = generator.generate();

        System.out.println(hero);

        JsonStorage<Hero> storage = new JsonStorage<>();
        storage.save(hero, "hero.json");
        System.out.println("Герой сохранен в hero.json");

        Monster monster = service.getRandomMonster();

        System.out.println(monster);

        Scanner sc = new Scanner(System.in);
        System.out.print("Начать бой? (y/n): ");
        String ans = sc.nextLine().trim().toLowerCase();
        if (!ans.equals("y")) {
            System.out.println("Выход.");
            return;
        }

        Combatant heroC = new HeroCombatant(hero);
        Combatant monsterC = new MonsterCombatant(monster);
        CombatLog<Combatant> log = new CombatLog<>();

        BattleSimulator sim = new BattleSimulator();
        BattleResult result = sim.fight(heroC, monsterC, log);

        log.printAll();
        log.saveToFile("battle_log.txt");
        System.out.println("Лог сохранен в battle_log,txt");

        System.out.println("ИТОГ СРАЖЕНИЯ");
        System.out.printf("Победитель: %-20s %n", result.winner);
        System.out.printf("Раундов: %-20d %n", result.rounds);
        System.out.printf("Урон (герой): %-18d %n", result.heroDamage);
        System.out.printf("Урон (монстр): %-18d y%n", result.monsterDamage);
    }
}
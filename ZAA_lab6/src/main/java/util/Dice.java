package util;

import java.util.*;

public class Dice {
    private static final Random rnd = new Random();

    public static int roll(int sides) {
        return rnd.nextInt(sides) + 1;
    }

    public static int rollDropLowest(int count, int sides) {
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < count; i++) rolls.add(roll(sides));
        Integer min = Collections.min(rolls);
        rolls.remove(min);
        int sum = 0;
        for (int v : rolls) sum += v;
        return sum;
    }

    public static <T> T pickRandom(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(rnd.nextInt(list.size()));
    }

    public static <T> List<T> pickRandom(List<T> list, int n) {
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(n, copy.size()));
    }
}

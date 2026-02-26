package combat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CombatLog<T extends Combatant> {
    private final List<String> entries = new ArrayList<>();

    public void log(String message) {
        entries.add(message);
    }

    public void printAll() {
        for (String e : entries) System.out.println(e);
    }

    public void saveToFile(String filename) throws IOException {
        Files.write(Path.of(filename), entries);
    }
}

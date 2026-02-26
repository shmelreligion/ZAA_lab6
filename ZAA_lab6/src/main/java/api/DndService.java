package api;

import api.dto.ListResponse;
import api.dto.NamedApiResource;
import com.google.gson.*;

import model.Monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DndService {
    private final DndApiClient client;
    private final Gson gson = new Gson();

    public DndService(DndApiClient client) {
        this.client = client;
    }

    public List<String> getAllRaceIndexes() {
        String json = client.getString("/races");
        ListResponse res = gson.fromJson(json, ListResponse.class);

        List<String> out = new ArrayList<>();
        for (NamedApiResource r : res.results) out.add(r.index);
        return out;
    }

    public List<String> getAllClassIndexes() {
        String json = client.getString("/classes");
        ListResponse res = gson.fromJson(json, ListResponse.class);

        List<String> out = new ArrayList<>();
        for (NamedApiResource c : res.results) out.add(c.index);
        return out;
    }

    public String getRaceName(String index) {
        JsonObject obj = JsonParser.parseString(client.getString("/races/" + index)).getAsJsonObject();
        return optString(obj, "name", index);
    }

    public String getClassName(String index) {
        JsonObject obj = JsonParser.parseString(client.getString("/classes/" + index)).getAsJsonObject();
        return optString(obj, "name", index);
    }

    public Map<String, Integer> getRaceBonuses(String index) {
        JsonObject obj = JsonParser.parseString(client.getString("/races/" + index)).getAsJsonObject();
        Map<String, Integer> bonuses = new HashMap<>();

        JsonArray arr = optArray(obj, "ability_bonuses");
        for (int i = 0; i < arr.size(); i++) {
            JsonObject b = arr.get(i).getAsJsonObject();
            JsonObject ability = b.has("ability_score") && b.get("ability_score").isJsonObject()
                    ? b.getAsJsonObject("ability_score") : null;

            String abbr = abilityToAbbr(ability != null ? optString(ability, "index", "") : "");
            int bonus = optInt(b, "bonus", 0);

            if (!abbr.isEmpty()) bonuses.put(abbr, bonus);
        }

        return bonuses;
    }

    public int getClassHitDie(String index) {
        JsonObject obj = JsonParser.parseString(client.getString("/classes/" + index)).getAsJsonObject();
        return optInt(obj, "hit_die", 8);
    }

    public List<String> getClassSkills(String classIndex) {
        String raw = client.getString("/classes/" + classIndex + "/proficiencies");
        JsonObject obj = JsonParser.parseString(raw).getAsJsonObject();

        JsonArray list = null;
        if (obj.has("results") && obj.get("results").isJsonArray()) list = obj.getAsJsonArray("results");
        if (list == null && obj.has("proficiencies") && obj.get("proficiencies").isJsonArray())
            list = obj.getAsJsonArray("proficiencies");
        if (list == null) list = new JsonArray();

        List<String> skills = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject item = list.get(i).getAsJsonObject();
            String name = optString(item, "name", "");

            // должно быть "Skill:" (без s)
            if (name.toLowerCase().startsWith("skill:")) {
                skills.add(name.substring("Skill:".length()).trim());
            }
        }
        return skills;
    }

    public List<String> getStartingEquipment(String classIndex) {
        List<String> out = new LinkedList<>();

        String raw = client.getString("/classes/" + classIndex + "/starting-equipment");
        JsonObject obj = JsonParser.parseString(raw).getAsJsonObject();

        JsonArray arr = optArray(obj, "starting_equipment");
        for (int i = 0; i < arr.size(); i++) {
            JsonObject it = arr.get(i).getAsJsonObject();
            int qty = optInt(it, "quantity", 1);

            JsonObject eq = it.has("equipment") && it.get("equipment").isJsonObject()
                    ? it.getAsJsonObject("equipment") : new JsonObject();

            String nm = optString(eq, "name", "Unknown");
            out.add(qty + " x " + nm);
        }
        return out;
    }

    public Monster getRandomMonster() {
        String json = client.getString("/monsters");
        ListResponse res = gson.fromJson(json, ListResponse.class);

        List<NamedApiResource> list = new ArrayList<>(res.results);
        Collections.shuffle(list);

        String pickIndex = list.get(0).index;
        JsonObject m = JsonParser.parseString(client.getString("/monsters/" + pickIndex)).getAsJsonObject();

        String name = optString(m, "name", pickIndex);
        double cr = optDouble(m, "challenge_rating", 0.0);
        int hp = optInt(m, "hit_points", 1);
        int ac = parseArmorClass(m.get("armor_class"));

        Map<String, Integer> stats = new HashMap<>();
        stats.put("STR", optInt(m, "strength", 10));
        stats.put("DEX", optInt(m, "dexterity", 10));
        stats.put("CON", optInt(m, "constitution", 10));
        stats.put("INT", optInt(m, "intelligence", 10));
        stats.put("WIS", optInt(m, "wisdom", 10));
        stats.put("CHA", optInt(m, "charisma", 10));

        String damageDice = extractDamageDice(m);
        return new Monster(name, pickIndex, cr, hp, ac, stats, damageDice);
    }

    // ---------- helpers ----------

    private static int parseArmorClass(JsonElement el) {
        if (el == null || el.isJsonNull()) return 10;

        if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber()) return el.getAsInt();

        if (el.isJsonArray()) {
            JsonArray arr = el.getAsJsonArray();
            if (arr.size() > 0 && arr.get(0).isJsonObject()) {
                JsonObject first = arr.get(0).getAsJsonObject();
                return optInt(first, "value", 10);
            }
        }

        if (el.isJsonObject()) {
            return optInt(el.getAsJsonObject(), "value", 10);
        }

        return 10;
    }

    private static String extractDamageDice(JsonObject monsterObj) {
        if (monsterObj.has("actions") && monsterObj.get("actions").isJsonArray()) {
            JsonArray actions = monsterObj.getAsJsonArray("actions");
            for (int i = 0; i < actions.size(); i++) {
                if (!actions.get(i).isJsonObject()) continue;

                JsonObject act = actions.get(i).getAsJsonObject();
                if (!act.has("damage") || !act.get("damage").isJsonObject()) continue;

                JsonObject dmg = act.getAsJsonObject("damage");
                if (!dmg.has("damage_dice") || !dmg.get("damage_dice").isJsonArray()) continue;

                JsonArray dd = dmg.getAsJsonArray("damage_dice");
                for (int j = 0; j < dd.size(); j++) {
                    if (!dd.get(j).isJsonObject()) continue;
                    JsonObject d = dd.get(j).getAsJsonObject();
                    String s = optString(d, "damage_dice", "");
                    if (s.contains("d")) return s;
                }
            }
        }
        return "1d6";
    }

    private static String abilityToAbbr(String apiIndex) {
        if (apiIndex == null) return "";
        switch (apiIndex.toLowerCase()) {
            case "strength": return "STR";
            case "dexterity": return "DEX";
            case "constitution": return "CON";
            case "intelligence": return "INT";
            case "wisdom": return "WIS";
            case "charisma": return "CHA";
            default: return "";
        }
    }

    private static String optString(JsonObject o, String key, String def) {
        if (o == null || !o.has(key) || o.get(key).isJsonNull()) return def;
        try { return o.get(key).getAsString(); } catch (Exception e) { return def; }
    }

    private static int optInt(JsonObject o, String key, int def) {
        if (o == null || !o.has(key) || o.get(key).isJsonNull()) return def;
        try { return o.get(key).getAsInt(); } catch (Exception e) { return def; }
    }

    private static double optDouble(JsonObject o, String key, double def) {
        if (o == null || !o.has(key) || o.get(key).isJsonNull()) return def;
        try { return o.get(key).getAsDouble(); } catch (Exception e) { return def; }
    }

    private static JsonArray optArray(JsonObject o, String key) {
        if (o == null || !o.has(key) || !o.get(key).isJsonArray()) return new JsonArray();
        return o.getAsJsonArray(key);
    }
}
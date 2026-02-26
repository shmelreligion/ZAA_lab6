package model;

import java.util.*;

public class Repository<T extends Entity> {
    private final Map<String, T> storage = new HashMap<>();

    public void save(T entity) {
        storage.put(entity.getIndex(), entity);
    }

    public T findById(String id) {
        return storage.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
}

package ru.otus.java.pro.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = Logger.getLogger(HwCache.class.getName());
    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void put(K key, V value) {
        boolean exists = cache.containsKey(key);
        cache.put(key, value);
        notifyListeners(key, value, exists ? "UPDATED" : "CREATED");
    }

    @Override
    public void remove(K key) {
        V removedObj = cache.remove(key);
        notifyListeners(key, removedObj, "REMOVED");
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (listener == null) {
            return;
        }
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        if (listener == null) return;
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        List<WeakReference<HwListener<K, V>>> toRemove = new ArrayList<>();
        for (WeakReference<HwListener<K, V>> ref : listeners) {
            HwListener<K, V> listener = ref.get();
            if (listener != null) {
                try {
                    listener.notify(key, value, action);
                } catch (Exception e) {
                    logger.log(
                            Level.SEVERE,
                            "Error while notify listener " + listener + " (key=" + key + ", value=" + value
                                    + ", action=" + action + ")",
                            e);
                }
            } else {
                toRemove.add(ref);
            }
        }
        if (!toRemove.isEmpty()) {
            listeners.removeAll(toRemove);
        }
    }
}

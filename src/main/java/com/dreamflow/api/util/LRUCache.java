package com.dreamflow.api.util;
import java.util.LinkedHashMap;
import java.util.Map;

// Deprecated because of using redis cache
@Deprecated
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int capacity;
    public LRUCache(int capacity){
        super(capacity,0.75f,true);
        this.capacity=capacity;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest){
        return size()>capacity;
    }

}

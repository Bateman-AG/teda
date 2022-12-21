package com.brielmayer.teda.map;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class LinkedCaseInsensitiveMap<V> extends LinkedHashMap<String, V> {
    private Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public LinkedCaseInsensitiveMap() {
        this(null);
    }

    public LinkedCaseInsensitiveMap(Locale locale) {
        this.caseInsensitiveKeys = new HashMap<>();
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
        super(initialCapacity);
        this.caseInsensitiveKeys = new HashMap<>(initialCapacity);
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public V put(String key, V value) {
        String oldKey = this.caseInsensitiveKeys.put(this.convertKey(key), key);
        if (oldKey != null && !oldKey.equals(key)) {
            super.remove(oldKey);
        }

        return super.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends V> map) {
        if (!map.isEmpty()) {
            Iterator var2 = map.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<? extends String, ? extends V> entry = (Map.Entry) var2.next();
                this.put(entry.getKey(), entry.getValue());
            }

        }
    }

    public boolean containsKey(Object key) {
        return key instanceof String && this.caseInsensitiveKeys.containsKey(this.convertKey((String) key));
    }

    public V get(Object key) {
        if (key instanceof String) {
            String caseInsensitiveKey = this.caseInsensitiveKeys.get(this.convertKey((String) key));
            if (caseInsensitiveKey != null) {
                return super.get(caseInsensitiveKey);
            }
        }

        return null;
    }

    public V getOrDefault(Object key, V defaultValue) {
        if (key instanceof String) {
            String caseInsensitiveKey = this.caseInsensitiveKeys.get(this.convertKey((String) key));
            if (caseInsensitiveKey != null) {
                return super.get(caseInsensitiveKey);
            }
        }

        return defaultValue;
    }

    public V remove(Object key) {
        if (key instanceof String) {
            String caseInsensitiveKey = this.caseInsensitiveKeys.remove(this.convertKey((String) key));
            if (caseInsensitiveKey != null) {
                return super.remove(caseInsensitiveKey);
            }
        }

        return null;
    }

    public void clear() {
        this.caseInsensitiveKeys.clear();
        super.clear();
    }

    public Object clone() {
        LinkedCaseInsensitiveMap<V> copy = (LinkedCaseInsensitiveMap) super.clone();
        copy.caseInsensitiveKeys = new HashMap<>(this.caseInsensitiveKeys);
        return copy;
    }

    protected String convertKey(String key) {
        return key.toLowerCase(this.locale);
    }
}

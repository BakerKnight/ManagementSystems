package com.example.coursemanagment2;

import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadraticHashTable<K, V> {




    private static class Entry<K, V> {
            final K key;
            V value;

            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        private Entry<K, V>[] table;
        private int size;
        private static final double LOAD_FACTOR = 0.5; // Threshold to resize

        @SuppressWarnings("unchecked")
        public QuadraticHashTable(int capacity) {
            table = new Entry[capacity];
            size = 0;
        }

        private int hash(K key) {
            return Math.abs(key.hashCode() % table.length);
        }

    private int quadraticProbe(int hash, int step) {
        int newHash = (hash + step * step) % table.length;
        if (newHash < 0) {
            newHash += table.length;
        }
        return newHash;
    }

        public void put(K key, V value) {
            if (size >= table.length * LOAD_FACTOR) {
                resize();
            }

            int hash = hash(key);
            int step = 0;

            while (table[hash] != null && !table[hash].key.equals(key)) {
                step++;
                hash = quadraticProbe(hash(key), step);
            }

            if (table[hash] == null) {
                size++;
            }
            table[hash] = new Entry<>(key, value);
        }

    public V get(K key) {
        int hash = hash(key);
        int step = 0;

        while (true) {
            int currentHash = quadraticProbe(hash, step);
            if (table[currentHash] == null) {
                return null;
            }
            if (table[currentHash].key.equals(key)) {
                return table[currentHash].value;
            }
            step++;
        }
    }

        private void resize() {
            Entry<K, V>[] oldTable = table;
            table = Arrays.copyOf(oldTable, oldTable.length * 2);
            size = 0;

            for (Entry<K, V> entry : oldTable) {
                if (entry != null) {
                    put(entry.key, entry.value);
                }
            }
        }

        public int size() {
            return size;
        }

    public List<V> getAllValues() {
        List<V> values = new ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                values.add(entry.value);
            }
        }
        return values;
    }

    public void remove(K studentId) {
        int hash = hash(studentId);
        int step = 0;

        while (table[hash] != null) {
            if (table[hash].key.equals(studentId)) {
                table[hash] = null;
                size--;
                return;
            }
            step++;
            hash = quadraticProbe(hash(studentId), step);
        }
    }

    }

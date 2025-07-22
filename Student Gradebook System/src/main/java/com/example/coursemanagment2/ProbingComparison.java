package com.example.coursemanagment2;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class ProbingComparison {


    static class Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    static class CustomHashTable<K, V> {
        private Entry<K, V>[] table;
        private int size;
        private int collisionCount;
        private static final double LOAD_FACTOR = 0.5;
        private boolean useQuadraticProbing;
        private Map<Integer, Integer> probeLengths = new HashMap<>();

        @SuppressWarnings("unchecked")
        public CustomHashTable(int capacity, boolean useQuadraticProbing) {
            table = new Entry[capacity];
            this.useQuadraticProbing = useQuadraticProbing;
            this.collisionCount = 0;
        }

        private int hash(K key) {
            return Math.abs(key.hashCode() % table.length);
        }

        private int probe(int hash, int step) {
            if (useQuadraticProbing) {
                return (hash + step * step) % table.length;
            } else {
                return (hash + step) % table.length;
            }
        }

        public void put(K key, V value) {
            int hash = hash(key);
            int step = 0;

            while (table[probe(hash, step)] != null && !table[probe(hash, step)].key.equals(key)) {
                collisionCount++;
                probeLengths.put(step, probeLengths.getOrDefault(step, 0) + 1);
                step++;
            }

            if (table[probe(hash, step)] == null) {
                table[probe(hash, step)] = new Entry<>(key, value);
                size++;
            }
        }

        public int size() {
            return size;
        }

        public int getCollisionCount() {
            return collisionCount;
        }

        public void printStatistics() {
            System.out.println("Total collisions: " + collisionCount);
            System.out.println("Load factor at which collisions occurred: " + ((double)size / (double)table.length));
            probeLengths.forEach((k, v) -> System.out.println("Step " + k + " had " + v + " collisions"));
        }
    }

    public static void main(String[] args) {
        int capacity = 2000;
        int numEntries = 1800; // change num of entries here

        CustomHashTable<Integer, Integer> linearTable = new CustomHashTable<>(capacity, false);
        CustomHashTable<Integer, Integer> quadraticTable = new CustomHashTable<>(capacity, true);

        Random rand = new Random();
        long startTime, endTime;

        // Testing Linear Probing
        startTime = System.nanoTime();
        for (int i = 0; i < numEntries; i++) {
            linearTable.put(rand.nextInt(), i);
        }
        endTime = System.nanoTime();
        System.out.println("Linear Probing Time: " + (endTime - startTime) + "ns");
        linearTable.printStatistics();

        // Testing Quadratic Probing
        startTime = System.nanoTime();
        for (int i = 0; i < numEntries; i++) {
            quadraticTable.put(rand.nextInt(), i);
        }
        endTime = System.nanoTime();
        System.out.println("Quadratic Probing Time: " + (endTime - startTime) + "ns");
        quadraticTable.printStatistics();
    }
}
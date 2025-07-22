package com.example.coursemanagment2;

import java.util.HashMap;
import java.util.Map;

public class TestScoresHashTable {


    public Map<Integer, Double> getAllScores() {
        Map<Integer, Double> allScores = new HashMap<>();
        for (Entry entry : table) {
            if (entry != null) {
                allScores.put(entry.key, entry.value);
            }
        }
        return allScores;
    }

    private static class Entry {
        int key;
        double value;

        Entry(int key, double value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry[] table;
    private int size;


    public TestScoresHashTable(int capacity) {
        table = new Entry[capacity];
        size = 0;
    }

    private int hash(int key) {
        return key % table.length;
    }

    public void put(int key, double value) {
        int hash = hash(key);
        int i = 0;

        while (table[(hash + i * i) % table.length] != null) {
            if (table[(hash + i * i) % table.length].key == key) {
                table[(hash + i * i) % table.length].value = value;
                return;
            }
            i++;
        }

        table[(hash + i * i) % table.length] = new Entry(key, value);
        size++;

        if (size >= table.length / 2) {
            resize();
        }
    }

    public boolean testCollisions(int key) {
        int hash = hash(key);
        int i = 0;
        boolean collisionDetected = false;

        while (table[(hash + i * i) % table.length] != null) {
            collisionDetected = true;
            if (table[(hash + i * i) % table.length].key == key) {
                break;
            }
            i++;
        }

        System.out.println("Collisions detected: " + collisionDetected);
        return collisionDetected;
    }

    public Double get(int key) {
        int hash = hash(key);
        int i = 0;

        while (table[(hash + i * i) % table.length] != null) {
            if (table[(hash + i * i) % table.length].key == key) {
                return table[(hash + i * i) % table.length].value;
            }
            i++;
        }

        return null;
    }

    public void remove(int key) {
        int hash = hash(key);
        int i = 0;

        while (table[(hash + i * i) % table.length] != null) {
            if (table[(hash + i * i) % table.length].key == key) {
                table[(hash + i * i) % table.length] = null;
                size--;
                return;
            }
            i++;
        }
    }

    public boolean containsKey(int key) {
        return get(key) != null;
    }


    public int size() {
        return size;
    }

    // Check if the table is empty
    public boolean isEmpty() {
        return size == 0;
    }


    public double getSum() {
        double sum = 0.0;
        for (Entry entry : table) {
            if (entry != null) {
                sum += entry.value;
            }
        }
        return sum;
    }


    private void resize() {
        Entry[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0;

        for (Entry entry : oldTable) {
            if (entry != null) {
                put(entry.key, entry.value);
            }
        }
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Entry entry : table) {
            if (entry != null) {
                sb.append(entry.key).append(": ").append(entry.value).append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("}");
        return sb.toString();
    }
}


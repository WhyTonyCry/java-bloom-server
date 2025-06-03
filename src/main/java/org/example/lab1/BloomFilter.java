package org.example.lab1;

import java.util.BitSet;
import java.util.List;
import java.util.function.Function;

public class BloomFilter<T> {
    private final BitSet bitSet;
    private final int size;
    private final List<Function<T, Integer>> hashFunctions;

    public BloomFilter(int size, List<Function<T, Integer>> hashFunctions) {
        this.size = size;
        this.hashFunctions = hashFunctions;
        this.bitSet = new BitSet(size);
    }

    public void add(T key) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(key)) % size;
            bitSet.set(hash);
        }
    }

    public boolean contains(T key) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(key)) % size;
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }
}


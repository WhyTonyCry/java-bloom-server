package org.example.lab1;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping("/bloom")
public class BloomFilterController {
    private BloomFilter<Object> bloomFilter;
    private String keyType;

    // хеш-функции по названию
    private static final Map<String, Function<Object, Integer>> hashFunctionsRegistry = Map.of(
            "hashCode", obj -> obj.hashCode(),
            "stringLength", obj -> ((String)obj).length(),
            "custom1", obj -> ((String)obj).chars().sum()
    );

    @PostMapping("/init")
    public String init(@RequestBody BloomInitRequest request) {
        // Тип ключа сделал String
        this.keyType = request.keyType;
        List<Function<Object, Integer>> hashFunctions = new ArrayList<>();
        for (String name : request.hashFunctions) {
            if (!hashFunctionsRegistry.containsKey(name)) {
                return "Unknown hash function: " + name;
            }
            hashFunctions.add(hashFunctionsRegistry.get(name));
        }
        this.bloomFilter = new BloomFilter<>(request.size, hashFunctions);
        return "BloomFilter initialized";
    }

    @PostMapping("/add")
    public String add(@RequestBody KeyRequest request) {
        if (bloomFilter == null) return "Not initialized";
        Object key = parseKey(request.key);
        bloomFilter.add(key);
        return "Key added";
    }

    @PostMapping("/contains")
    public Map<String, Boolean> contains(@RequestBody KeyRequest request) {
        if (bloomFilter == null) return Map.of("error", false);
        Object key = parseKey(request.key);
        boolean res = bloomFilter.contains(key);
        return Map.of("contains", res);
    }

    private Object parseKey(String key) {
        switch (keyType) {
            case "int": return Integer.parseInt(key);
            case "string": default: return key;
        }
    }

    // DTO
    public static class BloomInitRequest {
        public int size;
        public String keyType;
        public List<String> hashFunctions;
    }
    public static class KeyRequest {
        public String key;
    }
}

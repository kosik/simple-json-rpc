package io.github.kosik.simplejsonrpc.client.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SecureRandomStringIdGeneratorTest {

    private static final char[] ALPHABET = "0123456789abcdef".toCharArray();
    private SecureRandomStringIdGenerator secureRandomStringIdGenerator;

    @Test
    public void test() {
        secureRandomStringIdGenerator = new SecureRandomStringIdGenerator();
        testSize(40);
    }

    @Test
    public void testSpecificSize() {
        secureRandomStringIdGenerator = new SecureRandomStringIdGenerator(64);
        testSize(64);
    }

    private void testSize(int size) {
        int amount = 100;
        Set<String> ids = new HashSet<>(amount * 2);
        for (int i = 0; i < amount; i++) {
            String id = secureRandomStringIdGenerator.generate();
            System.out.println(id);
            Assertions.assertEquals(id.length(), size);
            for (char c : id.toCharArray()) {
                if (Arrays.binarySearch(ALPHABET, c) == -1) {
                    Assertions.fail("Bad symbol: " + c);
                }
            }
            ids.add(id);
        }
        Assertions.assertEquals(ids.size(), amount);
    }

}
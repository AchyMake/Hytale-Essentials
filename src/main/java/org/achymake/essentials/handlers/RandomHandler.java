package org.achymake.essentials.handlers;

import java.util.Random;

public class RandomHandler {
    private Random getRandom() {
        return new Random();
    }
    private double getRandomDouble() {
        return nextDouble(0.0, 1.0);
    }
    private double getRandomInt() {
        return nextInt(0, 100);
    }
    public boolean isTrue(double chance) {
        return chance >= getRandomDouble();
    }
    public boolean isTrue(int chance) {
        return chance >= getRandomInt();
    }
    public double nextDouble(double origin, double bound) {
        return getRandom().nextDouble(origin, bound);
    }
    public int nextInt(int origin, int bound) {
        return getRandom().nextInt(origin, bound);
    }
}
package de.timmeey.libTimmeey.math;

import java.util.ArrayDeque;
import java.util.Queue;

public class MovingAverage {

    private final int size;
    double total = 0;
    int count = 0;
    Queue<Double> queue;

    public MovingAverage(int size) {
        this.size = size;
        this.queue = new ArrayDeque<>(size);
    }

    public void add(double x) {
        total += x;
        queue.add(x);
        if (queue.size() > size) {
            total -= queue.poll();
        } else {
            count++;
        }
    }

    public void add(float x) {
        total += x;
        queue.add(x + 0.0);
        if (queue.size() > size) {
            total -= queue.poll();
        } else {
            count++;
        }
    }

    public void add(int x) {
        total += x;
        queue.add(x + 0.0);
        if (queue.size() > size) {
            total -= queue.poll();
        } else {
            count++;
        }
    }

    public void add(long x) {
        total += x;
        queue.add(x + 0.0);
        if (queue.size() > size) {
            total -= queue.poll();
        } else {
            count++;
        }
    }

    public void add(short x) {
        total += x;
        queue.add(x + 0.0);
        if (queue.size() > size) {
            total -= queue.poll();
        } else {
            count++;
        }
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getVariance() {
        double mean = getMean();
        double temp = 0;
        for (double a : queue) {
            temp += (a - mean) * (a - mean);
        }
        return temp / count;
    }

    public double getMean() {

        return total / count;
    }

}

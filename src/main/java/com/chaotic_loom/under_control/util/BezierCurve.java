package com.chaotic_loom.under_control.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BezierCurve {
    private final List<float[]> controlPoints;

    public BezierCurve() {
        this.controlPoints = new ArrayList<>();
    }

    public void addControlPoint(float x, float y) {
        this.controlPoints.add(new float[]{x, y});
    }

    public void modifyControlPoint(int index, float x, float y) {
        if (index >= controlPoints.size()) {
            controlPoints.add(new float[]{x, y});
        } else {
            controlPoints.set(index, new float[]{x, y});
        }
    }

    public float[] getPoint(float t, EasingSystem.EasingType easingType) {
        if (controlPoints.size() < 2) {
            throw new IllegalStateException("At least two control points are required.");
        }

        t = applyEasing(t, easingType);
        return calculateBezierPoint(t);
    }

    private float applyEasing(float t, EasingSystem.EasingType easingType) {
        Function<Float, Float> easingFunction = EasingSystem.selectEasingFunction(easingType);
        return easingFunction.apply(t);
    }

    private float[] calculateBezierPoint(float t) {
        int n = controlPoints.size() - 1;
        float[] point = new float[]{0, 0};

        for (int i = 0; i <= n; i++) {
            float coefficient = bernsteinCoefficient(i, n, t);
            point[0] += coefficient * controlPoints.get(i)[0];
            point[1] += coefficient * controlPoints.get(i)[1];
        }

        return point;
    }

    private float bernsteinCoefficient(int i, int n, float t) {
        return binomialCoefficient(n, i) * (float) Math.pow(t, i) * (float) Math.pow(1 - t, n - i);
    }

    private int binomialCoefficient(int n, int k) {
        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - (k - i)) / i;
        }
        return result;
    }
}

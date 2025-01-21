package com.chaotic_loom.under_control.util;

import java.util.function.Function;

public class EasingSystem {
    public enum EasingType {
        LINEAR,
        SINE_IN, SINE_OUT, SINE_IN_OUT,
        QUAD_IN, QUAD_OUT, QUAD_IN_OUT,
        CUBIC_IN, CUBIC_OUT, CUBIC_IN_OUT,
        QUART_IN, QUART_OUT, QUART_IN_OUT,
        QUINT_IN, QUINT_OUT, QUINT_IN_OUT,
        EXPONENTIAL_IN, EXPONENTIAL_OUT, EXPONENTIAL_IN_OUT,
        CIRC_IN, CIRC_OUT, CIRC_IN_OUT,
        BACK_IN, BACK_OUT, BACK_IN_OUT,
        ELASTIC_IN, ELASTIC_OUT, ELASTIC_IN_OUT,
        BOUNCE_IN, BOUNCE_OUT, BOUNCE_IN_OUT,
    }

    public static float getEasedValue(long startTime, long endTime, float startValue, float endValue, EasingType type) {
        return getEasedValue(System.currentTimeMillis(), startTime, endTime, startValue, endValue, type);
    }

    public static float getEasedValue(long currentTime, long startTime, long endTime, float startValue, float endValue, EasingType type) {
        if (currentTime >= endTime) return endValue;
        if (currentTime <= startTime) return startValue;

        float t = (float) (currentTime - startTime) / (endTime - startTime);
        float delta = endValue - startValue;

        Function<Float, Float> easingFunction = selectEasingFunction(type);
        return startValue + delta * easingFunction.apply(t);
    }

    public static float getEasedValue(float progress, float startValue, float endValue, EasingType type) {
        progress = Math.min(1f, Math.max(0f, progress));

        float delta = endValue - startValue;

        Function<Float, Float> easingFunction = selectEasingFunction(type);
        return startValue + delta * easingFunction.apply(progress);
    }

    public static float[] getEasedBezierValue(long startTime, long endTime, BezierCurve bezierCurve, EasingType type) {
        return getEasedBezierValue(System.currentTimeMillis(), startTime, endTime, bezierCurve, type);
    }

    public static float[] getEasedBezierValue(long currentTime, long startTime, long endTime, BezierCurve bezierCurve, EasingType type) {
        return bezierCurve.getPoint(MathHelper.getProgress(currentTime, startTime, endTime), type);
    }

    public static Function<Float, Float> selectEasingFunction(EasingType type) {
        return switch (type) {
            case LINEAR -> t -> t;

            case SINE_IN -> t -> (float) (1 - Math.cos((t * Math.PI) / 2));
            case SINE_OUT -> t -> (float) Math.sin((t * Math.PI) / 2);
            case SINE_IN_OUT -> t -> (float) (-(Math.cos(Math.PI * t) - 1) / 2);

            case QUAD_IN -> t -> t * t;
            case QUAD_OUT -> t -> 1 - (1 - t) * (1 - t);
            case QUAD_IN_OUT -> t -> t < 0.5 ? 2 * t * t : (float) (1 - Math.pow(-2 * t + 2, 2) / 2);

            case CUBIC_IN -> t -> t * t * t;
            case CUBIC_OUT -> t -> 1 - (float) Math.pow(1 - t, 3);
            case CUBIC_IN_OUT -> t -> t < 0.5
                    ? 4 * t * t * t
                    : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;

            case QUART_IN -> t -> t * t * t * t;
            case QUART_OUT -> t -> (float) (1 - Math.pow(1 - t, 4));
            case QUART_IN_OUT -> t -> t < 0.5 ? 8 * t * t * t * t : (float) (1 - Math.pow(-2 * t + 2, 4) / 2);

            case QUINT_IN -> t -> t * t * t * t * t;
            case QUINT_OUT -> t -> (float) (1 - Math.pow(1 - t, 5));
            case QUINT_IN_OUT -> t -> t < 0.5 ? 16 * t * t * t * t * t : (float) (1 - Math.pow(-2 * t + 2, 5) / 2);

            case EXPONENTIAL_IN -> t -> t == 0 ? 0 : (float) Math.pow(2, 10 * t - 10);
            case EXPONENTIAL_OUT -> t -> t == 1 ? 1 : (float) (1 - Math.pow(2, -10 * t));
            case EXPONENTIAL_IN_OUT -> t -> t == 0
                    ? 0
                    : (float) (t == 1
                    ? 1
                    : t < 0.5 ? Math.pow(2, 20 * t - 10) / 2
                    : (2 - Math.pow(2, -20 * t + 10)) / 2);

            case CIRC_IN -> t -> (float) (1 - Math.sqrt(1 - Math.pow(t, 2)));
            case CIRC_OUT -> t -> (float) Math.sqrt(1 - Math.pow(t - 1, 2));
            case CIRC_IN_OUT -> t -> (float) (t < 0.5
                                ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2
                                : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2);

            case BACK_IN -> t -> (float) ((1.70158f + 1) * t * t * t - 1.70158 * t * t);
            case BACK_OUT -> t -> (float) (1 + (1.70158 + 1) * Math.pow(t - 1, 3) + 1.70158f * Math.pow(t - 1, 2));
            case BACK_IN_OUT -> t -> (float) (t < 0.5
                                ? (Math.pow(2 * t, 2) * ((2.5949095f + 1) * 2 * t - 2.5949095f)) / 2
                                : (Math.pow(2 * t - 2, 2) * ((2.5949095f + 1) * (t * 2 - 2) + 2.5949095f) + 2) / 2);

            case ELASTIC_IN -> t -> t == 0
                                ? 0
                                : (float) (t == 1
                                ? 1
                                : -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * ((2 * Math.PI) / 3)));
            case ELASTIC_OUT -> t -> t == 0
                                ? 0
                                : (float) (t == 1
                                ? 1
                                : Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * ((2 * Math.PI) / 3)) + 1);
            case ELASTIC_IN_OUT -> t -> t == 0
                                ? 0
                                : (float) (t == 1
                                ? 1
                                : t < 0.5
                                ? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * ((2 * Math.PI) / 4.5))) / 2
                                : (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * ((2 * Math.PI) / 4.5))) / 2 + 1);

            case BOUNCE_IN -> t -> 1 - selectEasingFunction(EasingType.BOUNCE_OUT).apply(1 - t);
            case BOUNCE_OUT -> t -> {
                if (t < (1 / 2.75f)) {
                    return 7.5625f * t * t;
                } else if (t < (2 / 2.75f)) {
                    return 7.5625f * (t -= 1.5f / 2.75f) * t + 0.75f;
                } else if (t < (2.5f / 2.75f)) {
                    return 7.5625f * (t -= 2.25f / 2.75f) * t + 0.9375f;
                } else {
                    return 7.5625f * (t -= 2.625f / 2.75f) * t + 0.984375f;
                }
            };
            case BOUNCE_IN_OUT -> t -> t < 0.5
                    ? 0.5f * selectEasingFunction(EasingType.BOUNCE_IN).apply(t * 2)
                    : 0.5f * selectEasingFunction(EasingType.BOUNCE_OUT).apply(t * 2 - 1) + 0.5f;
        };
    }
}

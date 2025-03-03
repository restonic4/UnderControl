package com.chaotic_loom.under_control.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that implements a weight-based evaluation system for boolean results.
 * Allows assigning different weights to each result and evaluating whether the combined result
 * exceeds a given threshold.
 */
public class WeightedEvaluator {
    private Map<String, EvaluationResult> results;
    private double totalWeight;
    private double positiveWeight;

    public WeightedEvaluator() {
        this.results = new HashMap<>();
        this.totalWeight = 0.0;
        this.positiveWeight = 0.0;
    }

    /**
     * Adds a result with its associated weight to the evaluator.
     *
     * @param name Identifier name of the result
     * @param weight Weight of the function in the evaluation
     * @param result Boolean result to evaluate
     */
    public void addResult(String name, double weight, boolean result) {
        results.put(name, new EvaluationResult(weight, result));
        totalWeight += weight;
    }

    /**
     * Evaluates a specific result by its name and updates the accumulated weight.
     *
     * @param name Name of the result to evaluate
     * @return Evaluation result
     */
    public boolean evaluateResult(String name) {
        if (!results.containsKey(name)) {
            throw new IllegalArgumentException("Could not find a result with that name: " + name);
        }

        EvaluationResult evaluationResult = results.get(name);
        boolean result = evaluationResult.result;

        if (result) {
            positiveWeight += evaluationResult.weight;
        }

        return result;
    }

    /**
     * Evaluates all registered results and updates the accumulated weight.
     */
    public void evaluateAll() {
        positiveWeight = 0.0;

        for (Map.Entry<String, EvaluationResult> entry : results.entrySet()) {
            EvaluationResult evaluationResult = entry.getValue();
            if (evaluationResult.result) {
                positiveWeight += evaluationResult.weight;
            }
        }
    }

    /**
     * Resets the evaluator, keeping the results but resetting the accumulated weights.
     */
    public void reset() {
        positiveWeight = 0.0;
    }

    /**
     * Gets the current approval percentage.
     *
     * @return Percentage of positive weight over the total (0.0 to 100.0)
     */
    public double getApprovalPercentage() {
        if (totalWeight == 0.0) return 0.0;
        return (positiveWeight / totalWeight) * 100.0;
    }

    /**
     * Checks whether a specific threshold has been exceeded.
     *
     * @param threshold Approval threshold (0.0 to 100.0)
     * @return true if the approval percentage meets or exceeds the threshold
     */
    public boolean isApproved(double threshold) {
        return getApprovalPercentage() >= threshold;
    }

    private static class EvaluationResult {
        double weight;
        boolean result;

        EvaluationResult(double weight, boolean result) {
            this.weight = weight;
            this.result = result;
        }
    }
}
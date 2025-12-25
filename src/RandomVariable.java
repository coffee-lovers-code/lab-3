import java.io.PrintStream;
import java.util.*;
public class RandomVariable {
    public static PrintStream out = System.out;

    private List<Double> values;
    private List<Double> probabilities;

    public RandomVariable(double[] values, double[] probabilities) {
        this.values = new ArrayList<>();
        this.probabilities = new ArrayList<>();
        for (double v : values) this.values.add(v);
        for (double p : probabilities) this.probabilities.add(p);
        normalize();
    }

    private void normalize() {
        for (int i = 0; i < probabilities.size(); i++) {
            if (probabilities.get(i) < 0) probabilities.set(i, 0.0);
        }
        double sum = 0;
        for (double p : probabilities) sum += p;
        if (sum == 0) {
            throw new IllegalArgumentException("Сумма вероятностей равна 0. Проверьте введенные данные.");
        }
        if (Math.abs(sum - 1.0) > 0.00001) {
            for (int i = 0; i < probabilities.size(); i++) {
                probabilities.set(i, probabilities.get(i) / sum);
            }
        }
    }

    public void print() {
        out.println("Число\tВероятность");
        for (int i = 0; i < values.size(); i++) {
            out.printf("%.2f\t%.4f%n", values.get(i), probabilities.get(i));
        }
    }

    public void sort() {
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            pairs.add(new Pair(values.get(i), probabilities.get(i)));
        }
        pairs.sort(Comparator.comparingDouble(a -> a.value));
        values.clear();
        probabilities.clear();
        for (Pair pair : pairs) {
            values.add(pair.value);
            probabilities.add(pair.probability);
        }
    }

    public double expectedValue() {
        double sum = 0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i) * probabilities.get(i);
        }
        return sum;
    }

    public double variance() {
        double mean = expectedValue();
        double var = 0;
        for (int i = 0; i < values.size(); i++) {
            double diff = values.get(i) - mean;
            var += diff * diff * probabilities.get(i);
        }
        return var;
    }

    public double standardDeviation() {
        return Math.sqrt(variance());
    }

    public void square() {
        values.replaceAll(aDouble -> aDouble * aDouble);
    }

    public void multiply(double constant) {
        values.replaceAll(aDouble -> aDouble * constant);
    }

    public void add(double constant) {
        values.replaceAll(aDouble -> aDouble + constant);
    }

    public double median() {
        sort();
        int n = values.size();
        if (n % 2 == 1) {
            return values.get(n / 2);
        } else {
            return (values.get(n/2 - 1) + values.get(n/2)) / 2.0;
        }
    }

    public double mostProbable() {
        int maxIndex = 0;
        for (int i = 1; i < probabilities.size(); i++) {
            if (probabilities.get(i) > probabilities.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return values.get(maxIndex);
    }

    public boolean isNormal() {
        return Math.abs(median() - mostProbable()) < 0.00001;
    }

    public static RandomVariable bernoulli(int n, double p) {
        double[] values = new double[n + 1];
        double[] probs = new double[n + 1];
        for (int k = 0; k <= n; k++) {
            values[k] = k;
            probs[k] = comb(n, k) * Math.pow(p, k) * Math.pow(1-p, n-k);
        }
        return new RandomVariable(values, probs);
    }

    private static double comb(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        double result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }

    public static RandomVariable fromData(double[] data) {
        Map<Double, Integer> freq = new HashMap<>();
        for (double d : data) {
            freq.put(d, freq.getOrDefault(d, 0) + 1);
        }
        double[] values = new double[freq.size()];
        double[] probs = new double[freq.size()];
        int i = 0;
        for (Map.Entry<Double, Integer> entry : freq.entrySet()) {
            values[i] = entry.getKey();
            probs[i] = (double) entry.getValue() / data.length;
            i++;
        }
        return new RandomVariable(values, probs);
    }

    public static RandomVariable add(RandomVariable a, RandomVariable b) {
        if (a.values.size() != b.values.size()) {
            throw new RuntimeException("Разные размеры!");
        }
        double[] newValues = new double[a.values.size()];
        double[] newProbs = new double[a.values.size()];
        for (int i = 0; i < a.values.size(); i++) {
            newValues[i] = a.values.get(i) + b.values.get(i);
            newProbs[i] = a.probabilities.get(i);
        }
        return new RandomVariable(newValues, newProbs);
    }

    public static RandomVariable multiply(RandomVariable a, RandomVariable b) {
        if (a.values.size() != b.values.size()) {
            throw new RuntimeException("Разные размеры!");
        }
        double[] newValues = new double[a.values.size()];
        double[] newProbs = new double[a.values.size()];
        for (int i = 0; i < a.values.size(); i++) {
            newValues[i] = a.values.get(i) * b.values.get(i);
            newProbs[i] = a.probabilities.get(i);
        }
        return new RandomVariable(newValues, newProbs);
    }

    public List<Double> getValues() {
        return new ArrayList<>(values);
    }

    public List<Double> getProbabilities() {
        return new ArrayList<>(probabilities);
    }

    private static class Pair {
        double value;
        double probability;

        Pair(double value, double probability) {
            this.value = value;
            this.probability = probability;
        }
    }
}
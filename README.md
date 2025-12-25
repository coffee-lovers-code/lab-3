## Отчет по лабораторной работе № 3

#### № группы: `ПМ-2501`

#### Выполнил: `Калинин Арсений Александрович`

#### Вариант: `9`

### Cодержание:

- [Постановка задачи](#1-постановка-задачи)
- [Входные и выходные данные](#2-входные-и-выходные-данные)
- [Выбор структуры данных](#3-выбор-структуры-данных)
- [Алгоритм](#4-алгоритм)
- [Программа](#5-программа)

### 1. Постановка задачи

> Разработать класс для работы с дискретными случайными величинами. Класс должен поддерживать основные операции: создание случайной величины по значениям и вероятностям, вычисление математического ожидания, дисперсии, стандартного отклонения, медианы, моды, проверку на нормальность распределения, а также операции сложения и умножения случайных величин.

Задача сводится к созданию объектно-ориентированного решения для работы с дискретными распределениями, которое позволяет выполнять статистические вычисления и преобразования над случайными величинами.

### 2. Входные и выходные данные

#### Данные на вход

1. **При создании объекта:**
   - Массив значений (тип `double`)
   - Массив вероятностей (тип `double`)

2. **Для статических методов:**
   - Два объекта класса `RandomVariable` для операций сложения/умножения
   - Параметры распределения Бернулли (n, p)
   - Массив данных для построения эмпирического распределения

#### Данные на выход

1. **Результаты методов:**
   - Числовые значения (матожидание, дисперсия, стандартное отклонение, медиана, мода) - тип `double`
   - Логическое значение для проверки нормальности - тип `boolean`
   - Новые объекты `RandomVariable` после операций

2. **Печать распределения:**
   - Таблица значений и соответствующих вероятностей

### 3. Выбор структуры данных

Для хранения данных случайной величины используются два списка:
- `List<Double> values` - для хранения значений случайной величины
- `List<Double> probabilities` - для хранения соответствующих вероятностей

Дополнительно используется вспомогательный класс `Pair` для сортировки пар "значение-вероятность".

| Компонент             | Название        | Тип (в Java)                | Назначение                                     |
|-----------------------|-----------------|-----------------------------|------------------------------------------------|
| Значения              | `values`        | `List<Double>`              | Хранение значений случайной величины           |
| Вероятности           | `probabilities` | `List<Double>`              | Хранение вероятностей соответствующих значений |
| Вспомогательный класс | `Pair`          | Вложенный статический класс | Для сортировки пар значение-вероятность        |

### 4. Алгоритм

#### Основные этапы работы класса:

1. **Инициализация объекта:**
   - Принимаются массивы значений и вероятностей
   - Выполняется нормализация вероятностей (приведение к сумме = 1)
   - Проверка на неотрицательность вероятностей

2. **Нормализация вероятностей:**
   - Все отрицательные вероятности устанавливаются в 0
   - Вычисляется сумма всех вероятностей
   - Если сумма не равна 1, все вероятности делятся на сумму

3. **Статистические вычисления:**
   - **Матожидание**: сумма произведений значений на их вероятности
   - **Дисперсия**: сумма квадратов отклонений от матожидания, умноженных на вероятности
   - **Стандартное отклонение**: квадратный корень из дисперсии
   - **Медиана**: центральное значение после сортировки
   - **Мода**: значение с максимальной вероятностью

4. **Операции над величинами:**
   - **Сложение/умножение**: поэлементные операции над значениями с сохранением вероятностей
   - **Преобразования**: умножение/прибавление константы, возведение в квадрат

5. **Специальные распределения:**
   - **Бернулли**: генерация биномиального распределения
   - **Из данных**: построение распределения по эмпирическим данным

### 5. Программа

```java
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
```

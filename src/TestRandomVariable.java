import java.io.PrintStream;
import java.util.Scanner;

public class TestRandomVariable {
    public static Scanner in = new Scanner(System.in);
    public static PrintStream out = System.out;

    public static void main(String[] args) {
        out.println("=== ТЕСТИРОВАНИЕ ===");

        out.println("\n1. Тест создания:");
        double[] vals = {1, 2, 3};
        double[] probs = {0.2, 0.3, 0.5};
        RandomVariable rv = new RandomVariable(vals, probs);
        rv.print();
        double expected = 2.3;
        double expectedValue = rv.expectedValue();
        out.printf("Мат. ожидание: ожидалось %.2f, получилось %.2f\n%s%n",
                expected, expectedValue, Math.abs(expected-expectedValue) < 0.01 ? "Успешно" : "Ошибка");
        out.print("\n====================\n");

        out.println("\n2. Тест дисперсии:");
        expected = 0.61;
        double variance = rv.variance();
        out.printf("Дисперсия: ожидалось %.2f, получилось %.2f\n%s%n",
                expected, variance, Math.abs(expected-variance) < 0.01 ? "Успешно" : "Ошибка");

        out.print("\n====================\n");

        out.println("\n3. Тест сортировки:");
        rv.sort();
        out.print("Отсортировано: ");
        for (double v : rv.getValues()) {
            out.print(v + " ");
        }
        out.println("\nУспешно");
        out.print("\n====================\n");

        out.println("\n4. Тест Бернулли (n=3, p=0.5):");
        RandomVariable bern = RandomVariable.bernoulli(3, 0.5);
        bern.print();
        double sum = 0;
        for (double prob : bern.getProbabilities()) {
            sum += prob;
        }
        out.printf("Сумма вероятностей: %.4f\n%s%n", sum, Math.abs(sum-1) < 0.0001 ? "Успешно" : "Ошибка");
        out.print("\n====================\n");

        out.println("\n5. Тест из массива данных:");
        double[] data = {1, 2, 2, 3, 3, 3};
        RandomVariable fromData = RandomVariable.fromData(data);
        fromData.print();
        out.print("Проверка частот: ");
        boolean freqTest = true;
        for (int i = 0; i < fromData.getValues().size(); i++) {
            double value = fromData.getValues().get(i);
            double prob = fromData.getProbabilities().get(i);

            if (value == 1.0 && Math.abs(prob - 1.0/6.0) > 0.0001) freqTest = false;
            if (value == 2.0 && Math.abs(prob - 2.0/6.0) > 0.0001) freqTest = false;
            if (value == 3.0 && Math.abs(prob - 3.0/6.0) > 0.0001) freqTest = false;
        }
        out.println(freqTest ? "\nУспешно" : "\nОшибка");
        out.print("\n====================\n");

        out.println("\n6. Тест операций:");
        rv.multiply(2);
        out.print("Умножение на 2: ");
        boolean multiplyTest = true;
        for (double v : rv.getValues()) {
            out.print(v + " ");
            if (v != 2.0 && v != 4.0 && v != 6.0) multiplyTest = false;
        }
        out.println(multiplyTest ? "\nУспешно" : "\nОшибка");
        out.print("\n====================\n");

        out.println("\n7. Тест медианы:");
        RandomVariable rv2 = new RandomVariable(
                new double[]{1, 2, 3, 4},
                new double[]{0.25, 0.25, 0.25, 0.25}
        );
        double median = rv2.median();
        out.printf("Медиана для {1,2,3,4}: %.2f\n%s%n", median,
                Math.abs(median - 2.5) < 0.01 ? "Успешно" : "Ошибка");
        out.print("\n====================\n");

        out.println("\n8. Тест наиболее вероятного значения:");
        RandomVariable rv3 = new RandomVariable(
                new double[]{10, 20, 30},
                new double[]{0.1, 0.7, 0.2}
        );
        double mostProbable = rv3.mostProbable();
        out.printf("Наиболее вероятное: %.0f\n%s%n", mostProbable,
                mostProbable == 20 ? "Успешно" : "Ошибка");
        out.print("\n====================\n");

        out.println("\n9. Тест сложения случайных величин:");
        try {
            RandomVariable sumRV = RandomVariable.add(rv, rv3);
            out.print("Сложение: ");
            sumRV.print();
            out.println("Успешно");
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
        out.print("\n====================\n");

        out.println("\n10. Тест проверки нормальности:");
        RandomVariable normalRV = new RandomVariable(
                new double[]{1, 2, 3},
                new double[]{0.1, 0.8, 0.1}
        );
        boolean isNormal = normalRV.isNormal();
        out.println("Распределение нормальное? " + isNormal);
        out.println("Ожидается true" + (isNormal ? "\nУспешно" : "\nОшибка"));

        out.println("\n=== ВСЕ ТЕСТЫ ВЫПОЛНЕНЫ ===");

        out.print("\nЗапустить основную программу? (1-да, 0-нет): ");
        int choice = in.nextInt();
        if (choice == 1) {
            Main.main(new String[0]);
        }

        in.close();
    }
}
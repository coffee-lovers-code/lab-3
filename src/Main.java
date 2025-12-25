import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static Scanner in = new Scanner(System.in);
    public static PrintStream out = System.out;
    public static RandomVariable createRV(){
        out.println("Введите количество значений и вероятностей:");
        int values_count = in.nextInt();
        int probs_count = values_count;
        double[] values = new double[values_count];
        double[] probs = new double[probs_count];
        out.println("Введите " + values_count + " значений через пробел:");
        for(int i = 0; i < values_count; i++){
            values[i] = in.nextDouble();
        }
        out.println("Введите " + probs_count + " вероятностей через пробел:");
        for(int i = 0; i < probs_count; i++){
            probs[i] = in.nextDouble();
        }
        return new RandomVariable(values, probs);
    }

    public static void main(String[] args) {
        out.println("=== РАБОТА СО СЛУЧАЙНЫМИ ВЕЛИЧИНАМИ ===\n");
        RandomVariable rv = createRV();

        while (true) {
            out.println("\n=== МЕНЮ ===");
            out.println("1. Показать случайную величину");
            out.println("2. Отсортировать");
            out.println("3. Мат. ожидание");
            out.println("4. Дисперсия");
            out.println("5. СКО");
            out.println("6. Операции с числами");
            out.println("7. Медиана");
            out.println("8. Наиболее вероятное");
            out.println("9. Проверка нормальности");
            out.println("10. Бернулли");
            out.println("11. Из массива данных");
            out.println("12. Операции с двумя величинами");
            out.println("0. Выход");
            out.print("Выбор: ");
            int choice = in.nextInt();
            switch (choice) {
                case 1:
                    rv.print();
                    break;

                case 2:
                    rv.sort();
                    out.println("Отсортировано:");
                    rv.print();
                    break;

                case 3:
                    out.printf("Мат. ожидание: %.4f%n", rv.expectedValue());
                    break;

                case 4:
                    out.printf("Дисперсия: %.4f%n", rv.variance());
                    break;

                case 5:
                    out.printf("СКО: %.4f%n", rv.standardDeviation());
                    break;

                case 6:
                    out.print("Выберите операцию (1-квадрат, 2-умножить, 3-прибавить): ");
                    int op = in.nextInt();
                    if (op == 1) {
                        rv.square();
                        out.println("Возведено в квадрат");
                    } else if (op == 2) {
                        out.print("Константа: ");
                        double c = in.nextDouble();
                        rv.multiply(c);
                        out.println("Умножено на " + c);
                    } else if (op == 3) {
                        out.print("Константа: ");
                        double c = in.nextDouble();
                        rv.add(c);
                        out.println("Прибавлено " + c);
                    }
                    rv.print();
                    break;

                case 7:
                    out.printf("Медиана: %.4f%n", rv.median());
                    break;

                case 8:
                    out.printf("Наиболее вероятное: %.4f%n", rv.mostProbable());
                    break;

                case 9:
                    if (rv.isNormal()) {
                        out.println("Распределение нормальное");
                    } else {
                        out.println("Распределение не нормальное");
                    }
                    break;

                case 10:
                    out.print("Введите n: ");
                    int n = in.nextInt();
                    out.print("Введите p (0-1): ");
                    double p = in.nextDouble();
                    RandomVariable bern = RandomVariable.bernoulli(n, p);
                    out.println("Распределение Бернулли:");
                    bern.print();
                    break;

                case 11:
                    out.print("Введите количество чисел:");
                    int count = in.nextInt();
                    double[] data = new double[count];
                    out.println("Введите числа:");
                    for (int i = 0; i < count; i++) {
                        data[i] = in.nextDouble();
                    }
                    rv = RandomVariable.fromData(data);
                    out.println("Создано из массива:");
                    rv.print();
                    break;

                case 12:
                    out.println("Создание второй величины");
                    RandomVariable rv2 = createRV();
                    out.print("Операция (1-сложение, 2-умножение): ");
                    int operation = in.nextInt();
                    try {
                        if (operation == 1) {
                            RandomVariable result = RandomVariable.add(rv, rv2);
                            out.println("Сумма:");
                            result.print();
                        } else if (operation == 2) {
                            RandomVariable result = RandomVariable.multiply(rv, rv2);
                            out.println("Произведение:");
                            result.print();
                        }
                    } catch (Exception e) {
                        out.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case 0:
                    out.println("Выход...");
                    in.close();
                    return;

                default:
                    out.println("Неверный выбор");
            }
        }
    }
}
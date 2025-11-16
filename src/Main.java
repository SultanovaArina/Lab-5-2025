import functions.*;
import functions.basic.*;
import functions.meta.*;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Проверка аналитических функций ===");
        checkAnalyticalFunctions();

        System.out.println("\n=== Табулированные функции и сравнение с исходными ===");
        checkTabulatedFunctions();

        System.out.println("\n=== Проверка сериализации (Serializable и Externalizable) ===");
        checkSerialization();
    }

    // --- Задание 8: аналитические функции Sin и Cos ---
    public static void checkAnalyticalFunctions() {
        Function sin = new Sin();
        Function cos = new Cos();

        System.out.println("\nСинус:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("sin(%.2f) = %.4f%n", x, sin.getFunctionValue(x));
        }

        System.out.println("\nКосинус:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("cos(%.2f) = %.4f%n", x, cos.getFunctionValue(x));
        }
    }

    // --- Задание 8: табулирование и сравнение ---
    public static void checkTabulatedFunctions() {
        Function sin = new Sin();
        Function cos = new Cos();

        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);

        // Сумма квадратов табулированных функций
        Function sumSquares = Functions.sum(
                Functions.power(tabSin, 2),
                Functions.power(tabCos, 2)
        );

        System.out.println("\nСравнение исходных и табулированных значений на [0, PI]:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double f = sumSquares.getFunctionValue(x);
            System.out.printf("x=%.2f, sin^2+cos^2=%.4f%n", x, f);
        }

        // Табулирование экспоненты и логарифма
        TabulatedFunction tabExp = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
        TabulatedFunction tabLog = TabulatedFunctions.tabulate(new Log(Math.E), 0, 10, 11);

        // Сохранение и чтение текстового файла (экспонента)
        try (FileWriter writer = new FileWriter("exp_tab.txt")) {
            TabulatedFunctions.writeTabulatedFunction(tabExp, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileReader reader = new FileReader("exp_tab.txt")) {
            TabulatedFunction readExp = TabulatedFunctions.readTabulatedFunction(reader);
            System.out.println("\nСравнение исходной и считанной табулированной экспоненты:");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f, original=%.4f, read=%.4f%n",
                        x, tabExp.getFunctionValue(x), readExp.getFunctionValue(x));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сохранение и чтение бинарного файла (логарифм)
        try (FileOutputStream out = new FileOutputStream("log_tab.bin")) {
            TabulatedFunctions.outputTabulatedFunction(tabLog, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream in = new FileInputStream("log_tab.bin")) {
            TabulatedFunction readLog = TabulatedFunctions.inputTabulatedFunction(in);
            System.out.println("\nСравнение исходного и считанного табулированного логарифма:");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f, original=%.4f, read=%.4f%n",
                        x, tabLog.getFunctionValue(x), readLog.getFunctionValue(x));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Задание 9: сериализация Serializable и Externalizable ---
    public static void checkSerialization() {
        try {
            // Создаем композицию Log(Exp(x))
            Function log = new Log(Math.E);
            Function exp = new Exp();
            TabulatedFunction tabFunc = TabulatedFunctions.tabulate(
                    new Composition(log, exp), 0, 10, 11
            );

            // --- Serializable ---
            ObjectOutputStream outSer = new ObjectOutputStream(new FileOutputStream("func_ser.dat"));
            outSer.writeObject(tabFunc);
            outSer.close();

            ObjectInputStream inSer = new ObjectInputStream(new FileInputStream("func_ser.dat"));
            TabulatedFunction readSer = (TabulatedFunction) inSer.readObject();
            inSer.close();

            System.out.println("\nСравнение исходной и десериализованной (Serializable) функции:");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f, original=%.4f, read=%.4f%n",
                        x, tabFunc.getFunctionValue(x), readSer.getFunctionValue(x));
            }

            // --- Externalizable ---
            ObjectOutputStream outExt = new ObjectOutputStream(new FileOutputStream("func_ext.dat"));
            outExt.writeObject(tabFunc);
            outExt.close();

            ObjectInputStream inExt = new ObjectInputStream(new FileInputStream("func_ext.dat"));
            TabulatedFunction readExt = (TabulatedFunction) inExt.readObject();
            inExt.close();

            System.out.println("\nСравнение исходной и десериализованной (Externalizable) функции:");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f, original=%.4f, read=%.4f%n",
                        x, tabFunc.getFunctionValue(x), readExt.getFunctionValue(x));
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


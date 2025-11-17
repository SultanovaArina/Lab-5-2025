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

    // Задание 9: сериализация Serializable и Externalizable
    public static void checkSerialization() {
        try {
              // Serializable для ArrayTabulatedFunction
             TabulatedFunction arrayFunc = TabulatedFunctions.tabulate( new Composition(new Log(Math.E), new Exp()), 0, 10, 11  );

            ObjectOutputStream outSer = new ObjectOutputStream(new FileOutputStream("array_ser.dat"));
            outSer.writeObject(arrayFunc);  // Serializable
            outSer.close();

            ObjectInputStream inSer = new ObjectInputStream(new FileInputStream("array_ser.dat"));
            TabulatedFunction readArray = (TabulatedFunction) inSer.readObject();
            inSer.close();

            System.out.println("\nSerializable (Array) проверка:");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f, original=%.4f, read=%.4f%n",
                        x, arrayFunc.getFunctionValue(x), readArray.getFunctionValue(x));
            }
            // Externalizable для LinkedListTabulatedFunction
            Function func = new Composition(new Log(Math.E), new Exp());
            double left = 0;
            double right = 10;
            int pointsCount = 11;

            // Вычисляем значения функции в точках
            double[] values = new double[pointsCount];
            double step = (right - left) / (pointsCount - 1);
            for (int i = 0; i < pointsCount; i++) {
                double x = left + i * step;
                values[i] = func.getFunctionValue(x);
            }

            // Создаём LinkedListTabulatedFunction с готовыми значениями
            LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(left, right, values);

            // Сериализация Externalizable
            ObjectOutputStream outExt = new ObjectOutputStream(new FileOutputStream("list_ext.dat"));
            outExt.writeObject(listFunc);  // вызов writeExternal()
            outExt.close();

            // Десериализация
            ObjectInputStream inExt = new ObjectInputStream(new FileInputStream("list_ext.dat"));
            LinkedListTabulatedFunction readList = (LinkedListTabulatedFunction) inExt.readObject(); // вызов readExternal()
            inExt.close();

            System.out.println("\nExternalizable (LinkedList) проверка:");
            for (double x = 0; x <= 10; x += 1) {
                System.out.printf("x=%.1f, original=%.4f, read=%.4f%n",
                        x, listFunc.getFunctionValue(x), readList.getFunctionValue(x));
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}


import functions.*;

public class Main {
    public static void main(String[] args) {

        double[] values = {0, 1, 8, 27, 64};

        System.out.println("ТЕСТ ArrayTabulatedFunction ");
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(0, 4, values);
        testFunction(arrayFunction);
        testExceptions(arrayFunction);

        System.out.println("\nТЕСТ LinkedListTabulatedFunction");
        TabulatedFunction listFunction = new LinkedListTabulatedFunction(0, 4, values);
        testFunction(listFunction);
        testExceptions(listFunction);
    }

    // Проверка основных операций: получение, изменение, удаление, добавление
    public static void testFunction(TabulatedFunction func) {
        System.out.println("\n Текущее состояние функции (" + func.getClass().getSimpleName() + ") ");
        printFunction(func);

        System.out.println("\nПроверка вычисления значения функции:");
        System.out.println("f(2.7) = " + func.getFunctionValue(2.7));

        System.out.println("\nИзменение значения Y:");
        System.out.println("Меняем Y точки с индексом 3 на 50.0");
        func.setPointY(3, 50.0);
        printFunction(func);
        func.setPointY(3, 27.0); // возвращаем исходное значение

        System.out.println("\nУдаление точки:");
        System.out.println("Удаляем точку с индексом 1");
        func.deletePoint(1);
        printFunction(func);

        System.out.println("\nДобавление точки:");
        System.out.println("Добавляем точку (1.0; 1.0) обратно");
        try {
            func.addPoint(new FunctionPoint(1.0, 1.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Не удалось добавить точку: " + e.getMessage());
        }
        printFunction(func);

        System.out.println("\nПроверка setPoint() с некорректным X:");
        try {
            System.out.println("Попытка заменить точку с индексом 2 на (0.5; 0.125)");
            func.setPoint(2, new FunctionPoint(0.5, 0.125));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }
    }

    // Проверка выбрасывания исключений
    public static void testExceptions(TabulatedFunction func) {
        System.out.println("\n Тестирование исключений для " + func.getClass().getSimpleName() );

        int totalPoints = func.getPointsCount();

        // 1. Индекс за пределами
        try {
            System.out.println("Попытка получить точку с индексом -1");
            func.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        try {
            System.out.println("Попытка получить точку с индексом " + totalPoints);
            func.getPoint(totalPoints);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 2. Некорректное изменение X
        try {
            System.out.println("Попытка изменить X точки 2 на 0.8 (нарушает порядок)");
            func.setPointX(2, 0.8);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 3. Добавление точки с существующим X
        try {
            System.out.println("Попытка добавить точку с X=3.0 (уже существует)");
            func.addPoint(new FunctionPoint(3.0, 27.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 4. Удаление из функции с 2 точками
        try {
            System.out.println("Попытка удалить точку из функции с двумя точками");
            TabulatedFunction smallFunc;
            if (func instanceof ArrayTabulatedFunction) {
                smallFunc = new ArrayTabulatedFunction(0, 1, new double[]{0, 1});
            } else {
                smallFunc = new LinkedListTabulatedFunction(0, 1, new double[]{0, 1});
            }
            smallFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 5. Некорректные параметры конструктора
        try {
            System.out.println("Попытка создать функцию с левым >= правого и меньше двух точек");
            TabulatedFunction wrongFunc = new ArrayTabulatedFunction(5, 1, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }
    }

    // Вывод точек функции
    public static void printFunction(TabulatedFunction func) {
        int totalPoints = func.getPointsCount();
        System.out.println("Функция состоит из " + totalPoints + " точек:");
        for (int i = 0; i < totalPoints; i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.println("Точка " + i + ": (" + p.getX() + "; " + p.getY() + ")");
        }
    }
}

import functions.*;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {

        // Создание тестовых объектов
        double[] values1 = {1.0, 2.0, 3.0, 4.0};
        double[] values2 = {1.0, 2.0, 3.1, 4.0}; // немного изменённая точка для проверки equals и hashCode

        ArrayTabulatedFunction arrayFunc1 = new ArrayTabulatedFunction(0.0, 3.0, values1);
        ArrayTabulatedFunction arrayFunc2 = new ArrayTabulatedFunction(0.0, 3.0, values1);
        ArrayTabulatedFunction arrayFunc3 = new ArrayTabulatedFunction(0.0, 3.0, values2);

        LinkedListTabulatedFunction listFunc1 = new LinkedListTabulatedFunction(0.0, 3.0, values1);
        LinkedListTabulatedFunction listFunc2 = new LinkedListTabulatedFunction(0.0, 3.0, values1);
        LinkedListTabulatedFunction listFunc3 = new LinkedListTabulatedFunction(0.0, 3.0, values2);

        // Проверка toString()
        System.out.println("\ntoString() ");
        System.out.println("ArrayTabulatedFunction: " + arrayFunc1);
        System.out.println("LinkedListTabulatedFunction: " + listFunc1);

        // Проверка equals()
        System.out.println("\nequals()");
        System.out.println("arrayFunc1.equals(arrayFunc2): " + arrayFunc1.equals(arrayFunc2)); // true
        System.out.println("arrayFunc1.equals(arrayFunc3): " + arrayFunc1.equals(arrayFunc3)); // false
        System.out.println("arrayFunc1.equals(listFunc1): " + arrayFunc1.equals(listFunc1));   // true
        System.out.println("listFunc1.equals(listFunc3): " + listFunc1.equals(listFunc3));     // false

        // Проверка hashCode()
        System.out.println("\nhashCode()");
        System.out.println("arrayFunc1.hashCode(): " + arrayFunc1.hashCode());
        System.out.println("arrayFunc2.hashCode(): " + arrayFunc2.hashCode());
        System.out.println("arrayFunc3.hashCode(): " + arrayFunc3.hashCode());
        System.out.println("listFunc1.hashCode(): " + listFunc1.hashCode());
        System.out.println("listFunc2.hashCode(): " + listFunc2.hashCode());
        System.out.println("listFunc3.hashCode(): " + listFunc3.hashCode());

        // Проверка clone()
        System.out.println("\n clone() и проверка глубокого клонирования");
        ArrayTabulatedFunction arrayClone = (ArrayTabulatedFunction) arrayFunc1.clone();
        LinkedListTabulatedFunction listClone = (LinkedListTabulatedFunction) listFunc1.clone();

        System.out.println("Клоны до изменения исходных объектов:");
        System.out.println("arrayClone: " + arrayClone);
        System.out.println("listClone: " + listClone);

        // Изменяем исходные объекты
        arrayFunc1.setPointY(0, 10.0);
        listFunc1.setPointY(0, 10.0);

        System.out.println("\nПосле изменения исходных объектов:");
        System.out.println("Исходный arrayFunc1: " + arrayFunc1);
        System.out.println("Клон arrayClone (должен остаться прежним): " + arrayClone);

        System.out.println("Исходный listFunc1: " + listFunc1);
        System.out.println("Клон listClone (должен остаться прежним): " + listClone);

        // Проверка equals() между клоном и исходным после изменения
        System.out.println("\nПроверка equals() после изменения исходных объектов:");
        System.out.println("arrayFunc1.equals(arrayClone): " + arrayFunc1.equals(arrayClone)); // false
        System.out.println("listFunc1.equals(listClone): " + listFunc1.equals(listClone));     // false
    }
}

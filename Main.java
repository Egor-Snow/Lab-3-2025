import functions.*;

public class Main {

    public static void main(String[] args) throws InappropriateFunctionPointException {


        TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, 5);

        System.out.println("Создана функция: " + func.getClass().getName());
        System.out.println("Количество точек: " + func.getPointsCount());
        System.out.println("\n--- Проверим работу классов с корректными значениями ---");
        // Присваиваем функции значения: y = x^2+2
        for (int i = 0; i < func.getPointsCount(); i++) {
            double x = func.getPointX(i);
            func.setPointY(i, x * x + 2);
        }

        // Выводим все точки функции
        System.out.println("Исходные точки функции:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.printf("(%.2f; %.2f)%n", p.getX(), p.getY());
        }

        // Проверяем работу getFunctionValue с разными x
        System.out.println("\nПроверка getFunctionValue:");
        double[] testX = {-5, 0, 2.5, 5, 7.5, 10, 12};
        for (double x : testX) {
            double y = func.getFunctionValue(x);
            System.out.printf("f(%.2f) = %.2f%n", x, y);
        }

        // Проверяем изменение точки
        System.out.println("\nИзменяем значение точки (вторая точка, Y = 100):");
        func.setPointY(1, 100);
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.printf("(%.2f; %.2f)%n", p.getX(), p.getY());
        }

        // Добавляем новую точку
        System.out.println("\nДобавляем новую точку (5.5; 30.25):");
        func.addPoint(new FunctionPoint(5.5, 30.25));

        // Выводим все точки после добавления
        System.out.println("После добавления:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.printf("(%.2f; %.2f)%n", p.getX(), p.getY());
        }

        // Удаляем точку
        System.out.println("\nУдаляем точку с индексом 4:");
        func.deletePoint(4);

        // Выводим все точки после удаления
        System.out.println("После удаления:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.printf("(%.2f; %.2f)%n", p.getX(), p.getY());
        }

        System.out.println("\n--- Проверка работы исключений ---");
        // 1. Тест: IllegalArgumentException заданы неверные границы
        try {
            TabulatedFunction badFunc1 = new ArrayTabulatedFunction(5.0, 1.0, 10);
            System.out.println("ОШИБКА: Должно быть IllegalArgumentException (неверные границы)");
        } catch (IllegalArgumentException e) {
            System.out.println("УСПЕХ (IllegalArgumentException): " + e.getMessage());
        }
        // 1. Тест: IllegalArgumentException задано неверное количество точек
        try {
            TabulatedFunction badFunc2 = new ArrayTabulatedFunction(1.0, 5.0, 1);
            System.out.println("ОШИБКА: Должно быть IllegalArgumentException (мало точек)");
        } catch (IllegalArgumentException e) {
            System.out.println("УСПЕХ (IllegalArgumentException): " + e.getMessage());
        }

        // 3. Тест: FunctionPointIndexOutOfBoundsException. Обращение к точке по неверному индексу
        try {
            func.getPoint(99); // Индекс 99 заведомо не существует
            System.out.println("ОШИБКА: Должно быть FunctionPointIndexOutOfBoundsException");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("УСПЕХ (FunctionPointIndexOutOfBoundsException): " + e.getMessage());
        }

        // 4. Тест: InappropriateFunctionPointException (нарушение порядка). Нарушаем порядок точек, устанавливаем в x с индексом 2 значение меньшее предыдущего x
        try {
            func.setPointX(2, 0.5);
            System.out.println("ОШИБКА: Должно быть InappropriateFunctionPointException (нарушение порядка)");
        } catch (Exception e) { // Ловим общее, т.к. setPointX бросает 2 типа
            if (e instanceof InappropriateFunctionPointException) {
                System.out.println("УСПЕХ (InappropriateFunctionPointException): " + e.getMessage());
            } else {
                System.out.println("ОШИБКА: Поймано неверное исключение " + e.getClass().getName());
            }
        }

        // 5. Тест: InappropriateFunctionPointException. Пробуем добавить уже существующий x
        try {
            func.addPoint(new FunctionPoint(5.00, 27.00));
            System.out.println("ОШИБКА: Должно быть InappropriateFunctionPointException (дубликат X)");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("УСПЕХ (InappropriateFunctionPointException): " + e.getMessage());
        }

        // 6. Тест: IllegalStateException. Удаление точек при кол-ве точек < 3
        try {
            func.deletePoint(0);
            func.deletePoint(0);
            func.deletePoint(0);
            func.deletePoint(0);
            System.out.println("ОШИБКА: Должно быть IllegalStateException");
        } catch (IllegalStateException e) {
            System.out.println("УСПЕХ (IllegalStateException): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ОШИБКА: Поймано неверное исключение " + e.getClass().getName());
        }

        System.out.println("--- Проверка завершена ---");
    }
}

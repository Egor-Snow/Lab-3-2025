package functions;

// Класс FunctionPoint описывает точку функции
public class FunctionPoint {

    // абсцисса
    private double x;

    // ордината
    private double y;

    // Конструктор, создающий точку с заданными координатами.
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Конструктор копирования, создает новую точку с такими же координатами, как у переданной точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    // Конструктор создаёт точку с координатами (0; 0)
    public FunctionPoint() {
        this(0.0, 0.0);
    }

    // Геттер - возвращает значение абсциссы данной точки
    public double getX() {
        return x;
    }

    // Сеттер - устанавливает новое значение абсциссы данной точки
    public void setX(double x) {
        this.x = x;
    }

    // Геттер - возвращает значение ординаты данной точки
    public double getY() {
        return y;
    }

    // Сеттер - устанавливает новое значение ординаты данной точки
    public void setY(double y) {
        this.y = y;
    }
}

package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction  {
    private static final double EPS = 1e-9;

    //  Внутренний класс узла списка
    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode next;
        FunctionNode prev;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }

    private FunctionNode head;
    private int pointsCount;

    // Конструктор по умолчанию (создает пустой список)
    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        pointsCount = 0;
    }

    // Конструктор с количеством точек (y = 0)
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this();
        if (leftX >= rightX - EPS)
            throw new IllegalArgumentException("Left border must be less than right border");
        if (pointsCount < 2)
            throw new IllegalArgumentException("At least two points required");

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, 0.0));
        }
    }

    // Конструктор с массивом значений
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();
        if (leftX >= rightX - EPS)
            throw new IllegalArgumentException("Left border must be less than right border");
        if (values.length < 2)
            throw new IllegalArgumentException("At least two points required");

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }

    // Добавление узла в конец списка
    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);
        newNode.prev = head.prev;
        newNode.next = head;
        head.prev.next = newNode;
        head.prev = newNode;
        pointsCount++;
        return newNode;
    }

    // Поиск узла по индексу
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);

        FunctionNode current;
        if (index < pointsCount / 2) {
            current = head.next;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = head.prev;
            for (int i = pointsCount - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    // Получение границ функции
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    // Количество точек
    public int getPointsCount() {
        return pointsCount;
    }

    // Линейная интерполяция
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        FunctionNode current = head.next;
        while (current.next != head) {
            double x1 = current.point.getX();
            double y1 = current.point.getY();
            double x2 = current.next.point.getX();
            double y2 = current.next.point.getY();

            if (Math.abs(x - x1) < EPS)
                return y1;
            if (x > x1 && x < x2)
                return y1 + (y2 - y1) * ((x - x1) / (x2 - x1));

            current = current.next;
        }
        return Double.NaN;
    }

    // Получение точки
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    // Изменение всей точки
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();

        if ((index > 0 && newX <= getNodeByIndex(index - 1).point.getX() + EPS) ||
                (index < pointsCount - 1 && newX >= getNodeByIndex(index + 1).point.getX() - EPS)) {
            throw new InappropriateFunctionPointException("New X breaks order of points");
        }

        node.point = new FunctionPoint(point);
    }

    // Получение X
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }
    // Получение Y
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    // Изменение X
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if ((index > 0 && x <= getNodeByIndex(index - 1).point.getX() + EPS) ||
                (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).point.getX() - EPS)) {
            throw new InappropriateFunctionPointException("New X breaks order of points");
        }

        node.point.setX(x);
    }

    // Изменение Y
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }

    // Удаление точки
    public void deletePoint(int index) {
        if (pointsCount < 3)
            throw new IllegalStateException("Cannot delete: at least 3 points required");

        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        pointsCount--;
    }

    // Добавление новой точки (по значению)
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();

        // Проверяем, нет ли точки с таким же X
        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(current.point.getX() - newX) < EPS)
                throw new InappropriateFunctionPointException("Point with same X already exists");
            current = current.next;
        }

        // Ищем место для вставки
        current = head.next;
        while (current != head && current.point.getX() < newX)
            current = current.next;

        // Вставляем новую точку
        FunctionNode newNode = new FunctionNode(new FunctionPoint(point));
        newNode.next = current;
        newNode.prev = current.prev;
        current.prev.next = newNode;
        current.prev = newNode;

        pointsCount++;
    }
}

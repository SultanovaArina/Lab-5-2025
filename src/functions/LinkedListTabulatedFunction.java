package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private FunctionNode head;
    private int size;

    private static class FunctionNode {
        public FunctionPoint point;
        public FunctionNode prev;
        public FunctionNode next;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }
    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
    }
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        }

        FunctionNode node;
        if (index < size / 2) {
            node = head.next;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = head.prev;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }
        return node;
    }
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint());
        FunctionNode last = head.prev;

        last.next = newNode;
        newNode.prev = last;
        newNode.next = head;
        head.prev = newNode;

        size++;
        return newNode;
    }
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        }

        FunctionNode nextNode = (index == size) ? head : getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;

        FunctionNode newNode = new FunctionNode(new FunctionPoint());
        newNode.prev = prevNode;
        newNode.next = nextNode;

        prevNode.next = newNode;
        nextNode.prev = newNode;

        size++;
        return newNode;
    }
    private FunctionNode deleteNodeByIndex(int index) {
        if (size < 3) {
            throw new IllegalStateException("Нельзя удалить точку — останется меньше трёх");
        }

        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;

        size--;
        return node;
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.size = 0;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            FunctionNode node = addNodeToTail();
            node.point.setX(leftX + i * step);
            node.point.setY(0.0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.size = 0;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            FunctionNode node = addNodeToTail();
            node.point.setX(leftX + i * step);
            node.point.setY(values[i]);
        }
    }
    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.point);
    }
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double x = point.getX();

        // Проверка соседей
        if ((index > 0 && x <= getNodeByIndex(index - 1).point.getX()) ||
                (index < size - 1 && x >= getNodeByIndex(index + 1).point.getX())) {
            throw new InappropriateFunctionPointException("Неверная координата X");
        }

        node.point = new FunctionPoint(point);
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if ((index > 0 && x <= getNodeByIndex(index - 1).point.getX()) ||
                (index < size - 1 && x >= getNodeByIndex(index + 1).point.getX())) {
            throw new InappropriateFunctionPointException("Неверное значение X");
        }

        node.point.setX(x);
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();

        for (int i = 0; i < size; i++) {
            if (getNodeByIndex(i).point.getX() == x) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }

        int insertIndex = 0;
        while (insertIndex < size && getPointX(insertIndex) < x) {
            insertIndex++;
        }

        FunctionNode node = addNodeByIndex(insertIndex);
        node.point = new FunctionPoint(point);
    }
    public void deletePoint(int index) {
        if (size <= 2) {
            throw new IllegalStateException("Нельзя удалить точку — останется меньше трёх");
        }
        deleteNodeByIndex(index);
    }
    public double getFunctionValue(double x) {
        if (x < getPointX(0) || x > getPointX(size - 1)) {
            return Double.NaN;
        }

        for (int i = 0; i < size - 1; i++) {
            double x0 = getPointX(i);
            double x1 = getPointX(i + 1);
            double y0 = getPointY(i);
            double y1 = getPointY(i + 1);

            if (x == x0) return y0;
            if (x == x1) return y1;
            if (x > x0 && x < x1) {
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }
        return Double.NaN;
    }
    public int getPointsCount() {
        return size;
    }

    public double getLeftDomainBorder() {
        return getPointX(0);
    }

    public double getRightDomainBorder() {
        return getPointX(size - 1);
    }

}


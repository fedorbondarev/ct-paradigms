package queue;


import java.util.Arrays;
import java.util.Objects;

/*
Model: a [1 ... size]
Invariant: size >= 0 && for all (i in 1..size) a[i] != null

Let immutable(n): for all (i in 1..n) a'[i] == a[i]
 */
public class ArrayQueueModule {
    private static int head = 0;
    private static int tail = 0;
    private static Object[] elements = {null};

    // Pred: element != null
    // Post: size' == size + 1 && a[size'] == element && immutable(size)
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);

        ensureCapacity(size() + 1);
        elements[tail] = element;

        tail = (tail + 1) % elements.length;
    }

    private static void ensureCapacity(int n) {
        if (n + 1 > elements.length) {
            int l = elements.length;
            elements = Arrays.copyOf(elements, n * 2);

            if (tail < head) {
                for (int i = l - 1; i >= head; i--) {
                    elements[elements.length - l + i] = elements[i];
                    elements[i] = null;
                }

                head += elements.length - l;
            }
        }
    }

    // Pred: size > 0
    // Post: R = a[1] && size' == size && immutable(size)
    public static Object element() {
        assert !isEmpty();
        return elements[head];
    }

    // Pred: size > 0
    // Post: R == a[1] && size' == size - 1 && for all (i in 1..size') a'[i] == a[i + 1]
    public static Object dequeue() {
        assert !isEmpty();

        Object result = elements[head];

        elements[head] = null;
        head = (head + 1) % elements.length;

        return result;
    }

    // Pred: true
    // Post: R == size && immutable(size) && size' == size
    public static int size() {
        if (head > tail) {
            return elements.length - head + tail;
        }
        return tail - head;
    }

    // Pred: true
    // Post: R == (size == 0) && immutable(size) && size' == size
    public static boolean isEmpty() {
        return head == tail;
    }


    // Pred: true
    // Post: size' == 0
    public static void clear() {
        head = 0;
        tail = 0;
        elements = new Object[]{null};
    }

    // Pred: true
    // Post: R == '[' + голова + ', ' + ... + ', ' + хвост + ']' && immutable(size)
    public static String toStr() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");

        int queueSize = size();
        if (queueSize > 0) {
            int i = head;
            for (int c = 0; c < queueSize - 1; c++) {
                stringBuilder.append(elements[i]);
                stringBuilder.append(", ");
                i = (i + 1) % elements.length;
            }

            stringBuilder.append(elements[i]);
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}

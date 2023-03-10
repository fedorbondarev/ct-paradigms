package queue;


import java.util.Arrays;
import java.util.Objects;

/*
Model: a [1 ... size]
Invariant: size >= 0 && for all (i in 1..size) a[i] != null

Let immutable(n): for all (i in 1..n) a'[i] == a[i]
 */
public class ArrayQueueADT {
    private int head = 0;
    private int tail = 0;
    private Object[] elements = {null};

    // Pred: element != null
    // Post: size' == size + 1 && a[size'] == element && immutable(size)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);

        ensureCapacity(queue, size(queue) + 1);
        queue.elements[queue.tail] = element;

        queue.tail = (queue.tail + 1) % queue.elements.length;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int n) {
        if (n + 1 > queue.elements.length) {
            int l = queue.elements.length;
            queue.elements = Arrays.copyOf(queue.elements, n * 2);

            if (queue.tail < queue.head) {
                for (int i = l - 1; i >= queue.head; i--) {
                    queue.elements[queue.elements.length - l + i] = queue.elements[i];
                    queue.elements[i] = null;
                }

                queue.head += queue.elements.length - l;
            }
        }
    }

    // Pred: size > 0
    // Post: R = a[1] && size' == size && immutable(size)
    public static Object element(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        return queue.elements[queue.head];
    }

    // Pred: size > 0
    // Post: R == a[1] && size' == size - 1 && for all (i in 1..size') a'[i] == a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert !isEmpty(queue);

        Object result = queue.elements[queue.head];

        queue.elements[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elements.length;

        return result;
    }

    // Pred: true
    // Post: R == size && immutable(size) && size' == size
    public static int size(ArrayQueueADT queue) {
        if (queue.head > queue.tail) {
            return queue.elements.length - queue.head + queue.tail;
        }
        return queue.tail - queue.head;
    }

    // Pred: true
    // Post: R == (size == 0) && immutable(size) && size' == size
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.head == queue.tail;
    }


    // Pred: true
    // Post: size' == 0
    public static void clear(ArrayQueueADT queue) {
        queue.head = 0;
        queue.tail = 0;
        queue.elements = new Object[]{null};
    }

    // Pred: true
    // Post: R == '[' + голова + ', ' + ... + ', ' + хвост + ']' && immutable(size)
    public static String toStr(ArrayQueueADT queue) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");

        int queueSize = size(queue);
        if (queueSize > 0) {
            int i = queue.head;
            for (int c = 0; c < queueSize - 1; c++) {
                stringBuilder.append(queue.elements[i]);
                stringBuilder.append(", ");
                i = (i + 1) % queue.elements.length;
            }

            stringBuilder.append(queue.elements[i]);
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}

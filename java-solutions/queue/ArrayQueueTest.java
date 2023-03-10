package queue;

public class ArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue();

        for (int i = 0; i < 10; i++) {
            arrayQueue.enqueue("Test " + i);
        }

        System.out.println("ToStr: " + arrayQueue.toStr());
        System.out.println("Size: " + arrayQueue.size());
        System.out.println("IsEmpty: " + arrayQueue.isEmpty());

        System.out.println();

        while (!arrayQueue.isEmpty()) {
            System.out.println("Element: " + arrayQueue.dequeue());
            System.out.println("Size: " + arrayQueue.size());
        }

        System.out.println();

        arrayQueue.clear();

        for (int i = 0; i < 3; i++) {
            arrayQueue.clear();
            arrayQueue.enqueue("-" + i);
            System.out.println(
                    "Size: " + arrayQueue.size() + ", "
                            + "Element: " + arrayQueue.element()
            );
        }
    }
}

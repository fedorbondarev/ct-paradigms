package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue("Test " + i);
        }

        System.out.println("ToStr: " + ArrayQueueModule.toStr());
        System.out.println("Size:" + ArrayQueueModule.size());
        System.out.println("IsEmpty: " + ArrayQueueModule.isEmpty());

        System.out.println();

        while (!ArrayQueueModule.isEmpty()) {
            System.out.println("Element: " + ArrayQueueModule.dequeue());
            System.out.println("Size: " + ArrayQueueModule.size());
        }

        System.out.println();

        ArrayQueueModule.clear();

        for (int i = 0; i < 3; i++) {
            ArrayQueueModule.clear();
            ArrayQueueModule.enqueue("-" + i);
            System.out.println(
                    "Size: " + ArrayQueueModule.size() + ", "
                            + "Element: " + ArrayQueueModule.element()
            );
        }
    }
}

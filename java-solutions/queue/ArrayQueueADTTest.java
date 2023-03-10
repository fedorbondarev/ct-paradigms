package queue;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT arrayQueueADT = new ArrayQueueADT();

        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(arrayQueueADT, "Test " + i);
        }

        System.out.println("ToStr: " + ArrayQueueADT.toStr(arrayQueueADT));
        System.out.println("Size: " + ArrayQueueADT.size(arrayQueueADT));
        System.out.println("IsEmpty: " + ArrayQueueADT.isEmpty(arrayQueueADT));

        System.out.println();

        while (!ArrayQueueADT.isEmpty(arrayQueueADT)) {
            System.out.println("Element: " + ArrayQueueADT.dequeue(arrayQueueADT));
            System.out.println("Size: " + ArrayQueueADT.size(arrayQueueADT));
        }

        System.out.println();

        ArrayQueueADT.clear(arrayQueueADT);

        for (int i = 0; i < 3; i++) {
            ArrayQueueADT.clear(arrayQueueADT);
            ArrayQueueADT.enqueue(arrayQueueADT, "-" + i);
            System.out.println(
                    "Size: " + ArrayQueueADT.size(arrayQueueADT) + ", "
                            + "Element: " + ArrayQueueADT.element(arrayQueueADT)
            );
        }
    }
}

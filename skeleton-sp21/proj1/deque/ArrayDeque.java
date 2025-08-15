package deque;

import java.util.*;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int first;
    private double ratio;
    private int rear;
    private int capacity;


    public ArrayDeque() {
        items = (T[]) new Object[8]; // T 不可以直接被实例化，所以要先用 Object，然后作强制类型转换
        size = 0;
        first = 0;
        rear = 0;
        capacity = 8;
        ratio = 0;
    }

    @Override
    public void addFirst(T item) {
        // 先检验是否需要扩容
        resize();

        // 这个逻辑应该没问题，当 first 为 0 的时候，就前移循环到数组的末尾，将末尾作为 first
        first = (first - 1 + capacity) % capacity;
        items[first] = item;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        // 同样先检查是否需要扩容
        resize();
        items[rear] = item;
        rear = (rear + 1) % capacity;
        size += 1;
    }

    /* 接口已经提供默认实现
    @Override
    public boolean isEmpty(){
        return size == 0;
    }
    */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        System.out.print(items[first]);

        // 这里遍历打印的逻辑需要注意，当 first 的实际位置在 rear 之后，用 for 循环来遍历需要分两段打印，而 first 实际位置在 rear 之前时
        // 可以直接打印
        if (first > rear) {
            for (int i = first + 1; i < capacity; i += 1) {
                System.out.print(" " + items[i]);
            }
            for (int i = 0; i < rear; i += 1) {
                System.out.print(" " + items[i]);
            }
        } else {
            for (int i = first + 1; i < rear; i++) {
                System.out.print(" " + items[i]);
            }
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T temp = items[first];
        items[first] = null;
        first = (first + 1) % capacity;
        size -= 1;
        resize();
        return temp;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        rear = (rear - 1 + capacity) % capacity;
        T temp = items[rear];
        items[rear] = null;
        size -= 1;
        resize();
        return temp;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return items[(first + index) % capacity];
    }

    private void resize() {
        // 这里我已经加入了对是否需要扩容的检查逻辑，后续每次尝试 add 的时候直接调用 resize() 即可
        ratio = (double) size / capacity;
        if (ratio == 1) {
            T[] newDeque = (T[]) new Object[capacity * 2];

            //这里拷贝数组的逻辑和前面的 printDeque 的逻辑非常类似（毕竟都需要遍历），我们要根据 first 和 rear 的位置关系分两种考虑
            if (first < rear) {
                System.arraycopy(items, first, newDeque, 0, size);
            } else {
                System.arraycopy(items, first, newDeque, 0, capacity - first);
                System.arraycopy(items, 0, newDeque, capacity - first, rear);

            }
            items = newDeque;
            capacity *= 2;
            first = 0;
            rear = size;
        } else if (capacity >= 16 && ratio < 0.25) {
            T[] newDeque = (T[]) new Object[capacity / 2];
            if (first < rear) {
                System.arraycopy(items, first, newDeque, 0, size);
            } else {
                System.arraycopy(items, first, newDeque, 0, capacity - first);
                System.arraycopy(items, 0, newDeque, capacity - first, rear);
            }
            items = newDeque;
            capacity /= 2;
            first = 0;
            rear = size;
        }
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int currentIndex;
        private int remaining;

        ArrayDequeIterator() {
            currentIndex = first;
            remaining = size;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }
            T item = items[currentIndex];
            currentIndex = (currentIndex + 1) % capacity;
            remaining--;
            return item;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Deque)) {
            return false;
        }

        Deque<T> otherArrayDeque = (Deque<T>) other;
        if (this.size() != otherArrayDeque.size()) {
            return false;
        }

        for (int i = 0; i < this.size(); i++) {
            T thisItem = this.get(i);
            T thatItem = otherArrayDeque.get(i);
            if (!Objects.equals(thisItem, thatItem)) {
                return false;
            }
        }
        return true;
    }

}

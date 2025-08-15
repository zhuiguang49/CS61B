package deque;

import java.util.*;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private static class ListNode<T> {
        private T value;
        private ListNode<T> prev;
        private ListNode<T> next;

        private ListNode(T x) {
            value = x;
            prev = null;
            next = null;
        }

        public T getvalue() {
            return value;
        }
    }

    private int size;
    private ListNode<T> head;
    private ListNode<T> tail;

    // 构造函数，size 为 0，head 指针 和 tail 指针均置为 null
    public LinkedListDeque() {
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public void addFirst(T item) {
        ListNode<T> newnode = new ListNode<>(item);
        if (head == null) {
            head = newnode;
            tail = newnode;
        } else {
            newnode.next = head;
            head.prev = newnode;
            newnode.prev = null;
            head = newnode;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        ListNode<T> newnode = new ListNode<>(item);
        if (tail == null) {
            head = newnode;
            tail = newnode;
        } else {
            newnode.prev = tail;
            newnode.next = null;
            tail.next = newnode;
            tail = newnode;
        }
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
        ListNode<T> temp = head;
        if (size() != 0) {
            System.out.print(temp.getvalue());
            temp = temp.next;
            while (temp != null) {
                System.out.print(" " + temp.getvalue());
                temp = temp.next;
            }
            System.out.println();
        }
        temp = null; // 这里是清除引用，以免仍然有指向某个节点的引用，导致后续回收机制失效
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T temp = head.getvalue();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size -= 1;
        return temp;

    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T temp = tail.getvalue();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size -= 1;
        return temp;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        ListNode<T> temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.getvalue();
    }

    // 由于 getRecursive 函数只有 index 一个参数，我感觉比较难以用来实现递归，所以我选择将 getRecursive 函数作为递归的发起函数，但是实际
    // 主要的递归逻辑将在我下面的这个辅助函数中完成

    private T getRecursiveHelper(ListNode<T> currentNode, int index) {
        if (currentNode == null) {
            return null;
        }
        if (index == 0) {
            return currentNode.getvalue();
        } else {
            return getRecursiveHelper(currentNode.next, index - 1);
        }
    }


    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        } else if (index == 0) {
            return head.getvalue();
        } else {
            return getRecursiveHelper(head, index);
        }
    }

    @Override
    public Iterator<T> iterator() {
        // LinkedListDeque 的基础是节点，所以我们直接用 this 指代当前节点，返回其迭代器
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private ListNode<T> current;

        LinkedListDequeIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (this.hasNext()) {
                T item = current.getvalue();
                current = current.next;
                return item;
            } else {
                return null;
            }
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

        Deque<T> that = (Deque<T>) other;

        if (this.size != that.size()) {
            return false;
        }

        for (int i = 0; i < this.size(); i++) {
            T thisItem = this.get(i);
            T thatItem = that.get(i);
            if (!Objects.equals(thisItem, thatItem)) {
                return false;
            }
        }
        return true;

    }
}

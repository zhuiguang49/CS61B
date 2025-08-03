package deque;

public class LinkedListDeque<T> {
    public class ListNode<T>{
        private T value;
        private ListNode<T> prev;
        private ListNode<T> next;

        public ListNode(T x){
            value = x;
            prev = null;
            next = null;
        }

        public T getvalue(){
            return value;
        }
    }

    private int size;
    private ListNode<T> head;
    private ListNode<T> tail;

    // 构造函数，size 为 0，head 指针 和 tail 指针均置为 null
    public LinkedListDeque(){
        size = 0;
        head = null;
        tail = null;
    }
    public void addFirst(T item){
        ListNode<T> newnode = new ListNode<>(item);
        if(head == null){
            head = newnode;
            tail = newnode;
        }else{
            newnode.next = head;
            head.prev = newnode;
            newnode.prev = null;
            head = newnode;
        }
        size += 1;
    }

    public void addLast(T item){
        ListNode<T> newnode = new ListNode<>(item);
        if(tail == null){
            head = newnode;
            tail = newnode;
        }else{
            newnode.prev = tail;
            newnode.next = null;
            tail.next = newnode;
            tail = newnode;
        }
        size += 1;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        ListNode<T> temp = head;
        if(size() != 0){
            System.out.print(temp.getvalue());
            temp = temp.next;
            while(temp != null){
                System.out.print(" " + temp.getvalue());
                temp = temp.next;
            }
            System.out.println();
        }
        temp = null; // 这里是清除引用，以免仍然有指向某个节点的引用，导致后续回收机制失效
    }

    public T removeFirst(){
        if(size == 0){
            return null;
        }
        T temp = head.getvalue();
        if(size == 1){
            head = null;
            tail = null;
        }else {
            head = head.next;
            head.prev = null;
        }
        size -= 1;
        return temp;

    }

    public T removeLast(){
        if(size == 0){
            return null;
        }

        T temp = tail.getvalue();
        if(size == 1){
            head = null;
            tail = null;
        }else {
            tail = tail.prev;
            tail.next = null;
        }
        size -= 1;
        return temp;
    }

    public T get(int index){
        if(index >= size || index < 0){
            return null;
        }
        ListNode<T> temp = head;
        for(int i = 0; i < index; i++){
            temp = temp.next;
        }
        return temp.getvalue();
    }

    // 由于 getRecursive 函数只有 index 一个参数，我感觉比较难以用来实现递归，所以我选择将 getRecursive 函数作为递归的发起函数，但是实际
    // 主要的递归逻辑将在我下面的这个辅助函数中完成

    private T getRecursiveHelper(ListNode<T> currentNode, int index){
        if(currentNode == null){
            return null;
        }
        if(index == 0){
            return currentNode.getvalue();
        }else{
            return getRecursiveHelper(currentNode.next,index-1);
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

    // 这个返回迭代器的方法不太清楚咋写
    /*
    public Iterator<T> iterator(){

    }
    */

    // 这个暂时也还不太懂
    /*
    public boolean eaquals(Object o){

    }*/
}

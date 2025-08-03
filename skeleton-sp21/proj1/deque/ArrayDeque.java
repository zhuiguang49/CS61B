package deque;

public class ArrayDeque<T> {
    private T[] Item;
    private int size;
    private double ratio;
    private int first;
    private int rear;
    private int capacity;


    public ArrayDeque(){
        Item = (T[]) new Object[8]; // T 不可以直接被实例化，所以要先用 Object，然后作强制类型转换
        size = 0;
        first = 0;
        rear = 0;
        capacity = 8;
        ratio = 0;
    }

    public void addFirst(T item){
        // 先检验是否需要扩容
        resize();

        // 这个逻辑应该没问题，当 first 为 0 的时候，就前移循环到数组的末尾，将末尾作为 first
        first = (first - 1 + capacity) % capacity;
        Item[first] = item;
        size += 1;
    }

    public void addLast(T item){
        // 同样先检查是否需要扩容
        resize();

        Item[rear] = item;
        rear = (rear + 1) % capacity;
        size += 1;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        System.out.print(Item[first]);

        // 这里遍历打印的逻辑需要注意，当 first 的实际位置在 rear 之后，用 for 循环来遍历需要分两段打印，而 first 实际位置在 rear 之前时
        // 可以直接打印
        if(first > rear){
            for(int i = first + 1; i < capacity; i += 1){
                System.out.print(" " + Item[i]);
            }
            for(int i = 0; i < rear; i += 1){
                System.out.print(" " + Item[i]);
            }
        }else{
            for(int i = first + 1; i < rear; i++){
                System.out.print(" " + Item[i]);
            }
        }
        System.out.println();
    }

    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        T temp = Item[first];
        first = (first + 1) % capacity;
        size -= 1;
        checkSize();
        return temp;
    }

    public T removeLast(){
        if(isEmpty()){
            return null;
        }
        rear = (rear - 1 + capacity) % capacity;
        T temp = Item[rear];
        size -= 1;
        checkSize();
        return temp;
    }

    public T get(int index){
        if(index >= size || index < 0){
            return null;
        }
        return Item[(first+index)%capacity];
    }

    public void checkSize(){
        ratio = (double) size / capacity;
        if(size >= 16 && ratio < 0.25){
            resize();
        }
    }

    public void resize(){
        // 这里我已经加入了对是否需要扩容的检查逻辑，后续每次尝试 add 的时候直接调用 resize() 即可
        ratio = (double) size / capacity;
        if(ratio==1){
            T[] newDeque = (T[]) new Object[capacity * 2];

            //这里拷贝数组的逻辑和前面的 printDeque 的逻辑非常类似（毕竟都需要遍历），我们要根据 first 和 rear 的位置关系分两种考虑
            if(first >rear){
                System.arraycopy(Item,first,newDeque,0,capacity-first+1);
                System.arraycopy(Item,0,newDeque,capacity-first+1,rear+1);
            }else{
                System.arraycopy(Item,first,newDeque,0,size);
            }
            Item = newDeque;
            capacity *= 2;
            first = 0;
            rear = size;
        }else if(size >= 16 &&  ratio < 0.25){
            T[] newDeque = (T[]) new Object[capacity / 2];
            if(first >rear){
                System.arraycopy(Item,first,newDeque,0,capacity-first+1);
                System.arraycopy(Item,0,newDeque,capacity-first+1,rear+1);
            }else{
                System.arraycopy(Item,first,newDeque,0,size);
            }
            Item = newDeque;
            capacity /= 2;
            first = 0;
            rear = size;
        }
    }

}

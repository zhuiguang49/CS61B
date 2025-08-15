package deque;

import java.util.*;


public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.comparator = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }

        T maxElement = this.get(0);
        for (T x : this) {
            if (this.comparator.compare(x, maxElement) > 0) {
                maxElement = x;
            }
        }
        return maxElement;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T maxElement = this.get(0);
        for (T x : this) {
            if (c.compare(x, maxElement) > 0) {
                maxElement = x;
            }
        }
        return maxElement;
    }


}

package com.beelive.arrays.datastrcuture;
/**
 * MyArrays.java
 *
 * A generic, resizable array data structure (similar in spirit to
 * java.util.ArrayList) implemented from scratch on top of a plain
 * Object[] backing array.
 *
 * Supported operations:
 *   - add(element)                 -> append to end
 *   - add(index, element)          -> insert at index
 *   - get(index)                   -> read element
 *   - set(index, element)          -> overwrite element
 *   - remove(index)                -> remove and return element
 *   - remove(element)              -> remove first occurrence
 *   - indexOf(element)             -> search
 *   - contains(element)            -> search
 *   - size(), isEmpty(), clear()
 *   - toString()
 *
 * Growth strategy: capacity doubles when full (amortized O(1) add).
 * Shrink strategy: capacity halves when usage drops below 25% (optional,
 * included here to avoid wasted memory after many removals).
 */
public class MyArrays<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public MyArrays() {
        this(DEFAULT_CAPACITY);
    }

    public MyArrays(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative: " + initialCapacity);
        }
        data = new Object[initialCapacity == 0 ? DEFAULT_CAPACITY : initialCapacity];
        size = 0;
    }

    /** Number of elements currently stored. */
    public int size() {
        return size;
    }

    /** True if no elements are stored. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Appends an element to the end of the array. */
    public void add(T element) {
        ensureCapacity(size + 1);
        data[size++] = element;
    }

    /** Inserts an element at the given index, shifting subsequent elements right. */
    public void add(int index, T element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    /** Returns the element at the given index. */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    /** Replaces the element at the given index, returning the old value. */
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T old = (T) data[index];
        data[index] = element;
        return old;
    }

    /** Removes and returns the element at the given index. */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null; // let GC reclaim it
        shrinkIfNeeded();
        return removed;
    }

    /** Removes the first occurrence of the given element. Returns true if found & removed. */
    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx == -1) {
            return false;
        }
        remove(idx);
        return true;
    }

    /** Returns the index of the first occurrence of element, or -1 if not found. */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? data[i] == null : element.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    /** True if the array contains the given element. */
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    /** Removes all elements. */
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    /** Current backing array capacity (for inspection/testing). */
    public int capacity() {
        return data.length;
    }

    // ---------- internal helpers ----------

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = Math.max(data.length * 2, minCapacity);
            data = java.util.Arrays.copyOf(data, newCapacity);
        }
    }

    private void shrinkIfNeeded() {
        if (data.length > DEFAULT_CAPACITY && size <= data.length / 4) {
            int newCapacity = Math.max(DEFAULT_CAPACITY, data.length / 2);
            data = java.util.Arrays.copyOf(data, newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    // ---------- demo ----------

    public static void main(String[] args) {
        MyArrays<Integer> arr = new MyArrays<>();
        arr.add(10);
        arr.add(20);
        arr.add(30);
        System.out.println("After adds: " + arr + " size=" + arr.size() + " capacity=" + arr.capacity());

        arr.add(1, 15);
        System.out.println("After insert at 1: " + arr);

        arr.set(0, 99);
        System.out.println("After set(0, 99): " + arr);

        System.out.println("get(2) = " + arr.get(2));
        System.out.println("indexOf(30) = " + arr.indexOf(30));
        System.out.println("contains(15) = " + arr.contains(15));

        arr.remove(0);
        System.out.println("After remove(0): " + arr);

        arr.remove(Integer.valueOf(30));
        System.out.println("After remove(Object 30): " + arr);

        System.out.println("isEmpty() = " + arr.isEmpty());
        arr.clear();
        System.out.println("After clear(): " + arr + " isEmpty=" + arr.isEmpty());
    }
}
package com.beelive.arrays.datastrcuture;

public class MyArrays<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data ;
    private int size;

    public MyArrays(){
        size =DEFAULT_CAPACITY;
    }

    public MyArrays(int initialCapacity) throws IllegalAccessException {
        if(initialCapacity < 0 ){
            throw new IllegalAccessException(" Initial capacity cannot be negative : "+ initialCapacity);
        }

        data = new Object[initialCapacity==0 ? DEFAULT_CAPACITY :initialCapacity];
        size =0;
    }



}

package com.octalsoftaware.archi.utils;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by anandj on 4/18/2017.
 */

public class CustomArrayList<T> extends ArrayList<T>{

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    public int indexOf(@Nullable Object o) {
        int size = this.size();
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (this.get(i)==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(this.get(i).toString()))
                    return i;
        }
        return -1;
    }
}

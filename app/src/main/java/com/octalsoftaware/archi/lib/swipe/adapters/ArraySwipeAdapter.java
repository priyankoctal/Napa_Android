package com.octalsoftaware.archi.lib.swipe.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.octalsoftaware.archi.lib.swipe.SwipeLayout;
import com.octalsoftaware.archi.lib.swipe.implments.SwipeItemMangerImpl;
import com.octalsoftaware.archi.lib.swipe.interfaces.SwipeAdapterInterface;
import com.octalsoftaware.archi.lib.swipe.interfaces.SwipeItemMangerInterface;
import com.octalsoftaware.archi.lib.swipe.util.Attributes;

import java.util.List;

public abstract class ArraySwipeAdapter<T> extends ArrayAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    @NonNull
    private SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);
    {}
    public ArraySwipeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ArraySwipeAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ArraySwipeAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
    }

    public ArraySwipeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ArraySwipeAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    public ArraySwipeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        mItemManger.bind(v, position);
        return v;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}

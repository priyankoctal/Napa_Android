package com.octalsoftaware.archi.lib.swipe.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.octalsoftaware.archi.lib.swipe.SwipeLayout;
import com.octalsoftaware.archi.lib.swipe.implments.SwipeItemMangerImpl;
import com.octalsoftaware.archi.lib.swipe.interfaces.SwipeAdapterInterface;
import com.octalsoftaware.archi.lib.swipe.interfaces.SwipeItemMangerInterface;
import com.octalsoftaware.archi.lib.swipe.util.Attributes;

import java.util.List;

public abstract class SimpleCursorSwipeAdapter extends SimpleCursorAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    @NonNull
    private SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    protected SimpleCursorSwipeAdapter(@NonNull Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    protected SimpleCursorSwipeAdapter(@NonNull Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

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

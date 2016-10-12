package io.github.plastix.prolificlibrary.data.model;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class BookDiffCallback extends DiffUtil.Callback {

    private final List<Book> oldList;
    private final List<Book> newList;

    public BookDiffCallback(List<Book> newList, List<Book> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).title.equals(newList.get(newItemPosition).title);
    }
}

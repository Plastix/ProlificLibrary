package io.github.plastix.prolificlibrary.ui.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.databinding.ItemBookBinding;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BindingHolder> {

    private List<Book> books;

    @Inject
    public BookAdapter() {
        this.books = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemBookBinding bookBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_book,
                parent,
                false
        );

        return new BindingHolder(bookBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemBookBinding binding = holder.binding;
        Book book = books.get(position);

        BookViewModel viewModel = holder.binding.getViewModel();
        if (viewModel != null) {
            viewModel.unbind();
        }

        viewModel = new BookViewModel(book);

        binding.setViewModel(viewModel);
        viewModel.bind(binding.getRoot().getContext());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        // TODO Switch to DiffUtil
        notifyDataSetChanged();
    }

    static class BindingHolder extends RecyclerView.ViewHolder {

        private ItemBookBinding binding;

        BindingHolder(ItemBookBinding binding) {
            super(binding.parent);
            this.binding = binding;
        }
    }
}

package io.github.plastix.prolificlibrary.ui.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.databinding.ActivityListBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ListActivity extends ViewModelActivity<ListViewModel, ActivityListBinding> implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    BookAdapter bookAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;

    private CompositeSubscription modelSubscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);

        setupUI();
    }

    private void setupUI() {
        binding.setViewModel(viewModel);
        binding.swipeRefresh.setOnRefreshListener(this);

        binding.recycler.setLayoutManager(linearLayoutManager);
        binding.recycler.setAdapter(bookAdapter);
    }

    @Override
    protected ActivityListBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_list);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new ListModule(this)).injectTo(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO Move this...
        Subscription booksSub = viewModel.getBooks().subscribe(books -> {
            binding.swipeRefresh.setRefreshing(false);
            bookAdapter.setBooks(books);
        });

        modelSubscriptions.add(booksSub);
    }

    @Override
    protected void onUnbind() {
        super.onUnbind();
        modelSubscriptions.clear();
    }

    @Override
    public void onRefresh() {
        viewModel.fetchBooks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookAdapter.onDestroy();
        bookAdapter = null;
        linearLayoutManager = null;
    }
}

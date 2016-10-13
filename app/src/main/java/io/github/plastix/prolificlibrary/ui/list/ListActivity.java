package io.github.plastix.prolificlibrary.ui.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.databinding.ActivityListBinding;
import io.github.plastix.prolificlibrary.ui.add.AddActivity;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import rx.subscriptions.CompositeSubscription;

public class ListActivity extends ViewModelActivity<ListViewModel, ActivityListBinding> implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "ListActivity";

    @Inject
    BookAdapter bookAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
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
    protected void onBind() {
        super.onBind();
        setupUI();
    }

    private void setupUI() {
        binding.setViewModel(viewModel);
        binding.swipeRefresh.setOnRefreshListener(this);

        binding.recycler.setLayoutManager(linearLayoutManager);
        binding.recycler.setAdapter(bookAdapter);

        subscriptions.add(viewModel.getBooks()
                .subscribe(this::updateBooks));

        subscriptions.add(viewModel.fabClicks()
                .subscribe(this::fabClicked));

        subscriptions.add(viewModel.networkErrors()
                .subscribe(this::showErrorSnackbar));
    }

    public void updateBooks(List<Book> books) {
        stopRefresh();
        bookAdapter.setBooks(books);
    }

    private void stopRefresh() {
        binding.swipeRefresh.setRefreshing(false);
    }

    public void fabClicked(Void ignore) {
        startActivity(AddActivity.newIntent(this));
    }

    public void showErrorSnackbar(Throwable throwable) {
        stopRefresh();
        Snackbar.make(binding.coordinator, R.string.list_fetch_error, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onUnbind() {
        super.onUnbind();
        subscriptions.clear();
    }

    // SwipeRefreshLayout callback
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
        bookAdapter = null;
        linearLayoutManager = null;
    }
}

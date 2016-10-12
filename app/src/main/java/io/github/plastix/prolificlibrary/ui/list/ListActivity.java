package io.github.plastix.prolificlibrary.ui.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.databinding.ActivityListBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;

public class ListActivity extends ViewModelActivity<ListViewModel, ActivityListBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);

        binding.setViewModel(viewModel);
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
}

package com.example.fulltaskc4i;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fulltaskc4i.Adapters.PersonRecyclerAdapter;
import com.example.fulltaskc4i.Adapters.SliderPagerAdapter;
import com.example.fulltaskc4i.ModelsPackage.PersonModel;
import com.example.fulltaskc4i.ModelsPackage.SaveModel;
import com.example.fulltaskc4i.SQLiteDBPackage.PersonDBManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    ArrayList<PersonModel> personModelList;
    private SaveModel saveModel;
    private ViewPager viewPager;
    private ArrayList<PersonModel> sliderList;
    private PersonRecyclerAdapter personRecyclerAdapter;
    private ProgressBar footerProgressBar;
    private PersonDBManager personDBManager;
    private boolean shouldLoadMore = true;

    private RecyclerView.OnScrollListener displayPage = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (dy > 0) {
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == personModelList.size() - 1) {
                    if (shouldLoadMore) {
                        footerProgressBar.setVisibility(View.VISIBLE);
                        loadAnotherPage();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveModel = new SaveModel(this);
        checkTheme();
        setContentView(R.layout.activity_main);
        setToolbar();
        personDBManager = new PersonDBManager(this).open();
        footerProgressBar = findViewById(R.id.footerProgressBar);
        setImageSlider();
        setRecyclerView();
        doTransition();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkTheme();
    }

    private void checkTheme() {
        int resId = saveModel.getThemeId();
        if (resId == 0) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(resId);
        }
    }


    void doTransition() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        getWindow().setEnterTransition(transition);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setImageSlider() {
        sliderList = new ArrayList<>();
        TabLayout tabLayout = findViewById(R.id.sliderTab);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        ArrayList<PersonModel> lastTenPersonModels = personDBManager.getLastTenRecords();
        if (lastTenPersonModels != null) {
            sliderList.addAll(lastTenPersonModels);
            SliderPagerAdapter sliderPagerAdapter = new SliderPagerAdapter(sliderList, this);
            viewPager.setAdapter(sliderPagerAdapter);
            viewPager.setPageTransformer(true, new ZoomTransaction());
            setAutoSlide();
        }

    }

    private void setAutoSlide() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < sliderList.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 3000, 3000);
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView personRecyclerView = findViewById(R.id.recycleView);
        personRecyclerView.setLayoutManager(layoutManager);
        personModelList = new ArrayList<>();
        personRecyclerAdapter = new PersonRecyclerAdapter(this, personModelList, getSupportFragmentManager());
        personRecyclerView.setAdapter(personRecyclerAdapter);
        personRecyclerView.addOnScrollListener(displayPage);
        loadFirstPage();
    }

    private void loadFirstPage() {
        ArrayList<PersonModel> personModelsPage = personDBManager.getPage(0);
        if (personModelsPage != null) {
            personModelList.addAll(personModelsPage);
            personRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void loadAnotherPage() {
        if (personModelList.size() == personDBManager.getTableRowCount()) {
            shouldLoadMore = false;
        } else {
            int lastIdLoaded = personModelList.get(personModelList.size() - 1).getId();
            personModelList.addAll(personDBManager.getPage(lastIdLoaded));
            personRecyclerAdapter.notifyDataSetChanged();
        }
        footerProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        handleSearchIcon(menu);
        return true;
    }

    private void handleSearchIcon(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                personRecyclerAdapter.searchPerson(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SaveModel saveModel = new SaveModel(this);
        switch (item.getItemId()) {
            case R.id.addPerson:
                displayFragment();
                break;
            case R.id.signOut:
                saveModel.saveSignedInStatus(false);
                displayActivity(FirstActivity.class);
                break;
            case R.id.changeTheme:
                displayActivity(ThemeSettingActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void displayFragment() {
        InsertOrUpdatePersonFragment fragment = new InsertOrUpdatePersonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("whatToDo", "Add Person");
        bundle.putSerializable("personList", personModelList);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.show(fragmentManager, null);
    }

}

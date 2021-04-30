package com.example.mesfrigos4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MesFrigos4);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras !=null)
        {
            if(getIntent().getStringExtra("fragment").equals("frigidaire")) {
                Bundle bundle = new Bundle();
                bundle.putString("barcode_from_activity", getIntent().getStringExtra("code"));
                Fragment fragment = new FrigidaireFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }else if (getIntent().getStringExtra("fragment").equals("congélateur")){
                Bundle bundle = new Bundle();
                bundle.putString("barcode_from_activity", getIntent().getStringExtra("code"));
                Fragment fragment = new CongelateurFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        }

        toolbar.setTitle("Frigidaire");
        loadFragment(new FrigidaireFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_frigidaire:
                    toolbar.setTitle("Frigidaire");
                    fragment = new FrigidaireFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_congelateur:
                    toolbar.setTitle("Congelateur");
                    fragment = new CongelateurFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
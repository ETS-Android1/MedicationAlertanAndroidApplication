package app.cave.medicinalertapp.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.cave.medicinalertapp.R;
import app.cave.medicinalertapp.fragment.Add_Medichine_fragment;
import app.cave.medicinalertapp.fragment.Medichine_View_fragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_Container, new Medichine_View_fragment()).commit();
        setTitle("Add New Medicine");
    }
}

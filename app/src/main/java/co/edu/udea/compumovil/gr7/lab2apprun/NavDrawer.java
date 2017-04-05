package co.edu.udea.compumovil.gr7.lab2apprun;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Set;

import co.edu.udea.compumovil.gr7.lab2apprun.data.DbHelper;
import co.edu.udea.compumovil.gr7.lab2apprun.data.StatusContract;
import co.edu.udea.compumovil.gr7.lab2apprun.events.AddEvent;
import co.edu.udea.compumovil.gr7.lab2apprun.events.EventList;
import co.edu.udea.compumovil.gr7.lab2apprun.user.LoginActivity;
import co.edu.udea.compumovil.gr7.lab2apprun.user.ProfileInfoFragment;

public class NavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DbHelper dbH;
    SQLiteDatabase db;
    Fragment about = new AboutFragment();
    Fragment events = new EventList();
    AddEvent add = new AddEvent();
    Fragment info = new ProfileInfoFragment();
    FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
    FloatingActionButton fab;
    private boolean controlSelect=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        dbH=new DbHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        manager.replace(R.id.fragment_container, about);
        manager.commit();
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        manager = getSupportFragmentManager().beginTransaction();
        events = new EventList();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!controlSelect){moveTaskToBack(true);}
            else{
                manager.replace(R.id.fragment_container, events);
                manager.commit();
                controlSelect=false;
                fab.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logOut) {
            db = dbH.getWritableDatabase();
            db.execSQL("delete from " + StatusContract.TABLE_LOGIN);
            db.close();
            Intent newActivity = new Intent(this, LoginActivity.class);
            startActivity(newActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        manager = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.profile) {
            info = new ProfileInfoFragment();
            manager.replace(R.id.fragment_container,info);
            fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.events) {
            events = new EventList();
            manager.replace(R.id.fragment_container, events);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.about) {
            about = new AboutFragment();
            manager.replace(R.id.fragment_container, about);
            fab.setVisibility(View.INVISIBLE);
        }
        manager.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void AddEvent(View v){
        manager = getSupportFragmentManager().beginTransaction();
        add = new AddEvent();
        manager.replace(R.id.fragment_container, add);
        manager.commit();
        fab.setVisibility(View.INVISIBLE);
        controlSelect=true;
    }
    public void DClic(View v){
        add.DateClic();
    }
    public void GClic(View v){
        add.ClickGalleryR();
    }
    public void CClic(View v){
        add.ClickCameraR();
    }
    public void onClickEvent(View v){
        add.onClickEvent();
        manager = getSupportFragmentManager().beginTransaction();
        events = new EventList();
        manager.replace(R.id.fragment_container, events);
        manager.commit();
        fab.setVisibility(View.VISIBLE);
    }
}

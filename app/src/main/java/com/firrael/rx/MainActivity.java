package com.firrael.rx;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null)
            requestItems("test");
    }

    public void onItems(List<JsonObject> objects) {//List<Item> items) {
        /*adapter.clear();
        adapter.addAll(items);*/
        this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "TEST success", Toast.LENGTH_LONG).show());
    }

    public void onItemsError(Throwable throwable) {
        this.runOnUiThread(() -> Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show());
    }

    // You can use this method to (re)start the restartable with a new parameter
    private void requestItems(String query) {
        getPresenter().request(query);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            rxInit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rxInit() {
        /*Observable.just("Hello, world!")
                .map(String::hashCode)
                .map(i -> Integer.toString(i))
                .subscribe(System.out::println);


        query("Hello, world!")
                .flatMap(Observable::from)
                .subscribe(System.out::println);

        query("Hello, world!")
                .flatMap(Observable::from)
                .flatMap(this::getTitle)
                .filter(title -> title != null)
                .take(5)
                .doOnNext(this::saveTitle)
                .subscribe(System.out::println);

        query("Hello, world!")
                .flatMap(Observable::from)
                .flatMap(this::getTitle)
                .filter(title -> title != null)
                .take(5)
                .doOnNext(this::saveTitle)
                .subscribe(System.out::println);

        query("Hello, world!")
                .flatMap(Observable::from)
                .flatMap(this::getTitle)
                .filter(title -> title != null)
                .take(5)
                .doOnNext(this::saveTitle)
                .subscribe(System.out::println);*/

      /*  query("Hello, world!")
                .flatMap(Observable::from)
                .flatMap(this::getTitle)
                .flatMap(this::getTitle)
                .filter(title -> title != null)
                .take(5)
                .doOnNext(this::saveTitle)
                .subscribe(System.out::println);
*/
        Observable.just("Hello, world!")
                .flatMap(this::potentialException)
                .flatMap(this::anotherPotentialException)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Ouch!");
                    }
                });

        TextView label = (TextView) findViewById(R.id.label);
        assert label != null;

        Observable.just("Test")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(label::setText);

        Subscription subscription = Observable.just("Hello, World!")
                .subscribe(System.out::println);

        subscription.unsubscribe();
    }

    private Observable<String> anotherPotentialException(String s) {
        return null;
    }

    private Observable<String> potentialException(String s) {
        return null;
    }

    private void saveTitle(String title) {
    }

    Observable<List<String>> query(String text) {
        return null;
    }

    Observable<String> getTitle(String url) {
        return null;
    }

}

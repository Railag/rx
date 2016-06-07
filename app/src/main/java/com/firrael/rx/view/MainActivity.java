package com.firrael.rx.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firrael.rx.App;
import com.firrael.rx.R;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.ImageResult;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.MainPresenter;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

import static butterknife.ButterKnife.findById;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_MAIN = "mainTag";
    private static final int PHOTO_CODE = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

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

        View headerView = navigationView.getHeaderView(0);
        ImageView userImage = findById(headerView, R.id.userImage);
        userImage.setOnClickListener(v -> {
            makePhoto();
        });

        App.setMainActivity(this);

        toSplash();
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

        if (id == R.id.nav_profile) {
            toUserLandingScreen();
        } else if (id == R.id.nav_groups) {
            toMyGroups();
        } else if (id == R.id.nav_new_group) {
            toNewGroup();
        } else if (id == R.id.nav_share) {
            // TODO share app
        } else if (id == R.id.nav_logout) {
            User.logout(this);
            toLogin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void makePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PHOTO_CODE);
        } else
            Toast.makeText(this, getString(R.string.image_capture_no_camera), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView userImage = findById(this, R.id.userImage);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), imageBitmap);
            circularBitmapDrawable.setCircular(true);
            userImage.setImageDrawable(circularBitmapDrawable);

            getPresenter().request(imageBitmap);
        }
    }

    public void startLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        loading.setVisibility(View.GONE);
    }

    public <T extends Fragment> void setFragment(final T fragment) {
        runOnUiThread(() -> {
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // TODO custom transaction animations
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.replace(R.id.mainFragment, fragment, TAG_MAIN);
            fragmentTransaction.commitAllowingStateLoss();
        });
    }

    public void toSplash() {
        setFragment(SplashFragment.newInstance());
    }

    public void toLogin() {
        setFragment(LoginFragment.newInstance());
    }

    public void toUserLandingScreen() {
        setFragment(UserLandingFragment.newInstance());
    }

    public void toCreateAccount() {
        setFragment(CreateAccountFragment.newInstance());
    }

    public void toMyGroups() {
        setFragment(MyGroupsFragment.newInstance());
    }

    public void toNewGroup() {
        setFragment(NewGroupFragment.newInstance());
    }

    public void toGroupMemberScreen(Group group) {
        setFragment(GroupMemberFragment.newInstance(group));
    }

    public void toGroupCreatorScreen(Group group) {
        setFragment(GroupCreatorFragment.newInstance(group));
    }

    ;

    public void updateNavigationMenu() {
        View headerView = navigationView.getHeaderView(0);

        ImageView userImage = findById(headerView, R.id.userImage);
        TextView userLogin = findById(headerView, R.id.userLogin);
        TextView userEmail = findById(headerView, R.id.userEmail);

        User user = User.get(this);
        Glide.with(this).load(user.getProfileImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(userImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                userImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        userLogin.setText(user.getLogin());
        userEmail.setText(user.getEmail());
    }

    public void onSuccess(ImageResult result) {
        User.get(this).setProfileImageUrl(result.getUrl());
        updateNavigationMenu();
    }

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
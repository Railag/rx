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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firrael.rx.App;
import com.firrael.rx.FcmMessagingService;
import com.firrael.rx.R;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.model.AddUserResult;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.ImageResult;
import com.firrael.rx.model.SendFCMTokenResult;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.MainPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.findById;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private static final String TAG_MAIN = "mainTag";
    private static final int PHOTO_CODE = 1;

    private final static int PN_GROUP_INVITE = 1;
    private final static int PN_GROUP_CALL = 2;

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

        // TODO check for google play services
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().showErrorDialogFragment(this, code, 1);
        }

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

        if (!User.isFcmSaved(this)) {
            User user = User.get(this);
            String fcmToken = user.getFcmToken(this);
            if (!TextUtils.isEmpty(fcmToken)) {
                RConnectorService service = App.restService();

                service.sendFCMToken(user.getId(), fcmToken)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onFcmTokenSuccess, this::onFcmTokenError);
            }
        }


        if (getIntent() != null) {
            handlePN(getIntent());
        }
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

            getPresenter().saveImage(imageBitmap);
        }
    }

    public void startLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        loading.setVisibility(View.GONE);
    }

    private <T extends Fragment> void setFragment(final T fragment) {
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
        // TODO add user fragment with info if needed
        // setFragment(UserLandingFragment.newInstance());
        toMyGroups();
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

    public void toWebrtcScreen(String host) {
        Intent intent = new Intent(this, WebrtcActivity.class);
        intent.putExtra(WebrtcActivity.HOST, host);
        startActivity(intent);
    }

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

    public void onSuccessSaveImage(ImageResult result) {
        User.get(this).setProfileImageUrl(result.getUrl());
        updateNavigationMenu();
    }

    public void onErrorSaveImage(Throwable throwable) {
        throwable.printStackTrace();
    }


    public void onSuccessAddUser(AddUserResult result) {
        if (result.invalid()) {
            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show();
            return;
        }

        toMyGroups();
    }

    public void onErrorAddUser(Throwable throwable) {
        throwable.printStackTrace();
    }


    public void onFcmTokenSuccess(SendFCMTokenResult result) {
        if (result.invalid()) {
            Log.e(TAG, result.error);
        }

        Log.i(TAG, result.result);

        User.fcmSaved(getBaseContext());
    }

    public void onFcmTokenError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePN(intent);
    }

    public void handlePN(Intent intent) {
        Bundle args = intent.getExtras();
        if (args != null) {
            if (args.containsKey(FcmMessagingService.PN_CODE_KEY)) {
                int pnCode = args.getInt(FcmMessagingService.PN_CODE_KEY);
                String data = args.getString(FcmMessagingService.PN_DATA_KEY, "");
                switch (pnCode) {
                    case PN_GROUP_INVITE:
                        User user = User.get(this);
                        long userId = user.getId();
                        try {
                            long groupId = Long.valueOf(data);
                            getPresenter().addToGroup(userId, groupId);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PN_GROUP_CALL:
                        toWebrtcScreen(data);
                        break;
                }
            }
        } else { // no custom PN
            toSplash();
        }
    }


}
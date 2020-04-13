package gq.altafchaudhari.cowatch.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import de.hdodenhof.circleimageview.CircleImageView;
import gq.altafchaudhari.cowatch.BuildConfig;
import gq.altafchaudhari.cowatch.CountryWiseFragment;
import gq.altafchaudhari.cowatch.GraphicsFragment;
import gq.altafchaudhari.cowatch.IndiaFragment;
import gq.altafchaudhari.cowatch.R;
import gq.altafchaudhari.cowatch.SharedPreferenceManager;
import gq.altafchaudhari.cowatch.utilities.ExceptionHandler;
import gq.altafchaudhari.cowatch.utilities.GlobalData;
import gq.altafchaudhari.cowatch.utilities.Utils;
import me.ibrahimsn.particle.ParticleView;

public class MainActivity extends AppCompatActivity implements InstallStateUpdatedListener {


    private Fragment activeFragment;
    private Fragment overviewFragment,indiaFragment,districtFragment,graphicsFragment;

    FragmentManager fragmentManager;
    FloatingActionButton fab;

    private ParticleView particleView;


    private static String TAG = "MainActivity";
    private static String OVERVIEW = "overview";
    private static String INDIA = "india";
    private static String DISTRICT = "district";
    private static int REQUEST_CODE_FLEXI_UPDATE = 17362;

    SharedPreferenceManager spManager;
    DrawerLayout drawer;
   /* @BindView(R.id.bottomNavBar)*/
    BottomNavigationView bottomNavBar;

    private AppUpdateManager appUpdateManager;

    private MutableLiveData<Boolean> updateAvailable = new MutableLiveData<Boolean>();
    private AppUpdateInfo updateInfo= null;

    TextView appVersionTextView,tv_email_id,tv_app_version;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        updateAvailable.setValue(false);

        startExceptionHandler();
        bottomNavBar = findViewById(R.id.bottomNavBar);
        drawer = findViewById(R.id.drawer_layout);
        bottomNavBar.setOnNavigationItemSelectedListener(navListener);
        particleView = findViewById(R.id.particleView);
        tv_email_id = findViewById(R.id.tv_email_id);
        tv_app_version = findViewById(R.id.tv_app_version);

        tv_app_version.setText(getString(R.string.app_name)+" v"+ BuildConfig.VERSION_NAME);
        tv_email_id.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"report@coronacounter.unaux.com","altafc22@live.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Report s");
            intent.putExtra(Intent.EXTRA_TEXT,"Please enter your suggestion, issues here..");
            //intent.putExtra(Intent.EXTRA_BCC,"altafc22@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        });

        //with test app id
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.original_full_banner));
        /*        adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();*/
        adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        appUpdateManager = AppUpdateManagerFactory.create(this);

        //appVersionTextView = new TextView(this);
        //appVersionTextView.setText(BuildConfig.VERSION_CODE);

        if(!Utils.isInternetAvailable(this))
        {
            Toast.makeText(this,"Please connect internet to get updated stats.",Toast.LENGTH_SHORT).show();
        }

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {   //  check for the type of update flow you want
                startForInAppUpdate(appUpdateInfo);
            }
        });

        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(new OnCompleteListener<AppUpdateInfo>() {
            @Override
            public void onComplete(Task<AppUpdateInfo> task) {
                //Toast.makeText(MainActivity.this,"Update Available",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"UPDATE AVAILABLE");
            }
        });

        appUpdateManager.getAppUpdateInfo().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG,"GET Info failed ${it.message}");
            }
        });


        //checkForUpdate();
        checkForUpdate();



        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,
                    new CountryWiseFragment()).commit();
            activeFragment = new CountryWiseFragment();
        }
        init();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    public void startExceptionHandler()
    {
        if (GlobalData.isdebug) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, MainActivity.class));
        }
        final Intent intent = getIntent();

        if (intent.hasExtra("error")) {
            if (GlobalData.isdebug) {
                try {
                    final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage((CharSequence) intent.getExtras().getString("error"));
                    builder1.setCancelable(true);
                    builder1.setPositiveButton((CharSequence) "Copy & Share", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clipboard.setPrimaryClip(ClipData.newPlainText("Error", intent.getExtras().getString("error")));
                            Toast.makeText(MainActivity.this, "Copy to clipboard", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    builder1.create().show();
                }
                catch (Exception e)
                {

                }
                return;
            }
        }
    }

    private void checkForUpdate(){
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {   //  check for the type of update flow you want
                Toast.makeText(MainActivity.this,"Update Available",Toast.LENGTH_SHORT).show();
                updateAvailable.setValue(true);
                startForInAppUpdate(updateInfo);
            }
            else
            {
                Toast.makeText(MainActivity.this,"Application is up to date.",Toast.LENGTH_SHORT).show();
                updateAvailable.setValue(false);
            }
        });
    }

    private void startForInAppUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.FLEXIBLE,this,REQUEST_CODE_FLEXI_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FLEXI_UPDATE) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void init(){
        fragmentManager = getSupportFragmentManager();
        overviewFragment = new CountryWiseFragment();
        indiaFragment = new IndiaFragment();
        //districtFragment = new DistrictFragment();
        graphicsFragment = new GraphicsFragment();

        activeFragment = overviewFragment;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.homeContainer, activeFragment,"Overview");
        ft.commit();



    }

    public void openNavBar(View v)
    {
        drawer.openDrawer(GravityCompat.START);
        CircleImageView profile_image;
        profile_image =  findViewById(R.id.profile_image);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_world:
                            if(activeFragment!=overviewFragment)
                                activeFragment = overviewFragment;
                            break;
                        case R.id.action_india:
                            if(activeFragment!=indiaFragment)
                                activeFragment = indiaFragment;
                            break;
                       /* case R.id.action_district:
                            if(activeFragment!=districtFragment)
                                activeFragment = districtFragment;
                            break;*/
                        case R.id.action_graphics:
                            if(activeFragment!=graphicsFragment)
                            {
                                activeFragment = graphicsFragment;
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        mInterstitialAd.loadAd(adRequest);
                                    }
                                }, 3000);

                            }
                           //Toast.makeText(MainActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();
                            //getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,
                           //         activeFragment).commit();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,
                            activeFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    Toast.makeText(MainActivity.this,"Download complete", Toast.LENGTH_SHORT).show();
                    notifyUser();
                } else {
                    Toast.makeText(MainActivity.this,"${appUpdateInfo.installStatus()", Toast.LENGTH_SHORT).show();
                }
            }
        });
        particleView.resume();

    }

    @Override
    protected void onPause() {
        particleView.pause();
        super.onPause();
    }

    @Override
    public void onStateUpdate(InstallState installState) {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            notifyUser();
        }
    }

    private void notifyUser() {
        Snackbar.make(appVersionTextView,"Please restart app to install update.", Snackbar.LENGTH_INDEFINITE).setAction("Restart", v -> appUpdateManager.completeUpdate()).show();
    }

    public void openAboutInfo(View view) {

    }

    /*private void notifyUser()
    {
        Snackbar
                .make(appVersionTextView, R.string.restart_to_update, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_restart) {
        appUpdateManager.completeUpdate()
        appUpdateManager.unregisterListener(this)
    }
            .show()
    }
    }*/

    /*   private void showFabOptions() {
        val popupMenu = popupMenu {
            section {
                item {
                    label = SortEnum.ALPHABETICAL.name
                    callback = {
                            indiaFragment.onSort(SortEnum.ALPHABETICAL)
                    }
                }

                item {
                    label = SortEnum.ASCENDING.name
                    callback = {
                            indiaFragment.onSort(SortEnum.ASCENDING)
                    }
                }

                item {
                    label = SortEnum.DESCENDING.name
                    callback = {
                            indiaFragment.onSort(SortEnum.DESCENDING)
                    }
                }
            }
        }

        popupMenu.show(this, fab)
    }*/
/*
    @Override
    public void onBackPressed() {
        if (activeFragment == overviewFragment) {
            if (overviewFragment.handleBackPress()) {

            } else {
                finish();
            }
        } else if (activeFragment == indiaFragment) {
            handleOverviewAction();
        } else {
            super.onBackPressed();
        }
    }*/
}

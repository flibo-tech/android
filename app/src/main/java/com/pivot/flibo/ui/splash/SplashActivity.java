package com.pivot.flibo.ui.splash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.pivot.flibo.R;
import com.pivot.flibo.data.network.model.notification.Detail;
import com.pivot.flibo.ui.base.BaseActivity;
import com.pivot.flibo.ui.login.LoginActivity;
import com.pivot.flibo.ui.main.MainActivity;
import com.pivot.flibo.utils.AppLogger;

import static com.pivot.flibo.utils.AppConstants.BUTTON_INDEX;
import static com.pivot.flibo.utils.AppConstants.CHANNEL_ID;
import static com.pivot.flibo.utils.AppConstants.CHANNEL_NAME;
import static com.pivot.flibo.utils.AppConstants.NOTIFICATION_ID;
import static com.pivot.flibo.utils.AppConstants.TAPPED_ON;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashPresenter<SplashMvpView> mPresenter;

    @BindView(R.id.logo)
    ImageView logo;

    public static final String URL = "url";

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(SplashActivity.this);

        setUp();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int notificationId = bundle.getInt(NOTIFICATION_ID, 0);
            if (notificationId != 0) {
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.
                                NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);

                Detail detail = new Detail();
                detail.setNotificationId(notificationId);
                detail.setTappedOn(bundle.getString(TAPPED_ON));
                detail.setButtonIndex(bundle.getInt(BUTTON_INDEX));

                NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = nm.getNotificationChannel(CHANNEL_ID);
                    if(channel.getImportance() == NotificationManager.IMPORTANCE_NONE){
                        detail.setSnoozed(true);
                    }
                }

                detail.setTurnedOff(!NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled());

                String url = bundle.getString(URL, null);
                mPresenter.savDetails(detail, url);

                if(url != null){
                    return;
                }
            }
        }
        mPresenter.onViewAttached(logo, getIntent().getData());
    }

    @Override
    public void checkUrl(String url){
        if (url != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            finish();
        }
    }

    @Override
    public void openLoginActivity(String url) {
        Intent intent = LoginActivity.getStartIntent(SplashActivity.this, url);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity(String url) {
        Intent intent = MainActivity.getStartIntent(SplashActivity.this, url);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity(String url, String redirectUrl) {
        Intent intent = MainActivity.getStartIntent(SplashActivity.this, url, redirectUrl);
        startActivity(intent);
        finish();
    }
}
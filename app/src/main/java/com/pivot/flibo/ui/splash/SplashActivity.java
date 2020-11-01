package com.pivot.flibo.ui.splash;

import android.net.Uri;
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

import com.pivot.flibo.R;
import com.pivot.flibo.ui.base.BaseActivity;
import com.pivot.flibo.ui.login.LoginActivity;
import com.pivot.flibo.ui.main.MainActivity;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashPresenter<SplashMvpView> mPresenter;

    @BindView(R.id.logo)
    ImageView logo;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
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
        mPresenter.onViewAttached(logo, getIntent().getData());
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
}
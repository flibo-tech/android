package com.pivot.flibo.ui.login;

import android.app.Activity;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pivot.flibo.BuildConfig;
import com.pivot.flibo.R;
import com.pivot.flibo.service.fcm.MyFirebaseMessagingService;
import com.pivot.flibo.ui.base.BaseActivity;
import com.pivot.flibo.ui.main.MainActivity;
import com.pivot.flibo.utils.AppConstants;
import com.pivot.flibo.utils.AppLogger;
import com.pivot.flibo.utils.CommonUtils;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginPresenter<LoginMvpView> mPresenter;

    @BindView(R.id.logo)
    ImageView logoImageView;

    @BindView(R.id.heading)
    TextView headingTextView;

    @BindView(R.id.google_sign_in_button)
    LinearLayout googleSignInContainer;

    @BindView(R.id.facebook_login_button)
    LinearLayout facebookSignInContainer;

    @BindView(R.id.cover)
    ImageView cover;

    private final static String URL = "url";
    private String redirectUrl;

    private GoogleSignInClient googleSignInClient;
    private final int REQUEST_CODE_GOOGLE_SIGN_IN = 102;
    private CallbackManager callbackManager;

    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(LoginActivity.this);

        setUp();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        redirectUrl = getIntent().getStringExtra(URL);
        cover.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
            }
        }).start();

        facebookSignInContainer.startAnimation(inFromLeftAnimation(500));
        googleSignInContainer.startAnimation(inFromLeftAnimation(650));

        logoImageView.startAnimation(inFromRightAnimation(500));
        headingTextView.startAnimation(inFromRightAnimation(650));

        callbackManager = CallbackManager.Factory.create();
        mPresenter.onViewPrepared(callbackManager);
        GoogleSignInOptions gso = CommonUtils.getGSO();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.facebook_login_button)
    public void facebookLogin(View view){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
    }

    @Override
    public void openMainActivity(String userId) {
        String url = BuildConfig.SITE_URL+ userId+"&webview=true&releaseNo=4";
        if(redirectUrl != null && !TextUtils.isEmpty(redirectUrl)){
            startActivity(MainActivity.getStartIntent(this, url, redirectUrl));
            finish();
        } else{
            startActivity(MainActivity.getStartIntent(this, url));
            finish();
        }
    }

    @OnClick(R.id.google_sign_in_button)
    public void signIn(View view){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case REQUEST_CODE_GOOGLE_SIGN_IN:
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        mPresenter.updateGoogleAccountDetails(account);
                    } catch (ApiException e) {
                        AppLogger.e("Sign In Failed: "+e.getStatusCode());
                    }
                    break;
            }
    }

    private Animation inFromRightAnimation(int milli) {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(milli);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation inFromLeftAnimation(int milli) {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(milli);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }
}
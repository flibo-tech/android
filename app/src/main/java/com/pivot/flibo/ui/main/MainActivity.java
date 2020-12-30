package com.pivot.flibo.ui.main;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.pivot.flibo.R;
import com.pivot.flibo.ui.base.BaseActivity;
import com.pivot.flibo.ui.login.LoginActivity;
import com.pivot.flibo.ui.splash.SplashActivity;
import com.pivot.flibo.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject
    MainPresenter<MainMvpView> mPresenter;
    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.webview)
    WebView webView;

    private static String URL = "url";
    private static String REDIRECT_URL = "redirectUrl";

    public static Intent getStartIntent(Context context, String url, String redirectUrl) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(REDIRECT_URL, redirectUrl);
        return intent;
    }

    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(MainActivity.this);

        setUp();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
        String url = getIntent().getStringExtra(URL);
        String redirectUrl = getIntent().getStringExtra(REDIRECT_URL);
        if (isNetworkConnected()) {
            mPresenter.onViewPrepared(webView, url, redirectUrl);
        } else {
            showMessage("No internet connection!");
        }
    }

    @Override
    public void requestMicrophonePermission() {
        if (!(hasPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) && hasPermission(Manifest.permission.RECORD_AUDIO))) {
            requestPermissionsSafely(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS}, 1);
        }
    }

    @Override
    public void shareProfile(String imageUrl, String profileUrl) {
        Glide.with(MainActivity.this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Uri uri = getLocalBitmapUri(resource);

                        copyTextToClipboard(profileUrl);

                        Intent i = new Intent(Intent.ACTION_SEND);
                        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_TEXT, profileUrl);
                        i.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION );
                        i.putExtra(Intent.EXTRA_STREAM, uri);

                        hideLoading();
                        startActivity(i);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        hideLoading();
                        showMessage(R.string.some_error);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        showLoading();
                    }
                });
    }

    private void copyTextToClipboard(String profileUrl){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getStringById(R.string.app_name), profileUrl);
        clipboard.setPrimaryClip(clip);
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(
                    MainActivity.this,
                    getStringById(R.string.authority), //(use your app signature + ".provider" )
                    file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public String getStringById(int id){
        return getResources().getString(id);
    }

    @Override
    public void logout() {
        GoogleSignInOptions gso = CommonUtils.getGSO();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        LoginManager.getInstance().logOut();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(MainActivity.this, null);
        startActivity(intent);
        finish();
    }

    @Override
    public void startBrowser(String url) {
        if (url.startsWith("intent://")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    PackageManager packageManager = getPackageManager();
                    ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (info != null) {
                        startActivity(intent);
                    } else {
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        browser(fallbackUrl);
                    }
                }
            } catch (URISyntaxException e) {
            }
        } else {
            browser(url);
        }
    }

    private void browser(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.canGoBack()) {
            webView.goBack();
            mPresenter.goBack();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showMessage(R.string.click_back);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
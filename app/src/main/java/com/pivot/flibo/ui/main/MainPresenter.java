package com.pivot.flibo.ui.main;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pivot.flibo.BuildConfig;
import com.pivot.flibo.R;
import com.pivot.flibo.data.DataManager;
import com.pivot.flibo.ui.base.BasePresenter;
import com.pivot.flibo.utils.AppConstants;
import com.pivot.flibo.utils.AppLogger;
import com.pivot.flibo.utils.rx.SchedulerProvider;

import org.w3c.dom.Text;

import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    private static final String TAG = "MainPresenter";
    private boolean redirected = false;

    private int VISITED = 0;

    @Inject
    public MainPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared(WebView webView, String url, String redirectUrl) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void shareCollage(String imageUrl, String profileUrl)
            {
                getMvpView().shareProfile(imageUrl, profileUrl);
            }
        }, getMvpView().getStringById(R.string.javascript_interface));

        WebViewClient client = new WebViewClient() {

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                VISITED += 1;
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.equals(AppConstants.SITE_HOME_URL)){
                    getDataManager().setUserAsLoggedOut();
                    getMvpView().logout();
                    getMvpView().openLoginActivity();
                    return false;
                }

                if(!url.contains(AppConstants.BASE_SITE_URL)){
                    view.stopLoading();
                    getMvpView().startBrowser(url);
                    return false;
                }

                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(!redirected){
                    if (redirectUrl != null && !TextUtils.isEmpty(redirectUrl)) {
                        webView.loadUrl(redirectUrl);
                    }
                    redirected = true;
                }
            }
        };

        webView.setWebViewClient(client);
        webView.loadUrl(url);
    }

    @Override
    public void goBack() {
        VISITED -= 2;
    }

    @Override
    public boolean canGoBack() {
        if(VISITED < 2 ){
            return false;
        }
        return VISITED != 2;
    }
}
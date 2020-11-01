package com.pivot.flibo.ui.splash;

import android.net.Uri;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.pivot.flibo.data.DataManager;
import com.pivot.flibo.ui.base.BasePresenter;
import com.pivot.flibo.utils.AppConstants;
import com.pivot.flibo.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Inject;

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    private static final String TAG = "SplashPresenter";

    @Inject
    public SplashPresenter(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewAttached(ImageView logo, Uri data) {
        logo.animate().alpha(1).setDuration(1500).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        decideNextActivity(data);
                    }
                }, 1000);
            }
        }).start();
    }

    private void decideNextActivity(Uri data) {
        if (getDataManager().getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getMvpView().openLoginActivity(data != null  ? data.toString() : null);
        } else {
            String url = AppConstants.SITE_URL+ getDataManager().getCurrentUserId()+"&webview=true";
            getMvpView().openMainActivity(data != null  ? data.toString() : url);
        }
    }
}
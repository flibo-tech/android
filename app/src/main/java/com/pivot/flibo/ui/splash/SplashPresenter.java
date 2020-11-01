package com.pivot.flibo.ui.splash;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.androidnetworking.error.ANError;
import com.pivot.flibo.BuildConfig;
import com.pivot.flibo.data.DataManager;
import com.pivot.flibo.data.network.model.ApiResponse;
import com.pivot.flibo.data.network.model.notification.Detail;
import com.pivot.flibo.data.network.model.notification.DetailResponse;
import com.pivot.flibo.ui.base.BasePresenter;
import com.pivot.flibo.utils.AppConstants;
import com.pivot.flibo.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

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
    public void savDetails(Detail detail, String url) {
        getCompositeDisposable().add(getDataManager()
                .doSaveNotificationResponseApiCall(detail)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DetailResponse>() {
                    @Override
                    public void accept(DetailResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().checkUrl(url);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().checkUrl(url);
                    }
                }));
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

        if(!isViewAttached()){
            return;
        }

        if (getDataManager().getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getMvpView().openLoginActivity(data != null  ? data.toString() : null);
        } else {
            String url = BuildConfig.SITE_URL+ getDataManager().getCurrentUserId()+"&webview=true";
            if(data != null){
                String intentUrl = data.toString();
                if(!intentUrl.contains(BuildConfig.BASE_SITE_URL)){
                    getMvpView().openMainActivity(url, intentUrl);
                } else {
                    getMvpView().openMainActivity(intentUrl);
                }
            } else {
                getMvpView().openMainActivity(url);
            }
        }
    }
}
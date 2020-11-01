package com.pivot.flibo.di.module;

import android.app.Application;
import android.content.Context;

import com.pivot.flibo.BuildConfig;
import com.pivot.flibo.data.AppDataManager;
import com.pivot.flibo.data.DataManager;
import com.pivot.flibo.data.network.ApiHeader;
import com.pivot.flibo.data.network.ApiHelper;
import com.pivot.flibo.data.network.AppApiHelper;
import com.pivot.flibo.data.prefs.AppPreferencesHelper;
import com.pivot.flibo.data.prefs.PreferencesHelper;
import com.pivot.flibo.di.ApiInfo;
import com.pivot.flibo.di.ApplicationContext;
import com.pivot.flibo.di.PreferenceInfo;
import com.pivot.flibo.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey,
                                                           PreferencesHelper preferencesHelper) {
        return new ApiHeader.ProtectedApiHeader(
                apiKey,
                preferencesHelper.getCurrentUserId(),
                preferencesHelper.getAccessToken());
    }
}

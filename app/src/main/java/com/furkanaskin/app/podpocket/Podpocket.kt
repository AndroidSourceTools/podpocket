package com.furkanaskin.app.podpocket

import android.app.Activity
import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.furkanaskin.app.podpocket.di.component.DaggerApplicationComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pushbots.push.Pushbots
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject
import timber.log.Timber

class Podpocket : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.builder()
            .applicationBind(this)
            .build()
            .inject(this)

        AndroidThreeTen.init(this)
        initFabric()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
        Pushbots.sharedInstance().init(this)
    }

    private fun initFabric() {

        val core = CrashlyticsCore.Builder()
            .disabled(BuildConfig.DEBUG)
            .build()

        Fabric.with(this, Crashlytics.Builder().core(core).build(), Answers())
    }
}

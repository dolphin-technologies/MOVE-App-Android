package io.dolphin.move.android.firebase.data

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import io.dolphin.move.android.BuildConfig
import io.dolphin.move.android.R
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface RemoteConfigRepository {
    fun initRemoteConfigs()
    fun getRemoteConfigs(): RemoteConfigs
}

private const val REMOTE_CONFIG_HELP = "url_help"
private const val REMOTE_CONFIG_IMPRINT = "url_imprint"
private const val REMOTE_CONFIG_PRIVACY = "url_privacy"
private const val REMOTE_CONFIG_PRIVACY_ANALYTICS = "url_privacyanalytics"
private const val REMOTE_CONFIG_TERMS_OF_USE = "url_termsofuse"

class RemoteConfigRepositoryImpl @Inject constructor() : RemoteConfigRepository {

    // Get remote config instance
    private val remoteConfig = Firebase.remoteConfig

    override fun initRemoteConfigs() {
        val minFetchInterval: Long = if (BuildConfig.DEBUG)
            TimeUnit.MINUTES.toSeconds(30)
        else
            TimeUnit.HOURS.toSeconds(12)

        val configSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 30L
            minimumFetchIntervalInSeconds = minFetchInterval
        }

        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("Successful ${it.result}")
                } else {
                    Timber.d("Failed ${it.result}")
                }
                val remoteConfigs = getRemoteConfigs()
                Timber.d("url_help -> ${remoteConfigs.urlHelp}")
                Timber.d("url_imprint -> ${remoteConfigs.urlImprint}")
                Timber.d("url_privacy -> ${remoteConfigs.urlPrivacy}")
                Timber.d("url_privacyanalytics -> ${remoteConfigs.urlPrivacyAnalytics}")
                Timber.d("url_termsofuse -> ${remoteConfigs.urlTermsOfUse}")
            }.addOnFailureListener {
                Timber.d("Exception ${it.message}")
            }
    }

    override fun getRemoteConfigs(): RemoteConfigs {
        return RemoteConfigs(
            urlHelp = remoteConfig.getString(REMOTE_CONFIG_HELP),
            urlImprint = remoteConfig.getString(REMOTE_CONFIG_IMPRINT),
            urlPrivacy = remoteConfig.getString(REMOTE_CONFIG_PRIVACY),
            urlPrivacyAnalytics = remoteConfig.getString(REMOTE_CONFIG_PRIVACY_ANALYTICS),
            urlTermsOfUse = remoteConfig.getString(REMOTE_CONFIG_TERMS_OF_USE)
        )
    }
}
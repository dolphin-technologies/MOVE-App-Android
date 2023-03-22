package io.dolphin.move.android.basedata.storage

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserSharePreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppSharePreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecureSharedPreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
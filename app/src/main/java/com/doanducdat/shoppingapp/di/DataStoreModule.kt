package com.doanducdat.shoppingapp.di

import android.content.Context
import com.doanducdat.shoppingapp.utils.MyDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun provideMyDataStore(@ApplicationContext context:Context):MyDataStore{
        return MyDataStore(context)
    }
}
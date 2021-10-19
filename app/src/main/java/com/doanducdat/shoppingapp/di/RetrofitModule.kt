package com.doanducdat.shoppingapp.di

import com.doanducdat.shoppingapp.retrofit.*
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(AppConstants.Server.PUBLIC_HOST)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit.Builder): UserAPI {
        return retrofit.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideProductService(retrofit: Retrofit.Builder): ProductAPI {
        return retrofit.build().create(ProductAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideOrderService(retrofit: Retrofit.Builder): OrderAPI {
        return retrofit.build().create(OrderAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewService(retrofit: Retrofit.Builder): ReviewAPI {
        return retrofit.build().create(ReviewAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCommentService(retrofit: Retrofit.Builder): CommentAPI {
        return retrofit.build().create(CommentAPI::class.java)
    }

}
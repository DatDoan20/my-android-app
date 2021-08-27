//package com.doanducdat.shoppingapp.di
//
//import com.doanducdat.shoppingapp.utils.PhoneAuthentication
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.scopes.ViewModelScoped
//
//@Module
//@InstallIn(ViewModelComponent::class)
//object PhoneAuthModule {
//    @ViewModelScoped
//    @Provides
//    fun providePhoneAuthentication(): PhoneAuthentication {
//        return PhoneAuthentication()
//    }
//}
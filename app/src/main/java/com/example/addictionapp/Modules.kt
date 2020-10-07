package com.example.addictionapp

import com.example.addictionapp.data.ReflectionDatabase
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.ReflectionRepositoryImpl
import com.example.addictionapp.data.blocklist.BlocklistDatabase
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.blocklist.BlocklistRepositoryImpl
import com.example.addictionapp.ui.apps.AppSelectionViewModel
import com.example.addictionapp.ui.overview.OverviewViewModel
import com.example.addictionapp.ui.reflection.create.WhatElseViewModel
import com.example.addictionapp.ui.reflection.detail.ReflectionDetailViewModel
import com.example.addictionapp.ui.reflection.list.ReflectionListViewModel
import com.example.addictionapp.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ReflectionDatabase.getInstance(androidContext()) }
    single { get<ReflectionDatabase>().reflectionDao() }
    single { BlocklistDatabase.getInstance(androidContext()) }
    single { get<BlocklistDatabase>().blocklistDao() }
    single<ReflectionRepository> { ReflectionRepositoryImpl(get()) }
    single<BlocklistRepository> { BlocklistRepositoryImpl(get()) }
    viewModel { OverviewViewModel(get()) }
    viewModel { ReflectionListViewModel(get()) }
    viewModel { ReflectionDetailViewModel(get()) }
    viewModel { WhatElseViewModel(get()) }
    viewModel { AppSelectionViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}

package com.example.addictionapp

import com.example.addictionapp.data.ReflectionDatabase
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.ReflectionRepositoryImpl
import com.example.addictionapp.ui.reflection.create.WhatElseViewModel
import com.example.addictionapp.ui.reflection.detail.ReflectionDetailViewModel
import com.example.addictionapp.ui.reflection.list.ReflectionListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ReflectionDatabase.getInstance(androidContext()) }
    single { get<ReflectionDatabase>().reflectionDao() }
    single<ReflectionRepository> { ReflectionRepositoryImpl(get()) }
    viewModel { ReflectionListViewModel(get()) }
    viewModel { ReflectionDetailViewModel(get()) }
    viewModel { WhatElseViewModel(get()) }
}

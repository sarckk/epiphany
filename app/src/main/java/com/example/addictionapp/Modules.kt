package com.example.addictionapp

import com.example.addictionapp.data.reflections.ReflectionDatabase
import com.example.addictionapp.data.reflections.ReflectionRepository
import com.example.addictionapp.data.reflections.ReflectionRepositoryImpl
import com.example.addictionapp.data.blocklist.BlocklistDatabase
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.blocklist.BlocklistRepositoryImpl
import com.example.addictionapp.data.suggestions.SuggestionDatabase
import com.example.addictionapp.data.suggestions.SuggestionRepository
import com.example.addictionapp.data.suggestions.SuggestionRepositoryImpl
import com.example.addictionapp.ui.onboarding.apps.AppSelectionViewModel
import com.example.addictionapp.ui.overview.OverviewViewModel
import com.example.addictionapp.ui.reflection.create.WhatElseViewModel
import com.example.addictionapp.ui.reflection.detail.ReflectionDetailViewModel
import com.example.addictionapp.ui.reflection.list.ReflectionListViewModel
import com.example.addictionapp.ui.splash.SplashViewModel
import com.example.addictionapp.ui.suggestions.SuggestionsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ReflectionDatabase.getInstance(androidContext()) }
    single { get<ReflectionDatabase>().reflectionDao() }
    single { BlocklistDatabase.getInstance(androidContext()) }
    single { get<BlocklistDatabase>().blocklistDao() }
    single { SuggestionDatabase.getInstance(androidContext()) }
    single { get<SuggestionDatabase>().suggestionDao() }
    single<ReflectionRepository> { ReflectionRepositoryImpl(get()) }
    single<BlocklistRepository> { BlocklistRepositoryImpl(get()) }
    single<SuggestionRepository> { SuggestionRepositoryImpl(get()) }
    viewModel { OverviewViewModel(get()) }
    viewModel { ReflectionListViewModel(get()) }
    viewModel { ReflectionDetailViewModel(get()) }
    viewModel { WhatElseViewModel(get()) }
    viewModel { AppSelectionViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SuggestionsViewModel(get()) }
}

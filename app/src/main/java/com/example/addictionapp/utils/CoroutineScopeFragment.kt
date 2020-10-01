package com.example.addictionapp.utils

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class CoroutineScopeFragment(override val coroutineContext: CoroutineContext) : CoroutineScope, Fragment() {
}
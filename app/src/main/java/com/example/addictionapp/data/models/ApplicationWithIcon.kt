package com.example.addictionapp.data.models;

import android.graphics.drawable.Drawable
import kotlin.reflect.KFunction2

class ApplicationWithIcon(
    val icon: Drawable,
    val name: String,
    val packageName: String,
    val addToBlacklistedApps: (String, String) -> Unit,
    val removeFromBlacklistedApps: (String, String) -> Unit
)
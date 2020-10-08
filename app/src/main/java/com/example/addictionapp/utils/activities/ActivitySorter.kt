package com.example.addictionapp.utils.activities

import java.util.*

class ActivitySorter :
    Comparator<Activity> {
    // Used for sorting in ascending order of
    // roll number
    override fun compare(
        a: Activity,
        b: Activity
    ): Int {
        return a.score - b.score
    }
}
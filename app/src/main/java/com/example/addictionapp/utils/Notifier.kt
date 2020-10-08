package com.example.addictionapp.utils

import java.util.*

class Notifier {
    fun shouldNotify(hoursPreviousDay: Long, currentTimeOpen: Long): Boolean {
        val rand = Random()
        val index = rand.nextDouble()
        val personalCoefficient = 6 / hoursPreviousDay.toDouble()
        val probability = Math.abs(
            1 / Math.log10(
                Math.pow(
                    currentTimeOpen.toDouble(),
                    personalCoefficient
                ) / 30
            )
        )
        return (index < probability)
    }
}

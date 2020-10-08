package com.example.addictionapp.utils

import java.util.*

class Notifier {
    fun shouldNotify(hoursPreviousDay: Long, currentTimeOpen: Long): Boolean {
        val rand = Random()
        val index = 10000//rand.nextDouble()
        val personalCoefficient = 1 / hoursPreviousDay.toDouble()
        val probability = Math.abs(
            1 / Math.log10(
                Math.pow(
                    currentTimeOpen.toDouble(),
                    personalCoefficient
                ) / 30
            )
        )
        return true//(index < probability)
    }
}

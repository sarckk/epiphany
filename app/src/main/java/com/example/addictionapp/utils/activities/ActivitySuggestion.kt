package com.example.addictionapp.data.models.suggestion
import com.example.addictionapp.data.models.Suggestion

class ActivitySuggestion {
    lateinit var activityList: MutableList<Suggestion>

    fun getActivity(currentTime: String, currentActivity: String): String {
        activityList = mockNewActivity()

        var maxScore = 0
        var maxScoreName = ""
        activityList.forEach {
            val currentScore = getScore(it, currentTime, currentActivity)
            if (currentScore > maxScore) {
                maxScore = currentScore
                maxScoreName = it.activityName
            }
        }

        return maxScoreName
    }

    private fun getScore(suggestion: Suggestion, currentTime: String, currentActivity: String): Int{
        var score = 0

        when(currentTime) {
            "morning" -> score += suggestion.timesMorning
            "day" -> score += suggestion.timesDay
            "evening" -> score += suggestion.timesEvening
        }

        when(currentActivity) {
            "walking" -> score += suggestion.timesWalking
            "standing" -> score += suggestion.timesStanding
        }

        return score
    }

    private fun mockNewActivity(): MutableList<Suggestion> {
        val activityA = Suggestion(0, "watch all Tarantino's movies", 1, 3, 2, 2, 1)
        val activityB = Suggestion(0, "pet someone's hamster", 2, 3, 1, 1, 2)
        val activityC = Suggestion(0, "blow soap bubbles", 1, 0, 3, 1, 1)
        val activityD = Suggestion(0, "bite a table", 1,3,2,1,1)
        val activityE = Suggestion(0, "Bake a cake",1,5,2,1,0)
        val activityF = Suggestion(0, "Go for a walk",0,2,5,0, 6)
        val currentActivities = mutableListOf(
            activityA,
            activityB,
            activityC,
            activityD,
            activityE,
            activityF
        )
        return currentActivities
    }
}
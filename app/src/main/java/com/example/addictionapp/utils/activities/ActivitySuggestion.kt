package com.example.addictionapp.utils.activities
import java.util.*

class ActivitySuggestion {
    fun updateTags() {}

    companion object {
        var activityNameList = arrayOf(
            "watch all Tarantino's movies",
            "pet someone's hamster",
            "blow soap bubbles",
            "bite a table"
        )
        lateinit var activityList: MutableList<Activity>

        fun main(args: Array<String>) {
            activityList = mockNewActivity()
            for (i in activityList.indices) {
                activityList[i].setScore()
            }
            val suggestions = makeSelectionArray(activityList)
            val suggestion = giveFinalSuggestion(suggestions)
            println(suggestion)
        }

        private fun mockNewActivity(): MutableList<Activity> {
            val activityA = Activity("watch all Tarantino's movies")
            val activityB = Activity("pet someone's hamster")
            val activityC = Activity("blow soap bubbles")
            val activityD = Activity("bite a table")
            val activityE = Activity("Bake a cake")
            val activityF = Activity("Go for a walk")
            activityA.time = 1
            activityB.time = 0
            activityC.time = 3
            activityD.time = 0
            activityE.time = 2
            activityF.time = 2
            activityA.loc = 2
            activityB.loc = 3
            activityC.loc = 0
            activityD.loc = 1
            activityE.loc = 1
            activityF.loc = 3
            activityA.status = 2
            activityB.status = 1
            activityC.status = 0
            activityD.status = 4
            activityE.status = 4
            activityF.status = 1
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

        fun makeSelectionArray(suggestions: MutableList<Activity>): Array<String> {
            Collections.sort(
                suggestions,
                ActivitySorter()
            )
            val rand = Random()
            val randomExplore =
                rand.nextInt(activityNameList.size)
            val randomExploit1 =
                rand.nextInt(activityNameList.size / 3)
            val randomExploit2 =
                rand.nextInt(activityNameList.size / 3)
            val randomExploit3 =
                rand.nextInt(activityNameList.size / 3)
            return arrayOf(
                activityNameList[randomExplore],
                suggestions[randomExploit1].activityName,
                suggestions[randomExploit2].activityName,
                suggestions[randomExploit3].activityName
            )
        }

        fun giveFinalSuggestion(suggestionArray: Array<String>): String {
            val rand = Random()
            val suggestionIndex = rand.nextInt(suggestionArray.size)
            return suggestionArray[suggestionIndex]
        }
    }
}
package com.example.addictionapp.utils.activities

class Activity internal constructor(var activityName: String) {
	var time = 0
	var loc = 0
	var status = 0
	var score = 0
    fun setScore() {
        score = time / 6 + loc / 3 + status / 4
    }

    fun getFeatureValue(activityName: String?, feature: String): Int {
        //int feature =

        // if JSON then possible structure:
        // [{"activity1":{
        //			"timeEarlyMorning": 0,
        //			"timeMorning": 0,
        //			"timeAfternoon": 0,
        //			"timeEarlyEvening": 0,
        //			"timeEvening": 0,
        //			"timeNight": 0,
        //			"statusStanding": 0,
        //			"statusSitting": 0,
        //			"statusWalking": 0,},
//			{"activity2":{
        //			"timeEarlyMorning": 0,
        //			"timeMorning": 0,
        //			"timeAfternoon": 0,
        //			"timeEarlyEvening": 0,
        //			"timeEvening": 0,
        //			"timeNight": 0,
        //			"statusStanding": 0,
        //			"statusSitting": 0,
        //			"statusWalking": 0,}}]
        return 0
    }

    init {
        val time = getFeatureValue(activityName, "time")
        val loc = getFeatureValue(activityName, "loc")
        val status = getFeatureValue(activityName, "status")
    }
}
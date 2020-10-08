package com.example.addictionapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName="suggestion_table",
)
data class Suggestion (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name="activity_name")
    val activityName: String,
    @ColumnInfo(name="times_morning")
    val timesMorning: Int,
    @ColumnInfo(name="times_day")
    val timesDay: Int,
    @ColumnInfo(name="times_evening")
    val timesEvening: Int,
    @ColumnInfo(name="times_walking")
    val timesWalking: Int,
    @ColumnInfo(name="times_standing")
    val timesStanding: Int
)

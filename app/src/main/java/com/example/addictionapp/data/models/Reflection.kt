package com.example.addictionapp.data.models

import androidx.room.*

@Entity(
    tableName="reflection_table",
    indices = [Index("date_created")]
)
data class Reflection (
    @PrimaryKey
    @ColumnInfo(name="date_created")
    val dateCreated: String,
    @ColumnInfo(name="well_being_rating")
    val wellBeingRating: String,
    val reflection: String?
)
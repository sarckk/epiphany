package com.example.addictionapp.data.suggestions

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.data.models.Suggestion
import com.example.addictionapp.data.reflections.ReflectionDao

@Database(
    entities = [Suggestion::class],
    version = 2,
    exportSchema = false
)
abstract class SuggestionDatabase: RoomDatabase() {
    abstract fun suggestionDao() : SuggestionDao

    // Taken from Google's open-source example:
    // https://github.com/android/sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/data/AppDatabase.kt
    companion object {
        @Volatile private var instance: SuggestionDatabase? = null

        fun getInstance(context: Context): SuggestionDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): SuggestionDatabase {
            return Room.databaseBuilder(context, SuggestionDatabase::class.java, "suggestion_db").build()
        }
    }
}

package com.example.addictionapp.data.reflections

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.addictionapp.data.models.Reflection

@Database(
    entities = arrayOf(Reflection::class),
    version = 1,
    exportSchema = false
)
abstract class ReflectionDatabase: RoomDatabase() {
    abstract fun reflectionDao() : ReflectionDao

    // Taken from Google's open-source example:
    // https://github.com/android/sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/data/AppDatabase.kt
    companion object {
        @Volatile private var instance: ReflectionDatabase? = null

        fun getInstance(context: Context): ReflectionDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                   instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): ReflectionDatabase {
            return Room.databaseBuilder(context, ReflectionDatabase::class.java, "reflection_db").build()
        }
    }
}

package com.example.addictionapp.data.blocklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.addictionapp.data.blocklist.BlocklistDao
import com.example.addictionapp.data.models.Application

@Database(
    entities = arrayOf(Application::class),
    version = 1,
    exportSchema = false
)
abstract class BlocklistDatabase: RoomDatabase() {
    abstract fun blocklistDao() : BlocklistDao

    // Taken from Google's open-source example:
    // https://github.com/android/sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/data/AppDatabase.kt
    companion object {
        @Volatile private var instance: BlocklistDatabase? = null

        fun getInstance(context: Context): BlocklistDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): BlocklistDatabase {
            return Room.databaseBuilder(context, BlocklistDatabase::class.java, "blocklist_db").allowMainThreadQueries().build()
        }
    }
}
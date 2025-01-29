package com.example.codewizard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Project::class], version = 3, exportSchema = false)
@TypeConverters(StringListTypeConverter::class)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var Instance: ProjectDatabase? = null

        fun getDatabase(context: Context): ProjectDatabase {
            // if instance is null, create one
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ProjectDatabase::class.java, "project_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
package com.kabo.a24_makany.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PlaceEntity::class] , version = 1)
abstract class PlacesDatabase : RoomDatabase() {
    abstract val dao : PlacesDao

    companion object{
        @Volatile
        private var INSTANCE : PlacesDatabase? = null
        fun getInstance(context : Context) : PlacesDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room
                    .databaseBuilder(context, PlacesDatabase::class.java, "places.db")
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
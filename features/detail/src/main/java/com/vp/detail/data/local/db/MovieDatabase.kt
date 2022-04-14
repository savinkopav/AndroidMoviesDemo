package com.vp.detail.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.detail.data.local.db.dao.MovieDAO
import com.vp.detail.data.local.db.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDAO

    companion object {

        @Volatile
        private var mInstance: MovieDatabase? = null

        @JvmStatic
        fun getInstance(context: Context) : MovieDatabase = mInstance ?: synchronized(this) {
            mInstance ?: return Room.databaseBuilder(context, MovieDatabase::class.java, "database").build().also {
                mInstance = it
            }
        }
    }
}
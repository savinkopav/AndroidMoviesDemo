package com.vp.detail.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movies", indices = [Index(value = ["uid"], unique = true)])
data class Movie(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "year")
    val year: String,
    @ColumnInfo(name = "runtime")
    val runtime: String,
    @ColumnInfo(name = "director")
    val director: String,
    @ColumnInfo(name = "plot")
    val plot: String,
    @ColumnInfo(name = "poster")
    val poster: String,
    @ColumnInfo(name = "uid")
    val uid: String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
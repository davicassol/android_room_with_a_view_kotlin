package com.example.roomwordsample.data

import androidx.room.Entity // <--- ESSA É A IMPORTANTE
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "word_table")
data class Word(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "word")
    val word: String
)
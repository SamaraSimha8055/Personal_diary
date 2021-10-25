package com.example.personaldiary.data

import android.provider.BaseColumns

object DatabaseManager {

    object DiaryEntry : BaseColumns{

        const val TABLE_NAME = "diaries"

        const val ID = BaseColumns._ID

        const val COLUMN_DATE = "date"

        const val COLUMN_TITLE = "title"

        const val COLUMN_DIARY = "diary"
    }
}
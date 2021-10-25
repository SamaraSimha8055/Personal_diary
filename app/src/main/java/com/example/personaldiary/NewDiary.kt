package com.example.personaldiary

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.personaldiary.data.DiaryDBHelper
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.COLUMN_DATE
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.COLUMN_DIARY
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.COLUMN_TITLE
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.ID
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.TABLE_NAME
import kotlinx.android.synthetic.main.activity_new_diary.*
import java.text.SimpleDateFormat
import java.util.*

class NewDiary : AppCompatActivity() {
    @SuppressLint("SimpleDateFormat")

    private var id = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_diary)

        id = intent.getIntExtra("IDofRow", 0)

        if (id != 0) {
            readDiary(id)
        }

        Log.d("NewDiary", "The Pass ID is : $id")

        val currentDate = SimpleDateFormat("EEE. dd / MMM / yyyy")
        current_date_diary.text = currentDate.format(Date())
    }

    private fun readDiary(id: Int) {

        val mDBHelper = DiaryDBHelper(this)

        val db = mDBHelper.readableDatabase

        val projection = arrayOf(COLUMN_DATE, COLUMN_TITLE, COLUMN_DIARY)

        val selection = "$ID = ?"
        val selectionArgs = arrayOf("$id")

        val cursor: Cursor = db.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs, null, null, null
        )

        val dateColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE)
        val titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
        val diaryColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DIARY)

        while (cursor.moveToNext()) {

            val currentDate = cursor.getString(dateColumnIndex)
            val currentTitle = cursor.getString(titleColumnIndex)
            val currentDiary = cursor.getString(diaryColumnIndex)

            current_date_diary.text = currentDate
            title_diary.setText(currentTitle)
            diary_text.setText(currentDiary)
        }
        cursor.close()
    }

    private fun insertDiary() {

        val dateString = current_date_diary.text.toString()
        val titleString = title_diary.text.toString().trim { it <= ' ' }
        val diaryString = diary_text.text.toString().trim { it <= ' ' }

        val mDBHelper = DiaryDBHelper(this)

        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_DATE, dateString)
            put(COLUMN_TITLE, titleString)
            put(COLUMN_DIARY, diaryString)
        }

        val rawId = db.insert(TABLE_NAME, null, values)

        if (rawId.equals(-1)) {
            Toast.makeText(this, "Problem in inserting new diary", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Diary has been inserted $rawId", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_diary -> {

                if (id == 0) {
                    insertDiary()
                } else {
                    updateDiary(id)
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateDiary(id : Int) {

        val mDBHelper = DiaryDBHelper(this)

        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, title_diary.text.toString())
            put(COLUMN_DIARY, diary_text.text.toString())
        }

        db.update(TABLE_NAME, values, "$ID = $id", null)
    }

    override fun onStop() {
        super.onStop()
        if (id == 0) {
            insertDiary()
        } else {
            updateDiary(id)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (id == 0) {
            insertDiary()
        } else {
            updateDiary(id)
        }
    }
}
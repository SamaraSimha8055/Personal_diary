package com.example.personaldiary

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.COLUMN_DATE
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.COLUMN_DIARY
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.COLUMN_TITLE
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.ID
import com.example.personaldiary.data.DatabaseManager.DiaryEntry.TABLE_NAME
import com.example.personaldiary.data.Diary
import com.example.personaldiary.data.DiaryDBHelper
import kotlinx.android.synthetic.main.activity_diary.*

class DiaryActivity : AppCompatActivity() {

    private lateinit var mDBHelper: DiaryDBHelper
    private var diaryList: ArrayList<Diary> = ArrayList()
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var adapter : DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        mDBHelper = DiaryDBHelper(this)

        displayDataInfo()

    }

    @SuppressLint("Recycle")
    private fun displayDataInfo() {

        val db = mDBHelper.readableDatabase

        val projection = arrayOf(ID,COLUMN_DATE, COLUMN_TITLE, COLUMN_DIARY)

        val cursor : Cursor = db.query(TABLE_NAME, projection, null, null, null, null, null)

        val idColumnIndex = cursor.getColumnIndexOrThrow(ID)
        val dateColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE)
        val titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
        val diaryColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DIARY)

        while (cursor.moveToNext()){
            val currentId = cursor.getInt(idColumnIndex)
            val currentDate = cursor.getString(dateColumnIndex)
            val currentTitle = cursor.getString(titleColumnIndex)
            val currentDiary = cursor.getString(diaryColumnIndex)

            diaryList.add(Diary(currentId, currentDate, currentTitle, currentDiary))
        }
        cursor.close()

        linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = linearLayoutManager

        adapter = DiaryAdapter(diaryList)
        recycler_view.adapter = adapter


    }

    override fun onStart() {
        super.onStart()
        diaryList.clear()
        displayDataInfo()
    }

    fun createViewDiary(view: View) {
        val intent = Intent(this,NewDiary::class.java)
        startActivity(intent)
    }
}
package com.example.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.notes.notes.Notes
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.util.Date

class NotesTakerActivity : AppCompatActivity() {
    private lateinit var edittext_title: EditText
    private lateinit var edittext_notes: EditText
    private lateinit var image_save: ImageView
    private lateinit var notes: Notes
    private var isOldNote = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_taker)
        image_save = findViewById(R.id.image_save)
        edittext_notes = findViewById(R.id.edit_text_notes)
        edittext_title = findViewById(R.id.edit_text_title)
        notes = Notes()
        try {
            notes = intent.getSerializableExtra("old_notes") as Notes
            edittext_title.setText(notes.title)
            edittext_notes.setText(notes.notes)
            isOldNote = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        image_save.setOnClickListener {
            val title: String = edittext_title.text.toString()
            val description: String = edittext_notes.text.toString()
            if (description.isEmpty()) {
                Toast.makeText(this, "Please add description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dateFormat: DateFormat = getDateTimeInstance()
            val date = Date()
            if(!isOldNote){
                notes = Notes()
            }
            notes.title = title
            notes.notes = description
            notes.date = dateFormat.format(date)

            val intent = Intent()
            intent.putExtra("notes", notes)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
package com.curiousca.roomlivedataviewmodel.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.curiousca.roomlivedataviewmodel.R
import java.text.DateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {


    val EXTRA_ID = "com.curiousca.roomwithview.Activities.EXTRA_ID"
    val EXTRA_TITLE = "com.curiousca.roomwithview.Activities.EXTRA_TITLE"
    val EXTRA_DESCRIPTION = "com.curiousca.roomwithview.Activities.EXTRA_DESCRIPTION"
    val EXTRA_PRIORITY = "com.curiousca.roomwithview.Activities.EXTRA_PRIORITY"
    val EXTRA_DATE = "com.curiousca.roomwithview.Activities.EXTRA_Date"

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var numberPickerPriority: NumberPicker? = null
    private var textViewDate: TextView? = null
    private var calendar: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        numberPickerPriority = findViewById(R.id.number_picker_priority)
        textViewDate = findViewById(R.id.text_view_date)
        calendar = Calendar.getInstance()
        val currentDate =
            DateFormat.getDateInstance().format(calendar?.getTime())
        textViewDate?.setText(currentDate)
        numberPickerPriority?.setMaxValue(10)
        numberPickerPriority?.setMinValue(1)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            editTextTitle?.setText(intent.getStringExtra(EXTRA_TITLE))
            editTextDescription?.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            numberPickerPriority?.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1))
            textViewDate?.setText(currentDate)
        } else {
            title = "Add Note"
        }
    }

    private fun saveNote() {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()
        val priority = numberPickerPriority!!.value
        val date = textViewDate!!.text.toString()
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)
        data.putExtra(EXTRA_DATE, date)
        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

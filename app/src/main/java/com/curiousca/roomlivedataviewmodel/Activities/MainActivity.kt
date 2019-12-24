package com.curiousca.roomlivedataviewmodel.Activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.curiousca.roomlivedataviewmodel.DataClasses.Note
import com.curiousca.roomlivedataviewmodel.DataClasses.NoteAdapter
import com.curiousca.roomlivedataviewmodel.Model.NoteViewModel
import com.curiousca.roomlivedataviewmodel.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    var animationDrawable: AnimationDrawable? = null
    var coordinatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: NoteAdapter? = null
    val ADD_NOTE_REQUEST = 1
    val EDIT_NOTE_REQUEST = 2

    private var removedItem = 0
    private var removedCard: Note? = null

    private var noteViewModel: NoteViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fabAddNote: FloatingActionButton = findViewById(R.id.fab_add_note)
        fabAddNote.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        })
        animateBackground()
        buildRecyclerView()
        setAdapter()
        initSwipeToDelete()
    }

    private fun setAdapter() {
        adapter = NoteAdapter()
        recyclerView.setAdapter(adapter)
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes()
            .observe(this, object : Observer<List<Note?>?>() {
                fun onChanged(@Nullable notes: List<Note?>?) { //update recyclerView
                    adapter.submitList(notes)
                }
            })
        adapter.setOnItemClickListener(object : OnItemClickListener() {
            fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId())
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle())
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription())
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority())
                intent.putExtra(AddEditNoteActivity.EXTRA_DATE, note.getDate())
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }

    private fun buildRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setHasFixedSize(true)
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            fun onMove(
                @NonNull recyclerView: RecyclerView?,
                @NonNull viewHolder: RecyclerView.ViewHolder?,
                @NonNull viewHolder1: RecyclerView.ViewHolder?
            ): Boolean {
                return false
            }

            fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, position: Int) {
                noteViewModel?.delete(adapter?.getNoteAt(viewHolder.getAdapterPosition()))
                Toast.makeText(this@MainActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
                removedItem = viewHolder.getAdapterPosition()
                removedCard = adapter?.getNoteAt(viewHolder.getAdapterPosition())
                Snackbar.make(viewHolder.itemView, "Note Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", View.OnClickListener {
                        //add back
                        noteViewModel.insert(removedCard)
                    })
                    .show()
            }
        }).attachToRecyclerView(recyclerView)
    }

    private fun animateBackground() { //declare animation variables
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        animationDrawable = this.coordinatorLayout?.getBackground() as AnimationDrawable?
        //add time change
        animationDrawable!!.setEnterFadeDuration(5000)
        animationDrawable!!.setExitFadeDuration(2000)
        //start animation
        animationDrawable!!.start()
    }

    protected fun onActivityResult(
        requestCode: Int,
        resultCode: Int, @Nullable data: Intent
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description =
                data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, -1)
            val date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE)
            val note = Note(title, description, priority, date)
            noteViewModel.insert(note)
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Cannot update note", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description =
                data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, -1)
            val date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE)
            val note = Note(title, description, priority, date)
            note.setId(id)
            noteViewModel.update(note)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel?.deleteAllNotes()
                Toast.makeText(this, "All notes gone", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}

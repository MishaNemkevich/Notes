package com.example.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.Adapter.Notes_list_adapter
import com.example.notes.DataBase.RoomDB
import com.example.notes.notes.Notes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatActionButton: FloatingActionButton
    private lateinit var notesListAdapter: Notes_list_adapter
    private lateinit var dataBase: RoomDB
    private lateinit var searchNotes: SearchView
    private lateinit var selectedNote: Notes
    lateinit var notes: MutableList<Notes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchNotes = findViewById(R.id.search_notes)
        recyclerView = findViewById(R.id.recycler_home)
        floatActionButton = findViewById(R.id.notes_add)
        dataBase = RoomDB.getInstance(this)
        notes = dataBase.mainDao().getAll().toMutableList()
        updateRecyclre(notes)
        floatActionButton.setOnClickListener {
            val intent = Intent(this, NotesTakerActivity::class.java)
            startActivityForResult(intent, 101)
        }
        searchNotes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })
    }

    private fun filter(newText: String?) {
        val filteredList: MutableList<Notes> = mutableListOf()
        for (singlNote: Notes in notes) {
            if (newText != null) {
                if (singlNote.title.toLowerCase(Locale.ROOT)
                        .contains(newText.toLowerCase(Locale.ROOT))
                    || singlNote.notes.toLowerCase(Locale.ROOT)
                        .contains(newText.toLowerCase(Locale.ROOT))
                    || singlNote.date.toLowerCase(Locale.ROOT)
                        .contains(newText.toLowerCase(Locale.ROOT))
                ) {
                    filteredList.add(singlNote)
                }
            }
        }
        notesListAdapter.filterList(filteredList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                val new_notes: Notes = data?.getSerializableExtra("notes") as Notes
                dataBase.mainDao().insert(new_notes)
                notes.clear()
                notes.addAll(dataBase.mainDao().getAll())
                notesListAdapter.notifyDataSetChanged()
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                val new_notes: Notes = data?.getSerializableExtra("notes") as Notes
                dataBase.mainDao().update(new_notes.ID, new_notes.title, new_notes.notes)
                notes.clear()
                notes.addAll(dataBase.mainDao().getAll())
                notesListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateRecyclre(notes: MutableList<Notes>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        notesListAdapter = Notes_list_adapter(this, notes, notesClickListener)
        recyclerView.adapter = notesListAdapter
    }

    private val notesClickListener = object : NotesClickListener {
        override fun onClick(notes: Notes) {
            val intent = Intent(this@MainActivity, NotesTakerActivity::class.java)
            intent.putExtra("old_notes", notes)
            startActivityForResult(intent, 102)
        }

        override fun onLongClick(notes: Notes, cardView: CardView) {
            selectedNote = Notes()
            selectedNote = notes
            showPopup(cardView)
        }

    }

    private fun showPopup(cardView: CardView) {
        val popupMenu: PopupMenu = PopupMenu(this, cardView)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.pin -> {
                if (selectedNote.pinned) {
                    dataBase.mainDao().pin(selectedNote.ID, false)
                    Toast.makeText(this, "unpinned", Toast.LENGTH_SHORT).show()
                } else {
                    dataBase.mainDao().pin(selectedNote.ID, true)
                    Toast.makeText(this, "pinned", Toast.LENGTH_SHORT).show()
                }
                notes.clear()
                notes.addAll(dataBase.mainDao().getAll())
                notesListAdapter.notifyDataSetChanged()
                return true
            }

            R.id.delete -> {
                dataBase.mainDao().delete(selectedNote)
                notes.remove(selectedNote)
                notesListAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Note removed", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> {
                return false
            }
        }
    }
}
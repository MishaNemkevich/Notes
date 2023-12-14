package com.example.notes.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notes.notes.Notes

@Dao
interface mDataBase {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: Notes)
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll():List<Notes>
    @Query("UPDATE NOTES SET title =:title, notes =:notes WHERE ID =:ID")
    fun update(ID: Int, title: String, notes: String)
    @Delete
    fun delete(notes: Notes)
    @Query("UPDATE NOTES SET pinned =:pin WHERE ID =:ID")
    fun pin(ID: Int, pin: Boolean)
}

package com.example.notes.notes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "notes")
class Notes: Serializable{
    @PrimaryKey(autoGenerate = true)
    var ID = 0
    @ColumnInfo(name="title")
    var title = ""
    @ColumnInfo(name = "notes")
    var notes = ""
    @ColumnInfo(name = "date")
    var date = ""
    @ColumnInfo(name = "pinned")
    var pinned = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Notes

        if (ID != other.ID) return false
        if (title != other.title) return false
        if (notes != other.notes) return false
        if (date != other.date) return false
        if (pinned != other.pinned) return false

        return true
    }
    override fun hashCode(): Int {
        var result = ID
        result = 31 * result + title.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + pinned.hashCode()
        return result
    }
}
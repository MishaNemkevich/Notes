package com.example.notes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.NotesClickListener
import com.example.notes.R
import com.example.notes.notes.Notes
import kotlin.random.Random

class Notes_list_adapter(private var context: Context, private var list: MutableList<Notes>,
                         private var listener: NotesClickListener
) :
    RecyclerView.Adapter<NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun filterList(filteredList: MutableList<Notes>){
        list = filteredList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.text_title.text = list[position].title
        holder.text_title.isSelected
        holder.textv_notes.text = list[position].notes
        holder.textv_data.text = list[position].date
        holder.textv_data.isSelected
        if (list[position].pinned) {
            holder.image_pin.setImageResource(R.drawable.pin)
        } else {
            holder.image_pin.setImageResource(0)
        }
        val color_cod = getColor()
        holder.notes_conteiner.setCardBackgroundColor(holder.itemView.resources.getColor(color_cod, null))
        holder.notes_conteiner.setOnClickListener {
                listener.onClick(list[holder.adapterPosition])
        }
        holder.notes_conteiner.setOnLongClickListener {
            listener.onLongClick(list[holder.adapterPosition], holder.notes_conteiner)
            true
        }
    }
    private fun getColor(): Int{
        val colorCode: MutableList<Int> = mutableListOf()
        colorCode.add(R.color.color2)
        return colorCode[0]
    }
}

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var notes_conteiner: CardView

    var text_title: TextView

    var textv_notes: TextView

    var image_pin: ImageView

    var textv_data: TextView

    init {
        notes_conteiner = itemView.findViewById(R.id.notes_conteiner)
        text_title = itemView.findViewById(R.id.text_title)
        textv_notes = itemView.findViewById(R.id.textv_notes)
        image_pin = itemView.findViewById(R.id.image_pin)
        textv_data = itemView.findViewById(R.id.textv_data)
    }
}
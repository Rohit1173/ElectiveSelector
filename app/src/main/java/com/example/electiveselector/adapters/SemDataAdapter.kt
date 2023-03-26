package com.example.electiveselector.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.electiveselector.R
import com.example.electiveselector.data.ElectiveData
import com.example.electiveselector.data.semData

class SemDataAdapter(private var list: MutableList<Int>) :
    RecyclerView.Adapter<SemDataAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sno:TextView=view.findViewById(R.id.sno)
        val semDataCard:CardView=view.findViewById(R.id.semDataCard)
        val context: Context = view.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.sem_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.sno.text= "$item."
        if(position%2==0){
            holder.semDataCard.setCardBackgroundColor(Color.parseColor("#E2EDFB"))
        }
        else{
            holder.semDataCard.setCardBackgroundColor(Color.parseColor("#CFE0F8"))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
package com.example.electiveselector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.electiveselector.fragments.ElectiveData

class AnnouncementAdapter(private var list: MutableList<ElectiveData>) :
    RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val announcementText: TextView = view.findViewById(R.id.announcementText)
        val context: Context = view.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.announcement_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.announcementText.text =
            "The Elective ${item.electiveNum} for Sem ${item.semNum}  are: \n ${item.sub1.subTitle} , taught by ${item.sub1.facultyName} \n ${item.sub2.subTitle} , taught by ${item.sub2.facultyName} \n ${item.sub3.subTitle} , taught by ${item.sub3.facultyName} "
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
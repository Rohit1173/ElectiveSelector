package com.example.electiveselector.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.electiveselector.R
import com.example.electiveselector.data.ElectiveData

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
        var branches =""
        for(i in item.branchList){
            branches+=i
            branches+=" "
        }
        holder.announcementText.text =
            if(item.sub3.subTitle!="NA") {
                "Elective ${item.electiveNum} Released for Sem ${item.semNum} : \n Branches : ${branches} \n 1. ${item.sub1.subTitle} , by ${item.sub1.facultyName} \n 2. ${item.sub2.subTitle} , by ${item.sub2.facultyName} \n 3. ${item.sub3.subTitle} , by ${item.sub3.facultyName} "
            }
        else{
                "Elective ${item.electiveNum} Released for Sem ${item.semNum} : \n Branches : ${branches} \n 1. ${item.sub1.subTitle} , by ${item.sub1.facultyName} \n 2. ${item.sub2.subTitle} , by ${item.sub2.facultyName}"

            }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
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
import com.example.electiveselector.data.StudentsData
import com.example.electiveselector.data.semData
import org.json.JSONArray

class SemDataAdapter(private var list: JSONArray) :
    RecyclerView.Adapter<SemDataAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sno:TextView=view.findViewById(R.id.sno)
        val userName:TextView=view.findViewById(R.id.nameHeading)
        val subTitle:TextView=view.findViewById(R.id.subHeading)
        val rollNo:TextView=view.findViewById(R.id.rollNo)
        val semDataCard:CardView=view.findViewById(R.id.semDataCard)
        val context: Context = view.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.sem_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.getJSONObject(position)
        holder.sno.text= (position+1).toString()+"."
        val userName= item.getString("userName")
        val userEmail= item.getString("userEmail")
        val sub = item.getJSONObject("sub")
        val title = sub.getString("subTitle")
        val faculty = sub.getString("facultyName")
        val semNum= item.getString("semNum")
        val electiveNum= item.getString("electiveNum")
        holder.userName.text=userName
        holder.rollNo.text=userEmail
        holder.subTitle.text=title

        if(position%2==0){
            holder.semDataCard.setCardBackgroundColor(Color.parseColor("#E2EDFB"))
        }
        else{
            holder.semDataCard.setCardBackgroundColor(Color.parseColor("#CFE0F8"))
        }
    }

    override fun getItemCount(): Int {
        return list.length()
    }
}
package com.example.covidtracker_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker_kotlin.Adapter.ViewHolder
import java.text.NumberFormat

class Adapter(private val context: Context,private val countryList:List<ModelClass>): RecyclerView.Adapter<ViewHolder>() {

    var m:Int = 1


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cases:TextView = itemView.findViewById(R.id.countrycase)
        val country:TextView = itemView.findViewById(R.id.countryname)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item,null,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val modelClass: ModelClass = countryList[position]
        if (m==1){
            holder.cases.text = NumberFormat.getInstance().format(Integer.parseInt(modelClass.cases))
        }else if (m==2){
            holder.cases.text = NumberFormat.getInstance().format(Integer.parseInt(modelClass.recovered))

        }else if (m==3){
            holder.cases.text = NumberFormat.getInstance().format(Integer.parseInt(modelClass.deaths))

        }else{
            holder.cases.text = NumberFormat.getInstance().format(Integer.parseInt(modelClass.active))
        }

        holder.country.text = modelClass.country

    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun filter(charText: String) {

        if (charText == "cases"){

            m=1
        }else if (charText == "recovered"){

            m=2
        }else if (charText == "deaths"){

            m=3
        }else{

            m=4
        }
        notifyDataSetChanged()
    }
}
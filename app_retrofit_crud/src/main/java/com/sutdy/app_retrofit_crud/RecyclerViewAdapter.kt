package com.sutdy.app_retrofit_crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sutdy.app_retrofit_crud.model.Employee
import kotlinx.android.synthetic.main.recyclerview_row.view.*

class RecyclerViewAdapter(val listener: RowClickListener): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    var items: List<Employee> = ArrayList()

    fun setListData(list: List<Employee>) {
        items = list
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.tv_name
        val salary = view.tv_salary

        fun bind(employee: Employee) {
            name.text = employee.name
            salary.text = employee.salary.basic.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(items[position])
        }
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface RowClickListener {
        fun onItemClickListener(employee: Employee)
    }
}
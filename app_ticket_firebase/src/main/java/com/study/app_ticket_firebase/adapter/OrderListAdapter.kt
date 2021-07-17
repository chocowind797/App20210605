package com.study.app_ticket_firebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.study.app_ticket_firebase.R
import com.study.app_ticket_firebase.models.Order
import kotlinx.android.synthetic.main.recyclerview_order_list.view.*

class OrderListAdapter(val listener: OrderOnItemClickListener) :
    RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {
    private var orderList: List<Order> = ArrayList()

    fun setOrderList(orderList: List<Order>) {
        this.orderList = orderList
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.card_view
        val username = view.tv_username
        val allTickets = view.tv_allTickets
        val roundTrip = view.tv_roundTrip
        val oneWay = view.tv_oneWay
        val total = view.tv_total

        fun bind(order: Order) {
            username.text = order.ticket.username
            allTickets.text = order.ticket.allTickets.toString()
            roundTrip.text = order.ticket.roundTrip.toString()
            oneWay.text = order.ticket.oneWay.toString()
            total.text = String.format("%,d", order.ticket.total)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_order_list, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(orderList[position])
        }

        holder.itemView.setOnLongClickListener {
            listener.onItemLongClickListener(orderList[position])
            true
        }
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    interface OrderOnItemClickListener {
        fun onItemClickListener(order: Order)
        fun onItemLongClickListener(order: Order)
    }
}
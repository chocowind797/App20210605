package com.study.app_ticket_firebase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.study.app_ticket_firebase.adapter.OrderListAdapter
import com.study.app_ticket_firebase.models.Order
import com.study.app_ticket_firebase.models.Ticket
import com.study.app_ticket_firebase.models.TicketsStock
import kotlinx.android.synthetic.main.activity_order_list.*

class OrderListActivity : AppCompatActivity(), OrderListAdapter.OrderOnItemClickListener {
    val database = Firebase.database
    var myRef = database.getReference("ticketsStock/transactions")
    var isUser: Boolean = false
    lateinit var username: String
    lateinit var context: Context
    lateinit var ticketsStock: TicketsStock
    lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        context = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        username = intent.getStringExtra("username").toString()

        if (!(username == "" || username == "null"))
            isUser = true
        if (isUser) {
            title = String.format(title.toString(), username)
            myRef = myRef.child(username)
        } else
            title = resources.getString(R.string.all_order_txt)

        recyclerView.apply {
            orderListAdapter = OrderListAdapter(this@OrderListActivity)
            layoutManager = LinearLayoutManager(context)
            adapter = orderListAdapter
        }

        myRef.addValueEventListener(object : ValueEventListener {
            val orderList = ArrayList<Order>()

            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                snapshot.children.forEach {
                    if (isUser) {
                        addRecord(it, username)
                    } else {
                        it.children.forEach { inside ->
                            addRecord(inside, it.key.toString())
                        }
                    }
                }
                orderListAdapter.setOrderList(orderList)
                orderListAdapter.notifyDataSetChanged()
            }

            private fun addRecord(it: DataSnapshot, username: String) {
                val allTickets = it.child("allTickets").value.toString().toInt()
                val roundTrip = it.child("roundTrip").value.toString().toInt()
                val oneWay = it.child("oneWay").value.toString().toInt()
                val total = it.child("total").value.toString().toInt()

                orderList.add(
                    Order(it.key.toString(), Ticket(username, allTickets, roundTrip, oneWay, total))
                )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClickListener(order: Order) {
        // 產生 QR-Code

    }

    override fun onItemLongClickListener(order: Order) {
        AlertDialog.Builder(context)
            .setTitle(R.string.cancel_title)
            .setMessage("${order.key} 是否要退票")
            .setNegativeButton(R.string.cancel_no, null)
            .setPositiveButton(R.string.cancel_yes) { _, _ ->
                val ref = myRef.root.child("ticketsStock")
                ref.child("transactions").child(order.ticket.username)
                    .child(order.key).setValue(null)

                ref.child("totalAmount")
                    .setValue(ticketsStock.totalAmount + order.ticket.allTickets)
            }
            .show()

    }
}
package com.study.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.study.app_ticket_firebase.models.TicketsStock
import com.study.app_ticket_firebase.services.TicketService
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("ticketsStock")
    lateinit var context: Context
    lateinit var username: String
    lateinit var ticketsStock: TicketsStock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        ticketsStock = TicketsStock(0.0, 0, 0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        username = intent.getStringExtra("username").toString()
        title = String.format(title.toString(), username)

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                    when (it.key.toString()) {
                        "discount" ->
                            ticketsStock.discount = it.value.toString().toDouble()
                        "price" ->
                            ticketsStock.price = it.value.toString().toInt()
                        "totalAmount" ->
                            ticketsStock.totalAmount = it.value.toString().toInt()
                    }
                }
                // Refresh UI
                refreshUI()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        // ??????????????????
        clear(null)
    }

    fun refreshUI() {
        tv_ticket_discount.text = ticketsStock.discount.toString()
        tv_ticket_price.text = ticketsStock.price.toString()
        tv_total_amount.text = ticketsStock.totalAmount.toString()
    }

    fun clear(view: View?) {
        // ?????????????????????
        ll_result.visibility = View.GONE
        val result = resources.getString(R.string.submit_detail_txt)
        tv_result.text = String.format(result, 0, 0, 0, 0)

        // ????????????????????????????????????
        et_all_tickets.setText("0")
        et_round_trip.setText("0")

        // ?????????????????????
        tv_warning.text = ""
        ll_warning.visibility = View.GONE
    }

    fun buyTicket(view: View) {
        ll_result.visibility = View.GONE
        ll_warning.visibility = View.GONE

        if (et_all_tickets.text.isEmpty())
            et_all_tickets.setText("0")

        if (et_round_trip.text.isEmpty())
            et_round_trip.setText("0")


        val allTickets = et_all_tickets.text.toString().toInt()
        val roundTrip = et_round_trip.text.toString().toInt()

        ll_warning.visibility = View.VISIBLE
        try {
            TicketService.errorMessages =
                resources.getStringArray(R.array.exception_message_array)

            // ????????????
            val ticket = TicketService().submit(ticketsStock, username, allTickets, roundTrip)

            // ??????????????????
            if (ticket != null) {
                ll_result.visibility = View.VISIBLE
                val result = resources.getString(R.string.submit_detail_txt)
                tv_result.text =
                    String.format(
                        result,
                        ticket.allTickets,
                        ticket.roundTrip,
                        ticket.oneWay,
                        ticket.total
                    )

                // ?????? firebase
                // 1. ?????? totalAmount ????????????
                val amount = ticketsStock.totalAmount - ticket.allTickets
                myRef.child("totalAmount").setValue(amount)

                // 2. ??????????????????
                val date = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss:SSS",
                    Locale.getDefault()
                ).format(Date())
                val order = myRef.child("transactions").child(username).child(date)
                order.child("allTickets").setValue(ticket.allTickets)
                order.child("oneWay").setValue(ticket.oneWay)
                order.child("roundTrip").setValue(ticket.roundTrip)
                order.child("total").setValue(ticket.total)

                // ??????????????????
                tv_warning.setText(R.string.result_success_txt)
            }
        } catch (e: Exception) {
            tv_warning.text = e.message
        }
    }

    fun viewOrderList(view: View) {
        val intent = Intent(context, OrderListActivity::class.java)
        intent.putExtra("username", username)

        startActivity(intent)
    }
}
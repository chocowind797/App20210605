package com.study.app_ticket_firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.encoder.QRCode
import com.study.app_ticket_firebase.adapter.OrderListAdapter
import com.study.app_ticket_firebase.models.Order
import com.study.app_ticket_firebase.models.Ticket
import com.study.app_ticket_firebase.models.TicketsStock
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.recyclerview_order_list.*
import kotlinx.android.synthetic.main.recyclerview_order_list_title.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderListActivity : AppCompatActivity() {
    val database = Firebase.database
    var myRef = database.getReference("ticketsStock/transactions")
    var isUser: Boolean = false
    lateinit var username: String
    lateinit var context: Context
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
            orderListAdapter =
                OrderListAdapter(if (!(username == "" || username == "null")) user_orderOnItemClickListener else admin_orderOnItemClickListener)
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
                orderListAdapter.orderList = orderList
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

    private val user_orderOnItemClickListener = object : OrderListAdapter.OrderOnItemClickListener {
        override fun onItemClickListener(order: Order) {
            // ?????? Json
            val orderJsonString = Gson().toJson(order)

            // ?????? QR-Code
            val writer = QRCodeWriter()
            // ?????? bit ??????(?????????)
            val bitMatrix = writer.encode(orderJsonString, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            // ?????? bitmap ????????????
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            // ??? bitMatrix ?????? bitmap ??????
            for (x in 0 until width)
                for (y in 0 until height)
                // ??????????????????, ??????????????????
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            // ??? view ????????? bitmap ??????
            val qrcodeImageView = ImageView(context)
            qrcodeImageView.setImageBitmap(bitmap)
            // ?????? AlertDialog ????????? bitmap ??????
            AlertDialog.Builder(context)
                .setView(qrcodeImageView)
                .create()
                .show()
        }

        override fun onItemLongClickListener(order: Order) {
            AlertDialog.Builder(context)
                .setTitle(R.string.cancel_title)
                .setMessage("${order.key} ???????????????")
                .setNegativeButton(R.string.cancel_no, null)
                .setPositiveButton(R.string.cancel_yes) { _, _ ->
                    val ref = myRef.root.child("ticketsStock")
                    ref.child("transactions").child(order.ticket.username)
                        .child(order.key).removeValue()

                    ref.child("totalAmount")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val totalAmount = snapshot.value.toString().toInt()
                                ref.child("totalAmount")
                                    .setValue(totalAmount + order.ticket.allTickets)
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                    // ????????????
                    // firebase "transactions_refund"
                    val path = "${order.ticket.username}/${order.key}"
                    val orderJsonString = Gson().toJson(order)

                    val ts =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    ref.child("transactions_refund").child(path).child("refund_date")
                        .setValue(ts)
                    ref.child("transactions_refund").child(path).child("buy_date")
                        .setValue(order.key)
                    ref.child("transactions_refund").child(path).child("ticket").child("allTickets")
                        .setValue(order.ticket.allTickets)
                    ref.child("transactions_refund").child(path).child("ticket").child("oneWay")
                        .setValue(order.ticket.oneWay)
                    ref.child("transactions_refund").child(path).child("ticket").child("roundTrip")
                        .setValue(order.ticket.roundTrip)
                    ref.child("transactions_refund").child(path).child("ticket").child("total")
                        .setValue(order.ticket.total)
                    ref.child("transactions_refund").child(path).child("json")
                        .setValue(orderJsonString.replace("\\", ""))
                }
                .create()
                .show()
        }
    }

    private val admin_orderOnItemClickListener =
        object : OrderListAdapter.OrderOnItemClickListener {
            override fun onItemClickListener(order: Order) {
                val result_text = Gson().toJson(order, Order::class.java)
                val message = "????????????:\t%s\n" +
                        "?????????:\t%s\n" +
                        "?????????:\t%d\n" +
                        "?????????:\t%d\n" +
                        "?????????:\t%d\n" +
                        "?????????:\t%,d\n"

                AlertDialog.Builder(context)
                    .setTitle("????????????")
                    .setMessage(
                        String.format(
                            message,
                            order.key,
                            order.ticket.username,
                            order.ticket.allTickets,
                            order.ticket.oneWay,
                            order.ticket.roundTrip,
                            order.ticket.total
                        )
                    )
                    .setNegativeButton("??????", null)
                    .setPositiveButton("??????") { _, _ ->
                        // ??????????????????
                        val path = "${order.ticket.username}/${order.key}"
                        val ticket_path = "transactions/$path"
                        // ????????????
                        myRef.child(ticket_path).removeValue()
                        // ?????? transactions_history
                        val ts = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                        ).format(Date())
                        myRef.child("transactions_history").child(path).child("use_date")
                            .setValue(ts)
                        myRef.child("transactions_history").child(path).child("buy_date")
                            .setValue(order.key)
                        myRef.child("transactions_history").child(path).child("ticket")
                            .child("allTickets")
                            .setValue(order.ticket.allTickets)
                        myRef.child("transactions_history").child(path).child("ticket")
                            .child("oneWay")
                            .setValue(order.ticket.oneWay)
                        myRef.child("transactions_history").child(path).child("ticket")
                            .child("roundTrip")
                            .setValue(order.ticket.roundTrip)
                        myRef.child("transactions_history").child(path).child("ticket")
                            .child("total")
                            .setValue(order.ticket.total)
                        myRef.child("transactions_history").child(path).child("json")
                            .setValue(result_text.replace("\\", ""))

                        Toast.makeText(context, "????????? ${order.key}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .create()
                    .show()
            }

            override fun onItemLongClickListener(order: Order) {
                AlertDialog.Builder(context)
                    .setTitle(R.string.cancel_title)
                    .setMessage("${order.key} ???????????????")
                    .setNegativeButton(R.string.cancel_no, null)
                    .setPositiveButton(R.string.cancel_yes) { _, _ ->
                        val ref = myRef.root.child("ticketsStock")
                        ref.child("transactions").child(order.ticket.username)
                            .child(order.key).removeValue()

                        ref.child("totalAmount")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val totalAmount = snapshot.value.toString().toInt()
                                    ref.child("totalAmount")
                                        .setValue(totalAmount + order.ticket.allTickets)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })

                        // ????????????
                        // firebase "transactions_refund"
                        val path = "${order.ticket.username}/${order.key}"
                        val orderJsonString = Gson().toJson(order)

                        val ts =
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.getDefault()
                            ).format(Date())
                        ref.child("transactions_refund").child(path).child("refund_date")
                            .setValue(ts)
                        ref.child("transactions_refund").child(path).child("buy_date")
                            .setValue(order.key)
                        ref.child("transactions_refund").child(path).child("ticket")
                            .child("allTickets")
                            .setValue(order.ticket.allTickets)
                        ref.child("transactions_refund").child(path).child("ticket").child("oneWay")
                            .setValue(order.ticket.oneWay)
                        ref.child("transactions_refund").child(path).child("ticket")
                            .child("roundTrip")
                            .setValue(order.ticket.roundTrip)
                        ref.child("transactions_refund").child(path).child("ticket").child("total")
                            .setValue(order.ticket.total)
                        ref.child("transactions_refund").child(path).child("json")
                            .setValue(orderJsonString.replace("\\", ""))
                    }
                    .create()
                    .show()
            }
        }

    fun sort(view: View) {
        var symbol = "???"
        clearTitle()

        if (view.tag == orderListAdapter.tag) {
            orderListAdapter.reverse *= -1
            symbol = if (orderListAdapter.reverse == 1)
                "???"
            else
                "???"
        } else {
            orderListAdapter.reverse = 1
        }

        Collections.sort(orderListAdapter.orderList, Comparator.comparing {
            val compare: Int = when (view.tag) {
                "username" -> it.ticket.username.hashCode()
                "allTickets" -> it.ticket.allTickets
                "roundTrip" -> it.ticket.roundTrip
                "oneWay" -> it.ticket.oneWay
                "total" -> it.ticket.total
                "key" -> it.key.hashCode()
                else -> it.key.hashCode()
            }
            compare * orderListAdapter.reverse
        })

        val view_txt = view as TextView
        view_txt.text = view_txt.text.toString() + symbol
        orderListAdapter.tag = view.tag.toString()
        orderListAdapter.notifyDataSetChanged()
    }

    private fun clearTitle() {
        tv_key_title.setText(R.string.submit_time)
        tv_username_title.setText(R.string.username)
        tv_allTickets_title.setText(R.string.all_ticket_amount)
        tv_roundTrip_title.setText(R.string.roundtrip_amount)
        tv_oneWay_title.setText(R.string.oneway_amount)
        tv_total_title.setText(R.string.price_amount)
    }
}
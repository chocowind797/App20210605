package com.study.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_console.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset

class ConsoleActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("ticketsStock")
    lateinit var context: Context
    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_console)

        context = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        username = intent.getStringExtra("username").toString()
        title = String.format(title.toString(), username)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 統計資料
                var sumAllTicket = 0
                var sumOneWay = 0
                var sumRoundTrip = 0
                var sumTotal = 0

                // 個別訂購人統計資料
                val statListByUser = mutableMapOf<String, Int>()

                snapshot.children.forEach {
                    when (it.key.toString()) {
                        resources.getString(R.string.fb_discount) -> et_discount.setText(it.value.toString())
                        resources.getString(R.string.fb_price) -> et_price.setText(it.value.toString())
                        resources.getString(R.string.fb_totalAmount) -> et_totalAmount.setText(it.value.toString())
                        "transactions" -> {
                            it.children.forEach { username -> // 訂購人
                                val mapUserName = username.key.toString()
                                statListByUser[mapUserName] = 0

                                username.children.forEach { date -> // 訂票日期
                                    date.children.forEach { detail -> // 訂票內容
                                        when (detail.key.toString()) {
                                            "allTickets" ->
                                                sumAllTicket += detail.value.toString().toInt()
                                            "oneWay" ->
                                                sumOneWay += detail.value.toString().toInt()
                                            "roundTrip" ->
                                                sumRoundTrip += detail.value.toString().toInt()
                                            "total" -> {
                                                val total = detail.value.toString().toInt()
                                                sumTotal += total
                                                statListByUser[mapUserName] = statListByUser[mapUserName]!! + total
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                tv_stat.text = String.format(
                    resources.getString(R.string.analytic_detail),
                    sumAllTicket,
                    sumOneWay,
                    sumRoundTrip,
                    sumRoundTrip / 2,
                    sumTotal
                )

                loadChart(statListByUser)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun update(view: View) {
        val type = listOf(
            resources.getString(R.string.fb_discount),
            resources.getString(R.string.fb_price),
            resources.getString(R.string.fb_totalAmount)
        )

        type.forEach {
            when (it) {
                resources.getString(R.string.fb_discount) ->
                    myRef.child(it).setValue(et_discount.text.toString().toDouble())
                resources.getString(R.string.fb_price) ->
                    myRef.child(it).setValue(et_price.text.toString().toDouble())
                resources.getString(R.string.fb_totalAmount) ->
                    myRef.child(it).setValue(et_totalAmount.text.toString().toDouble())
            }
        }
        Toast.makeText(
            context,
            resources.getString(R.string.update_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadChart(statListByUser: MutableMap<String, Int>) {
        // 將 [{"AAA", 10}, {"BBB", 20}]
        // 轉 "['AAA', 10], ['BBB', 20], "
        val regex = "['%s', %d], "
        var args: String = ""
        statListByUser.forEach {
            args += String.format(regex, it.key, it.value)
        }

        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true

        val asset_path = "file:///android_asset"
        var html = getHtml("chart.html")

        html = String.format(html!!, args)

        web_view.loadDataWithBaseURL(asset_path, html, "text/html", "UTF-8", "null")
        web_view.requestFocusFromTouch()
    }

    private fun getHtml(filename: String): String? {
        var html: String? = null

        try {
            val inputStream: InputStream = assets.open(filename)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            outputStream.write(buffer)
            html = String(outputStream.toByteArray(), Charset.forName("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return html
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // group_id, item_id. order_id(越小越上面), name
        menu?.add(1,1 , 1, "[ - ]")?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menu?.add(1,2 , 2, "購票紀錄")
        menu?.add(1, 9, 5, "返回")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> startActivity(Intent(this, QRCodeActivity::class.java))
            2 -> startActivity(Intent(this, OrderListActivity::class.java))
            9 -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
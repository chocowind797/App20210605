package com.study.app_ticket_firebase

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.study.app_ticket_firebase.models.Order
import kotlinx.android.synthetic.main.activity_qrcode.*
import java.text.SimpleDateFormat
import java.util.*

class QRCodeActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var codeScanner: CodeScanner
    private val database = Firebase.database
    private val myRef = database.getReference("ticketsStock")
    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        context = this

        if (checkPermission()) {
            runProgram()
        } else {
            // 啟動動態核准對話框
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    fun runProgram() {
        title = "執行 QR-code 程式"
        codeScanner = CodeScanner(context, scanner_view)

        // 設定 codescanner 參數
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE  // SINGLE -> 找到即停止
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = true

        // 解碼
        codeScanner.setDecodeCallback {
            runOnUiThread {
                val result_text = it.text
                AlertDialog.Builder(context)
                    .setTitle("QR-code 內容")
                    .setMessage(result_text)
                    .setNegativeButton("取消") { _, _ ->
                        finish()
                    }
                    .setPositiveButton("使用") { _, _ ->
                        val order = Gson().fromJson(result_text, Order::class.java)
                        // 取得該票路徑
                        val path = "${order.ticket.username}/${order.key}"
                        val ticket_path = "transactions/$path"
                        // 刪除該票
                        myRef.child(ticket_path).removeValue()
                        // 建立 transactions_history
                        val ts = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                        myRef.child("transactions_history").child(path).child("use_date")
                            .setValue(ts)
                        myRef.child("transactions_history").child(path).child("buy_date")
                            .setValue(order.key)
                        myRef.child("transactions_history").child(path).child("ticket").child("allTickets")
                            .setValue(order.ticket.allTickets)
                        myRef.child("transactions_history").child(path).child("ticket").child("oneWay")
                            .setValue(order.ticket.oneWay)
                        myRef.child("transactions_history").child(path).child("ticket").child("roundTrip")
                            .setValue(order.ticket.roundTrip)
                        myRef.child("transactions_history").child(path).child("ticket").child("total")
                            .setValue(order.ticket.total)
                        myRef.child("transactions_history").child(path).child("json")
                            .setValue(result_text.replace("\\", ""))

                        Toast.makeText(context, "已使用 ${order.key}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .create()
                    .show()
            }
        }

        codeScanner.setErrorCallback {
            runOnUiThread {
                runOnUiThread {
                    val message_text = it.message
                    AlertDialog.Builder(context)
                        .setTitle("錯誤內容")
                        .setMessage(message_text)
                        .setPositiveButton("關閉") { _, _ ->
                            finish()
                        }
                        .create()
                        .show()
                }
            }
        }

        // 執行
        codeScanner.startPreview()
    }

    override fun onStop() {
        super.onStop()
        try {
            codeScanner.stopPreview()
        } catch (e: Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE)
            if (grantResults[0] == 0)
                runProgram()
            else
                finish()
    }

    // 使用者是否有同意使用權限
    private fun checkPermission(): Boolean {
        val check = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return check == PackageManager.PERMISSION_GRANTED
    }
}
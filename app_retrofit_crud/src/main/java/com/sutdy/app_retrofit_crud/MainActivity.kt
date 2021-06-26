package com.sutdy.app_retrofit_crud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sutdy.app_retrofit_crud.manager.EmployeeManager
import com.sutdy.app_retrofit_crud.model.Employee
import com.sutdy.app_retrofit_crud.service.EmployeeApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {
    lateinit var recyclerAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerAdapter

            val divider = DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        GlobalScope.launch {
            val api = EmployeeManager.instance.api
            val employees: List<Employee>? = api.getAllEmployees().execute().body()
            runOnUiThread {
                title = "員工筆數: ${employees!!.size}"
                recyclerAdapter.setListData(employees)
                recyclerAdapter.notifyDataSetChanged()
            }
        }

        btn_update.setOnClickListener {
            GlobalScope.launch {
                val api = EmployeeManager.instance.api

                val id = ed_salary.getTag(R.id.emp_id).toString().toInt()
                val name = ed_salary.getTag(R.id.emp_name).toString()
                val salary = ed_salary.text.toString().toInt()
                val employee = api.getEmployee(id).execute().body()

                if(employee != null) {
                    employee.salary.basic = salary
                    if(api.patchSalary(id, employee).execute().isSuccessful) {
                        val employees = api.getAllEmployees().execute().body()
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "成功修改 $name 的薪資", Toast.LENGTH_SHORT).show()
                            title = "員工筆數: ${employees!!.size}"
                            recyclerAdapter.setListData(employees)
                            recyclerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onItemClickListener(employee: Employee) {
        ed_salary.setTag(R.id.emp_id, employee.id)
        ed_salary.setTag(R.id.emp_name, employee.name)
        ed_salary.setText(employee.salary.basic.toString())
    }
}
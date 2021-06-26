package com.sutdy.app_retrofit_crud.manager

import com.sutdy.app_retrofit_crud.service.EmployeeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmployeeManager {
    private val employeeApi: EmployeeApi
    val api: EmployeeApi
        get() = employeeApi

    companion object {
        val instance = EmployeeManager()
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        employeeApi = retrofit.create(EmployeeApi::class.java)
    }
}
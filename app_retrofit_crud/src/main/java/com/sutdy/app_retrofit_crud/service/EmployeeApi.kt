package com.sutdy.app_retrofit_crud.service

import com.sutdy.app_retrofit_crud.model.Employee
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface EmployeeApi {
    @GET("/employees")
    fun getAllEmployees(): Call<List<Employee>>

    @GET("/employees/{id}")
    fun getEmployee(@Path("id") id: Int): Call<Employee>

    @PATCH("/employees/{id}")
    fun patchSalary(@Path("id") id: Int, @Body body: Employee): Call<Employee>
}
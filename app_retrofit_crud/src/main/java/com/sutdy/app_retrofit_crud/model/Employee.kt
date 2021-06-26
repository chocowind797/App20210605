package com.sutdy.app_retrofit_crud.model

data class Employee(
    var email: String,
    var id: Int,
    var name: String,
    var phone: String,
    var salary: Salary
)
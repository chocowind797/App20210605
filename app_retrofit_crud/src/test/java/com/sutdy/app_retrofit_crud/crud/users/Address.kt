package com.sutdy.app_retrofit_crud.crud.users

import com.sutdy.app_retrofit_crud.crud.users.Geo

data class Address(
    var city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)
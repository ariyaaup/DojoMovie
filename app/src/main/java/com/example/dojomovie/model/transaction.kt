package com.example.dojomovie.model

data class transaction(
    val transaction_id :Int,
    val user_id: String,
    val movie_id: String,
    val quantity: Int
)

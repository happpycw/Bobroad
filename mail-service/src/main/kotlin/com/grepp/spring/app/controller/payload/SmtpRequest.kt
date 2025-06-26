package com.grepp.spring.app.controller.payload

data class SmtpRequest(
    val toEmail: String,
    val tempPassword: String
){
}
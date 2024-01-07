package com.harshit.fixwheels.model
data class MyCartModel(
    var productName: String? = null,
    var productPrice: String? = null,
    var currentDate: String? = null,
    var currentTime: String? = null,
    var totalQuantity: String? = null,
    var totalPrice: Long = 0,
    var documentId: String? = null
)

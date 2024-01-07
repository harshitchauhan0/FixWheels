package com.harshit.fixwheels.model

import com.google.firebase.Timestamp

data class BookingModel(
    var imageUri: String? = "",
    var address: String? = "",
    var timestamp: Timestamp? = null,
    var description: String? = "",
    var customerId:String? = "",
    var garageId:String? = ""
)
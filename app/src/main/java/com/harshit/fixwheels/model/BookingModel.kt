package com.harshit.fixwheels.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class BookingModel(
    var imageUri: String? = "",
    var latitude: String? = "",
    var longitude:String? = "",
    var timestamp: Timestamp? = null,
    var description: String? = "",
    var customerId:String? = "",
    var vehicle:String? = "",
    var garageId:String? = "",
    var userName:String? = ""
):Serializable
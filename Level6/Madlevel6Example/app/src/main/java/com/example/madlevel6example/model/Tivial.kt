package com.example.madlevel6example.model

import com.google.gson.annotations.SerializedName

class Trivial (@SerializedName("text") var text: String, @SerializedName("number") var number: Int,
                @SerializedName("found") var found:Boolean, @SerializedName("type") var type:String)



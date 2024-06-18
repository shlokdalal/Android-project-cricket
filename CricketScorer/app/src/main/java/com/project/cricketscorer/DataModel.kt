package com.project.cricketscorer

import com.google.gson.annotations.SerializedName

data class DataModel (

    @SerializedName("ballResult"          ) var ballResult          : String? = null,
    @SerializedName("ballType"            ) var ballType            : String? = null,
    @SerializedName("bowlerId"            ) var bowlerId            : String? = null,
    @SerializedName("dismissedPlayerId"   ) var dismissedPlayerId   : String? = null,
    @SerializedName("extraRuns"           ) var extraRuns           : Int?    = null,
    @SerializedName("extraType"           ) var extraType           : String? = null,
    @SerializedName("helperPlayerId"      ) var helperPlayerId      : String? = null,
    @SerializedName("illegalDeliveryRuns" ) var illegalDeliveryRuns : Int?    = null,
    @SerializedName("newPlayerId"         ) var newPlayerId         : String? = null,
    @SerializedName("nonStrikerId"        ) var nonStrikerId        : String? = null,
    @SerializedName("strikerId"           ) var strikerId           : String? = null,
    @SerializedName("wicketType"          ) var wicketType          : String? = null

)
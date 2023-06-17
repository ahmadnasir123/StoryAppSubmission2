package com.sirdev.storyappsubmissionakhir.data.response

import com.google.gson.annotations.SerializedName

data class GeneralResponseError(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

package ced.cedie.cedrick.jujump.domain.model

import androidx.annotation.Keep

@Keep
data class JumpRequest(
    val androidname: String,
    val apistatus: String,
    val domainswitch: Int,
    val retryDomain: Int
)
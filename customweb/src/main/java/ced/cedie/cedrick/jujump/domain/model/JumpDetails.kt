package ced.cedie.cedrick.jujump.domain.model

import androidx.annotation.Keep

@Keep
data class JumpDetails(
    var version: Int,
    val wangzhi: String,
    val drainage: String,
)

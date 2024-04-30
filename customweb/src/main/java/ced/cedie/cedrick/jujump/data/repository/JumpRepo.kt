package ced.cedie.cedrick.jujump.data.repository

import ced.cedie.cedrick.jujump.domain.dto.ResponseJump
import ced.cedie.cedrick.jujump.domain.model.JumpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface JumpRepo {

    @POST("details")
    suspend fun startRequest(@Body param: JumpRequest): Response<ResponseJump>

}
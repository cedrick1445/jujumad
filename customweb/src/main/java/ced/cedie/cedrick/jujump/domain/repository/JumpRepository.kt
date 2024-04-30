package ced.cedie.cedrick.jujump.domain.repository

import ced.cedie.cedrick.jujump.domain.model.JumpRequest
import retrofit2.Response

interface JumpRepository {
    suspend fun startRequest(param: JumpRequest): Response<ced.cedie.cedrick.jujump.domain.dto.ResponseJump>
}
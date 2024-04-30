package ced.cedie.cedrick.jujump.domain.use_case

import ced.cedie.cedrick.jujump.data.repository.JumpRepoImp
import ced.cedie.cedrick.jujump.domain.model.JumpRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetJumpUrl {
    private val repo = JumpRepoImp()

    suspend operator fun invoke(param: JumpRequest) = repo.startRequest(param)
}
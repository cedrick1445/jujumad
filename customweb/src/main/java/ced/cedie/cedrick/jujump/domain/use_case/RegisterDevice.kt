package ced.cedie.cedrick.jujump.domain.use_case

import ced.cedie.cedrick.jujump.data.repository.JumpRepoImp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RegisterDevice {
    private val repo = JumpRepoImp()

    suspend operator fun invoke(param: ced.cedie.cedrick.jujump.domain.model.JumpRequest) = repo.startRequest(param)
}
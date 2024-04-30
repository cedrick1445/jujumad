package ced.cedie.cedrick.jujump.data.repository


import ced.cedie.cedrick.jujump.presentation.helper.RetrofitHelper
import ced.cedie.cedrick.jujump.presentation.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class JumpRepoImp : ced.cedie.cedrick.jujump.domain.repository.JumpRepository {

    val service = RetrofitHelper.retrofitService(Constants.BASE_URL)

    override suspend fun startRequest(param: ced.cedie.cedrick.jujump.domain.model.JumpRequest) = service.startRequest(param)

}
package ced.cedie.cedrick.jujump.presentation.ui.jump

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ced.cedie.cedrick.jujump.domain.use_case.GetJumpUrl
import ced.cedie.cedrick.jujump.domain.use_case.RegisterDevice
import ced.cedie.cedrick.jujump.presentation.utils.writeLogs
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.IOException

@ExperimentalCoroutinesApi
class JumpActivityViewModel : ViewModel() {

    private val getJumpUrl = GetJumpUrl()
    private val registerDevice = RegisterDevice()

    private var _jumpEvent = MutableLiveData<JumpEvent>()
    val jumpEvent: LiveData<JumpEvent> get() = _jumpEvent

    fun startRequest(id: String, domainSwitch: Int, retryDomain: Int) {
        val param = ced.cedie.cedrick.jujump.domain.model.JumpRequest(
            id,
            RequestType.INSTALL.type,
            domainSwitch,
            retryDomain
        )

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _jumpEvent.postValue(JumpEvent.JumpRequestError(throwable as Exception, RequestType.INSTALL))
        }){
            val data = registerDevice(param)
            writeLogs(Gson().toJson(data.body()))
            if(!data.isSuccessful) _jumpEvent.value = JumpEvent.JumpRequestError(IOException(),RequestType.INSTALL)
            else _jumpEvent.value = JumpEvent.AppInstalledEvent(data.body()?.httpCode == 200)
        }
    }

    fun getApplicationUrl(id: String, domainSwitch: Int, retryDomain: Int) {
        val param = ced.cedie.cedrick.jujump.domain.model.JumpRequest(
            id,
            RequestType.ANDROID_API.type,
            domainSwitch,
            retryDomain
        )
       viewModelScope.launch (CoroutineExceptionHandler { _, throwable ->
           _jumpEvent.postValue(JumpEvent.JumpRequestError(throwable as Exception, RequestType.ANDROID_API))
       }) {
           try{
               val data = getJumpUrl(param)
               writeLogs(Gson().toJson(data.body()))
               if(!data.isSuccessful) _jumpEvent.value = JumpEvent.JumpRequestError(IOException(),RequestType.ANDROID_API)
               else _jumpEvent.value = JumpEvent.JumpRequestSuccess(data.body()?.response?.get(0)!!)
           }catch (e: Exception){
               _jumpEvent.value = JumpEvent.JumpRequestError(e,RequestType.ANDROID_API)
           }
       }
    }
}
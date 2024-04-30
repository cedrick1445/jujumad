package ced.cedie.cedrick.jujump.presentation.ui.jump

import ced.cedie.cedrick.jujump.domain.dto.Response

sealed class JumpEvent {
    data class Loading(val isLoading: Boolean): JumpEvent()
    data class AppInstalledEvent(val isInstalled: Boolean): JumpEvent()
    data class JumpRequestSuccess(val list: Response): JumpEvent()
    data class JumpRequestError(val exception: Exception, val requestType: RequestType): JumpEvent()
}

enum class RequestType(val type: String){
    INSTALL("install"),
    ANDROID_API("androidAPI")
}

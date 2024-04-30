package ced.cedie.cedrick.jujump.presentation.ui.jump.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ced.cedie.cedrick.jujump.databinding.ActivityWebViewBinding
import ced.cedie.cedrick.jujump.presentation.utils.Constants
import kotlin.system.exitProcess

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityWebViewBinding.inflate(inflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(URL)
//        binding.webView.loadUrl(url ?: "")
        binding.webView.loadUrl(Constants.url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    Toast.makeText(applicationContext, "Press again to exit the program", Toast.LENGTH_SHORT).show()
                    exitTime = System.currentTimeMillis()
                } else {
                    finish()
                    exitProcess(0)
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object{
        var exitTime: Long = 0
        const val URL = "url"

        fun createIntent(context: Context, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(URL, url)
            }
        }
    }
}

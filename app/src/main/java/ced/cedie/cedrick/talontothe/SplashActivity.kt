package ced.cedie.cedrick.talontothe

import android.annotation.SuppressLint
import android.os.Bundle
import ced.cedie.cedrick.jujump.presentation.ui.jump.JumpActivity
import ced.cedie.cedrick.jujump.presentation.ui.jump.JumpType
import ced.cedie.cedrick.jujump.presentation.utils.writeLogs

import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("CustomSplashScreen")
@ExperimentalCoroutinesApi
class SplashActivity : JumpActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashAction(JumpType.JUMP_TESTING ) { _, downUrl ->
            writeLogs("URL : $downUrl")
            startActivity(MainActivity.createIntent(this@SplashActivity))
        }
    }
}
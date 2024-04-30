package ced.cedie.cedrick.jujump.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ced.cedie.cedrick.jujump.R

class NoNetworkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_network)
        setTheme(R.style.Theme_JumpCode)
    }

    companion object{
        fun createIntent(context: Context): Intent = Intent(context, NoNetworkActivity::class.java)
    }
}
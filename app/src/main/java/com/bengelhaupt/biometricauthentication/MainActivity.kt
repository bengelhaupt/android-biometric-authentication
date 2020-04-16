package com.bengelhaupt.biometricauthentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bengelhaupt.biometricauthentication.direct.DirectActivity
import com.bengelhaupt.biometricauthentication.zeroknowledge.fingerprint.ZeroKnowledgeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        direct.setOnClickListener {
            startActivity(
                Intent(this, DirectActivity::class.java)
            )
        }

        zeroknowledge.setOnClickListener {
            startActivity(
                Intent(this, ZeroKnowledgeActivity::class.java)
            )
        }
    }
}

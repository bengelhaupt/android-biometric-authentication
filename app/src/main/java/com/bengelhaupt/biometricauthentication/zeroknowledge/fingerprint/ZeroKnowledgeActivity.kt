package com.bengelhaupt.biometricauthentication.zeroknowledge.fingerprint

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bengelhaupt.biometricauthentication.R
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Authenticator
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Dependant
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Medium
import kotlinx.android.synthetic.main.activity_console.*
import kotlin.random.Random

class ZeroKnowledgeActivity : AppCompatActivity(), Authenticator<FingerprintBiometric> {

    private val medium = KeyStoreFingerprintMedium()
    private val biometric = FingerprintBiometric("FINGERPRINT", Random.nextBytes(128), this)
    private val dependant = SampleDependant()

    override fun challenge(sender: Dependant, challenge: ByteArray) {
        medium.solveAndEncrypt(this, challenge, biometric)
    }

    override fun solution(sender: Medium<FingerprintBiometric>, solution: ByteArray) {
        dependant.submit(this, medium, solution)
    }

    override fun success(match: Float) {
        print(match.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_console)

        enroll.setOnClickListener {
            medium.enroll(this, biometric)
        }

        authenticate.setOnClickListener {
            dependant.request(this)
        }

        fake.visibility = View.GONE
    }

    private fun print(string: String) {
        runOnUiThread {
            console.append(string)
            console.append("\n")
            scroll.fullScroll(View.FOCUS_DOWN)
        }
    }
}

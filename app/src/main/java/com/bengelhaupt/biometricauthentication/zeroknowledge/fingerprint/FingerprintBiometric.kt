package com.bengelhaupt.biometricauthentication.zeroknowledge.fingerprint

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Biometric
import java.util.concurrent.Executors

class FingerprintBiometric(override val identifier: String, override val features: ByteArray, private val activity: FragmentActivity) :
    Biometric<FingerprintBiometric> {

    override fun compare(other: FingerprintBiometric?, callback: (Float) -> Unit) {
        if (other == null) {
            callback(-1f)
        }

        BiometricPrompt(
            activity,
            Executors.newSingleThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    callback(1f)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback(0f)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    callback(-1f)
                }
            }
        ).authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Please authenticate")
                .setNegativeButtonText("Abort")
                .build()
        )
    }
}
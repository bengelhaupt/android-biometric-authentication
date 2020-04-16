package com.bengelhaupt.biometricauthentication.direct.fingerprint

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.bengelhaupt.biometricauthentication.direct.model.Authenticator
import com.bengelhaupt.biometricauthentication.direct.util.KeyStoreHelper
import java.util.concurrent.Executors

class FingerprintAuthenticator : Authenticator<FingerprintBiometric> {

    override fun solve(
        challenge: ByteArray,
        fingerprintBiometric: FingerprintBiometric,
        activity: FragmentActivity,
        callback: (ByteArray?) -> Unit
    ) {
        val privateKey = try {
            KeyStoreHelper.getPrivateKey(fingerprintBiometric.identifier)!!
        } catch (e: Exception) {
            Toast.makeText(activity, "Biometric not enrolled", Toast.LENGTH_SHORT).show()
            return
        }

        val signature = KeyStoreHelper.getSignatureInstance()
        signature.initSign(privateKey)

        val prompt = BiometricPrompt(
            activity,
            Executors.newSingleThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    callback.invoke(result.cryptoObject?.signature?.let {
                        it.update(challenge)
                        it.sign()
                    })
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.invoke(null)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.invoke(null)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Please authenticate")
            .setNegativeButtonText("Abort")
            .build()

        prompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(signature)
        )
    }
}
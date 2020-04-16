package com.bengelhaupt.biometricauthentication.direct

import androidx.fragment.app.FragmentActivity
import com.bengelhaupt.biometricauthentication.direct.fingerprint.FingerprintAuthenticator
import com.bengelhaupt.biometricauthentication.direct.fingerprint.FingerprintBiometric
import com.bengelhaupt.biometricauthentication.direct.fingerprint.SampleDependant
import com.bengelhaupt.biometricauthentication.direct.util.KeyStoreHelper
import java.util.*

class Interaction(private val activity: FragmentActivity, private val print: (String) -> Unit) {

    private val dependant = SampleDependant()
    private val authenticator = FingerprintAuthenticator()

    private val biometric = FingerprintBiometric(System.currentTimeMillis().toString())

    fun start() {
        print("Biometric is ${biometric.identifier}")
        print("")
    }

    fun enroll() {
        print("Enrollment started")
        if (KeyStoreHelper.getPublicKey(biometric.identifier) == null) {
            print("Enrollment does not exist")

            KeyStoreHelper.generateKeyPair(biometric.identifier)
            print("Key pair generated")

            dependant.enroll(
                biometric.identifier,
                KeyStoreHelper.getPublicKey(biometric.identifier)!!
            )
            print("Enrollment complete: ${biometric.identifier}")
        } else {
            print("Enrollment already exists. Aborting.")
        }
        print("")
    }

    fun authenticate() {
        print("Authentication started")

        val challenge = dependant.challenge(biometric.identifier)
        print(
            "Challenge created: ${Base64.getEncoder().encode(challenge).toString(Charsets.UTF_8)
                .subSequence(0, 10)}"
        )

        print("Trying to solve challenge")
        authenticator.solve(challenge, biometric, activity) {
            if (it != null) {
                print(
                    "Challenge solution: ${Base64.getEncoder().encode(it)
                        .toString(Charsets.UTF_8).subSequence(0, 10)}"
                )
                val success = dependant.authenticate(challenge, it)
                print("Solution is correct: $success")
            } else {
                print("An error occured")
            }
            print("")
        }
    }

    fun fake() {
        try {
            val challenge = dependant.challenge(biometric.identifier)
            val signature = KeyStoreHelper.getSignatureInstance()
            signature.initSign(KeyStoreHelper.getPrivateKey(biometric.identifier))
            signature.update(challenge)
            val sign = signature.sign()
            val success = dependant.authenticate(challenge, sign)
            print("Solution is correct: $success")
        } catch (e: Exception) {
            print(e.toString())
        }
    }
}

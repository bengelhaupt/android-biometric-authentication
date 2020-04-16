package com.bengelhaupt.biometricauthentication.direct.fingerprint

import com.bengelhaupt.biometricauthentication.direct.model.Dependant
import com.bengelhaupt.biometricauthentication.direct.util.KeyStoreHelper
import java.security.PublicKey
import kotlin.random.Random

class SampleDependant : Dependant {

    private val enrolledBiometrics: MutableMap<String, PublicKey> = mutableMapOf()

    private val challenges: MutableMap<ByteArray, String> = mutableMapOf()

    override fun enroll(biometricName: String, public: PublicKey) {
        enrolledBiometrics[biometricName] = public
    }

    override fun authenticate(challenge: ByteArray, solution: ByteArray): Boolean {
        val signature = KeyStoreHelper.getSignatureInstance()
        signature.initVerify(enrolledBiometrics[challenges[challenge]])
        signature.update(challenge)
        val success = signature.verify(solution)
        if (success) {
            challenges.remove(challenge)
        }
        return success
    }

    override fun challenge(biometricName: String): ByteArray {
        val data = Random.nextBytes(512)
        challenges[data] = biometricName
        return data
    }
}
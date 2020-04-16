package com.bengelhaupt.biometricauthentication.zeroknowledge.fingerprint

import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Authenticator
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Medium
import java.nio.ByteBuffer
import java.security.KeyPairGenerator
import java.security.PublicKey
import javax.crypto.Cipher

class KeyStoreFingerprintMedium : Medium<FingerprintBiometric> {

    companion object {
        private const val ALIAS = "MEDIUM"
        private const val KEYSTORE = "AndroidKeyStore"
    }

    private val keyPair = KeyPairGenerator.getInstance("RSA")
        .let {
            it.initialize(1024)
            it.generateKeyPair()
        }

    private val enrolledBiometrics: MutableMap<Authenticator<FingerprintBiometric>, FingerprintBiometric> = mutableMapOf()

    override fun enroll(sender: Authenticator<FingerprintBiometric>, biometric: FingerprintBiometric) {
        enrolledBiometrics[sender] = biometric
    }

    override fun solveAndEncrypt(sender: Authenticator<FingerprintBiometric>, challenge: ByteArray, biometric: FingerprintBiometric) {
        biometric.compare(enrolledBiometrics[sender]) {
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.private)
            val solution = cipher.doFinal(
                ByteBuffer.allocate(challenge.size + 4).putFloat(it).put(challenge).array()
            )
            sender.solution(this, solution)
        }
    }

    override val public: PublicKey = keyPair.public
}
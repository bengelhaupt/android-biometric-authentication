package com.bengelhaupt.biometricauthentication.zeroknowledge.fingerprint

import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Authenticator
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Dependant
import com.bengelhaupt.biometricauthentication.zeroknowledge.model.Medium
import java.nio.ByteBuffer
import javax.crypto.Cipher
import kotlin.random.Random

class SampleDependant : Dependant {

    private val openRequests: MutableMap<Authenticator<*>, ByteArray> = mutableMapOf()

    override fun request(sender: Authenticator<*>) {
        val challenge = Random.nextBytes(32)
        openRequests[sender] = challenge
        sender.challenge(this, challenge)
    }

    override fun submit(sender: Authenticator<*>, medium: Medium<*>, solution: ByteArray) {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, medium.public)
        try {
            val decrypt = cipher.doFinal(solution)
            val match = ByteBuffer.wrap(decrypt).float
            val challenge = decrypt.drop(4).toByteArray()
            if (openRequests[sender]?.contentEquals(challenge) == true) {
                sender.success(match)
            } else {
                sender.success(-1f)
            }
        } catch (e: Exception) {
            sender.success(-2f)
        }
    }
}
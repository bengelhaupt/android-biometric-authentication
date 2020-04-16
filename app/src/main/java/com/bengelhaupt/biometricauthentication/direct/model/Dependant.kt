package com.bengelhaupt.biometricauthentication.direct.model

import java.security.PublicKey

interface Dependant {

    fun enroll(biometricName: String, public: PublicKey)

    fun authenticate(challenge: ByteArray, solution: ByteArray): Boolean

    fun challenge(biometricName: String): ByteArray
}
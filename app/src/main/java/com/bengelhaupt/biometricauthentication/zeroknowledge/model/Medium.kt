package com.bengelhaupt.biometricauthentication.zeroknowledge.model

import java.security.PublicKey

interface Medium<T : Biometric<*>> {

    val public: PublicKey

    fun enroll(sender: Authenticator<T>, biometric: T)

    fun solveAndEncrypt(sender: Authenticator<T>, challenge: ByteArray, biometric: T)
}
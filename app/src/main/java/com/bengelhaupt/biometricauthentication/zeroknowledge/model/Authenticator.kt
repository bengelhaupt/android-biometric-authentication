package com.bengelhaupt.biometricauthentication.zeroknowledge.model

interface Authenticator<T : Biometric<*>> {

    fun challenge(sender: Dependant, challenge: ByteArray)

    fun solution(sender: Medium<T>, solution: ByteArray)

    fun success(match: Float)
}
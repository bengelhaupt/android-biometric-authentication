package com.bengelhaupt.biometricauthentication.zeroknowledge.model

interface Biometric<T : Biometric<T>> {

    val identifier: String

    val features: ByteArray

    fun compare(other: T?, callback: (Float) -> Unit)
}
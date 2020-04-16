package com.bengelhaupt.biometricauthentication.zeroknowledge.model

interface Dependant {

    fun request(sender: Authenticator<*>)

    fun submit(sender: Authenticator<*>, medium: Medium<*>, solution: ByteArray)
}
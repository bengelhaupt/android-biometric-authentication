package com.bengelhaupt.biometricauthentication.direct.model

import androidx.fragment.app.FragmentActivity

interface Authenticator<T : Biometric> {

    fun solve(
        challenge: ByteArray,
        biometric: T,
        activity: FragmentActivity,
        callback: ((ByteArray?) -> Unit)
    )
}
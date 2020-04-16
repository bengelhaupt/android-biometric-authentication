package com.bengelhaupt.biometricauthentication.direct.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*

object KeyStoreHelper {

    private const val KEYSTORE = "AndroidKeyStore"

    fun getSignatureInstance(): Signature {
        return Signature.getInstance("SHA256WITHRSA", "${KEYSTORE}BCWorkaround")
    }

    fun generateKeyPair(alias: String) {
        KeyPairGenerator.getInstance(
            "RSA",
            KEYSTORE
        )
            .apply {
                initialize(
                    KeyGenParameterSpec.Builder(
                        alias,
                        KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                    )
                        .setKeySize(512)
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                        .setUserAuthenticationRequired(true)
                        .setInvalidatedByBiometricEnrollment(true)
                        .build()
                )
                generateKeyPair()
            }
    }

    fun getPublicKey(alias: String): PublicKey? {
        return KeyStore.getInstance(KEYSTORE)
            .apply {
                load(null)
            }
            .getCertificate(alias)?.publicKey
    }

    fun getPrivateKey(alias: String): PrivateKey? {
        return KeyStore.getInstance(KEYSTORE)
            .apply {
                load(null)
            }
            .getKey(alias, null) as PrivateKey?
    }
}
package com.example.expens_tracker.Data

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat

object SecurityManager {
    fun showBiometricPrompt(
        activity: FragmentActivity,
        onSuccess: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess() // Run this code if they pass!
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Secure Access")
            .setSubtitle("Confirm your identity to continue")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
package com.example.expens_tracker.Data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Create DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsProvider(private val context: Context) {

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val MONTHLY_BUDGET = doublePreferencesKey("monthly_budget")
        val DAILY_LIMIT = doublePreferencesKey("daily_limit")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }

    // -----------------------------
    // READ VALUES (Flows)
    // -----------------------------

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[DARK_MODE] ?: false }

    val monthlyBudgetFlow: Flow<Double> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[MONTHLY_BUDGET] ?: 2000.0 }

    val dailyLimitFlow: Flow<Double> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[DAILY_LIMIT] ?: 50.0 }

    val biometricFlow: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[BIOMETRIC_ENABLED] ?: false }

    // -----------------------------
    // WRITE VALUES (Suspend Functions)
    // -----------------------------

    suspend fun toggleDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE] = enabled
        }
    }

    suspend fun saveMonthlyBudget(amount: Double) {
        context.dataStore.edit { prefs ->
            prefs[MONTHLY_BUDGET] = amount
        }
    }

    suspend fun saveDailyLimit(amount: Double) {
        context.dataStore.edit { prefs ->
            prefs[DAILY_LIMIT] = amount
        }
    }

    suspend fun saveBiometricSetting(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[BIOMETRIC_ENABLED] = enabled
        }
    }
}

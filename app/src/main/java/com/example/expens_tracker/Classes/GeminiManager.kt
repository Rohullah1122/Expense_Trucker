package com.example.expens_tracker.Classes

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

object GeminiManager {
    // Replace with your actual API key from Google AI Studio
    private const val API_KEY = "YOUR_GEMINI_API_KEY"

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = API_KEY
    )

    suspend fun getFinancialAdvice(analysis: FinancialAI.AIAnalysis): String {
        val prompt = """
            You are a helpful Canadian financial coach. 
            User Status:
            - Health Score: ${analysis.healthScore}%
            - Top Spending: ${analysis.topCategory}
            - Potential Savings: $${analysis.savingPotential}
            
            Give one short, punchy tip (max 2 sentences). 
            Be encouraging but direct. Use Canadian slang like 'loonies' or 'toonies' if it fits.
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            response.text ?: "I'm processing your data, check back in a bit!"
        } catch (e: Exception) {
            // This catches if you hit your 20-use daily limit
            "AI Coach is resting for today. Let's save some loonies and try again tomorrow!"
        }
    }
}
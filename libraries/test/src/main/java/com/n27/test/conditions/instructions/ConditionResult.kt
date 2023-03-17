package com.n27.test.conditions.instructions

sealed class ConditionResult {

    object Met : ConditionResult()
    data class NotMet(val error: Throwable) : ConditionResult()
}

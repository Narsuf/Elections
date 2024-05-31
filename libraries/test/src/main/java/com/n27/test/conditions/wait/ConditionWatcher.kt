package com.n27.test.conditions.wait

import com.n27.test.conditions.instructions.ConditionResult.Met
import com.n27.test.conditions.instructions.ConditionResult.NotMet
import com.n27.test.conditions.instructions.Instruction
import java.util.concurrent.TimeUnit

object ConditionWatcher {

    private const val CONDITION_NOT_MET = 0
    private const val CONDITION_MET = 1
    private const val CONDITION_TIMED_OUT = 2
    private const val DEFAULT_TIMEOUT_SECONDS = 10L // Seconds timeout by default.
    private const val WATCH_INTERVAL_MS = 100 // Check every 100ms

    @Suppress("LongMethod")
    @Throws(IllegalStateException::class)
    fun waitForCondition(
        tag: String,
        instruction: Instruction,
        timeoutLimitMs: Long = TimeUnit.SECONDS.toMillis(DEFAULT_TIMEOUT_SECONDS)
    ) {
        var status = CONDITION_NOT_MET
        var elapsedTime = 0
        var error: Throwable? = null

        do {
            when (val conditionResult = instruction.checkCondition()) {
                is Met -> status = CONDITION_MET
                is NotMet -> {
                    error = conditionResult.error
                    elapsedTime += WATCH_INTERVAL_MS
                    Thread.sleep(WATCH_INTERVAL_MS.toLong())
                }
            }

            if (elapsedTime >= timeoutLimitMs) {
                status = CONDITION_TIMED_OUT
                break
            }
        } while (status != CONDITION_MET)

        if (status == CONDITION_TIMED_OUT && error != null) {
            val genericMessage = "${instruction.javaClass.simpleName} took more than ${
                TimeUnit.MILLISECONDS.toSeconds(timeoutLimitMs)
            } seconds. Test stopped. See the root cause below. $tag"

            throw IllegalStateException(genericMessage, error)
        }
    }
}

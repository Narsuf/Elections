package com.n27.test.conditions.instructions

import com.n27.test.conditions.instructions.ConditionResult.Met
import com.n27.test.conditions.instructions.ConditionResult.NotMet
import com.n27.test.conditions.wait.ConditionWatcher.waitForCondition

open class GenericInstruction(private val assertionBlock: () -> Unit) : Instruction {

    @Suppress("SwallowedException", "Detekt.TooGenericExceptionCaught")
    override fun checkCondition(): ConditionResult {
        return try {
            assertionBlock()
            Met
        } catch (e: Throwable) {
            NotMet(e)
        }
    }
}

fun waitUntil(block: () -> Unit) { waitForCondition(GenericInstruction(block)) }

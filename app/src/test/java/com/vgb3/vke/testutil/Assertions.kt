package com.vgb3.vke.testutil

internal suspend inline fun <reified T : Throwable> assertSuspendThrows(
    crossinline block: suspend () -> Unit,
): T {
    try {
        block()
    } catch (exception: Throwable) {
        if (exception is T) return exception
        throw AssertionError("Expected ${T::class.java.name}, got ${exception.javaClass.name}", exception)
    }
    throw AssertionError("Expected ${T::class.java.name}, but the call succeeded")
}

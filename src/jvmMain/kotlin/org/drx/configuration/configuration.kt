package org.drx.configuration

import kotlin.reflect.full.createInstance


inline fun <D, reified C: Configuration<D>> configure(noinline sideEffect:  C.()->Unit) : D {
    val c = C::class.createInstance()
    c.sideEffect()
    return c.configure()
}
suspend inline fun <D, reified C: Configuration<D>> configureSuspended(noinline sideEffect: suspend C.()->Unit) : D {
    val c = C::class.createInstance()
    c.sideEffect()
    return c.configureSuspended()
}

inline fun <D,  reified C: Configuration<D>, reified Data> configure(data: Data, noinline sideEffect:  C.()->Unit) : D {
    var n: C? = null
    C::class.constructors.forEach {
        try {
            n = it.call(data)
        }
        catch (exception: Exception){/* unimportant */}
    }
    if(n == null) {
        throw Exception("Constructor does not take arguments of type ${Data::class}")
    }
    n!!.sideEffect()
    return n!!.configure()
}

inline fun <D,  reified C: Configuration<D>> configure(data: Array<out Any?>, noinline sideEffect:  C.()->Unit) : D {
    var n: C? = null
    C::class.constructors.forEach {
        try {
            n = it.call(*data)
        }
        catch (exception: Exception){/* unimportant */}
    }
    if(n == null) {
        throw Exception("Constructor does not take arguments of these types")
    }
    n!!.sideEffect()
    return n!!.configure()
}

fun constructor(vararg args : Any?): Array<out Any?> = args

inline fun <D, reified C: Configuration<D>> setupConfiguration(noinline sideEffect:  C.()->Unit) : C {
    val c = C::class.createInstance()
    c.sideEffect()
    return c
}

package com.n27.core.extensions

fun <T> List<T>.compare(list: List<T>): List<Int> = zip(list)
    .mapIndexedNotNull { index, (item1, item2) ->
        if (item1 != item2)
            index
        else
            null
    }
package com.example.dollarschat.handler

interface EventHandler<T> {

    fun obtainEvent(event: T)
}
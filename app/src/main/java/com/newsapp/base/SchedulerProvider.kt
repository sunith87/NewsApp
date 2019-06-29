package com.newsapp.base

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ioScheduler(): Scheduler
    fun mainScheduler(): Scheduler
}
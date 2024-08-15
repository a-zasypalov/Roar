package com.gaoyun.roar.domain

interface SynchronisationScheduler {
    fun scheduleSynchronisation()
    fun scheduleNightlySynchronisation()
}
package com.minimart.inventory

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<InventoryServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
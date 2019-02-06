package com.jeansaunie.rattrapagekotlin

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

private val DATA_YEAR = 0
private val DATA_DEVICE = 1
private val DATA_ORDER = 2
private val DATA_IMPRESSIONS = 3
private val DATA_CLICS = 4
private val DATA_COST = 5
private val DATA_TURNOVER = 7
private val DATA_MONTH = 9

fun main(args: Array<String>?) {
    var fileReader: BufferedReader? = null
    val yearMap = mutableMapOf<Int, MutableMap<String, Number>>()
    val deviceMap = mutableMapOf<String, Number>()
    val deviceMonthMap = mutableMapOf<String, MutableMap<String, Number>>()

    try {
        val orders = ArrayList<Order>()
        var line: String?

        fileReader = BufferedReader(FileReader("data.csv"))

        // Read CSV header
        fileReader.readLine()

        // Read the file line by line starting from the second line
        line = fileReader.readLine()
        while (line != null) {
            val tokens = line.split(";")

            // Set order
            if (tokens.isNotEmpty()) setOrder(orders, tokens)

            line = fileReader.readLine()
        }

        // Print the new data list
        for (order in orders) {
            setYearMap(yearMap, order.year, order.month, order.turnover)
            setDeviceMap(deviceMap, order.device, order.turnover)
            setDeviceMonthMap(deviceMonthMap, order.device, order.month, order.turnover)
        }

        println(yearMap[2017])

        printYearMap(yearMap)
        printDeviceMap(deviceMap)
        printAverageCart(orders)
        printCPC(orders)
        printClickRate(orders)
        printROI(orders)
        printDeviceMonthMap(deviceMonthMap)

    } catch (e: Exception) {
        println("Reading CSV Error!")
        e.printStackTrace()
    } finally {
        try {
            fileReader!!.close()
        } catch (e: IOException) {
            println("Closing fileReader Error!")
            e.printStackTrace()
        }
    }
}

fun setOrder(orders: ArrayList<Order>, tokens: List<String>) {
    orders.add(
        Order(
            Integer.parseInt(tokens[DATA_YEAR]),
            tokens[DATA_DEVICE],
            NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[DATA_ORDER]),
            Integer.parseInt(tokens[DATA_IMPRESSIONS]),
            Integer.parseInt(tokens[DATA_CLICS]),
            NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[DATA_COST]),
            NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[DATA_TURNOVER]),
            tokens[DATA_MONTH]
        )
    )
}

fun formatFloat(format: String, value: Number): String {
    return format.format(value)
}

fun setYearMap(
    map: MutableMap<Int, MutableMap<String, Number>>,
    year: Int,
    month: String,
    value: Number
) {
    var yearMap = mutableMapOf<String, Number>()
    var turnover: Number = 0

    if (map.contains(year)) yearMap = map[year]!!
    if (yearMap.contains(month)) turnover = yearMap[month]!!

    yearMap[month] = turnover.toFloat() + value.toFloat()
    map[year] = yearMap
}

fun setDeviceMonthMap(
    map: MutableMap<String, MutableMap<String, Number>>,
    device: String,
    month: String,
    value: Number
) {
    var monthMap = mutableMapOf<String, Number>()
    var turnover: Number = 0

    if (map.contains(device)) monthMap = map[device]!!
    if (monthMap.contains(month)) turnover = monthMap[month]!!

    monthMap[month] = turnover.toFloat() + value.toFloat()
    map[device] = monthMap
}

fun setDeviceMap(map: MutableMap<String, Number>, device: String, value: Number) {
    var turnover: Number = 0

    if (map.contains(device)) turnover = map[device]!!

    map[device] = turnover.toFloat() + value.toFloat()
}

fun printYearMap(yearMap: MutableMap<Int, MutableMap<String, Number>>) {
    println("\nLe chiffre d’affaires par mois par année :")
    for ((year, monthMap) in yearMap) {
        for ((month, turnover) in monthMap) {
            println("[$year | $month]   =>  " + formatFloat("%.2f", turnover) + "€")
        }
    }
}

fun printDeviceMap(deviceMap: MutableMap<String, Number>) {
    println("\nLe chiffre d’affaires par appareil :")
    for ((device, turnover) in deviceMap) {
        println("$device   =>  " + formatFloat("%.2f", turnover) + "€")
    }
}

fun printDeviceMonthMap(deviceMonthMap: MutableMap<String, MutableMap<String, Number>>) {
    println("\nLe ROI segmenté par appareil et par mois (uniquement sur 2017) :")
    for ((device, monthMap) in deviceMonthMap) {
        for ((month, turnover) in monthMap) {
            println("[$device | $month]   =>  " + formatFloat("%.2f", turnover) + "€")
        }
    }
}

fun printAverageCart(orders: ArrayList<Order>) {
    val totalTurnover = orders.map { it.orders.toFloat() }.sum()
    println("\nLe panier moyen est de " + formatFloat("%.2f", totalTurnover / orders.size) + "€")
}

fun printCPC(orders: ArrayList<Order>) {
    val totalClicks = orders.map { it.clicks.toFloat() }.sum()
    val totalCosts = orders.map { it.cost.toFloat() }.sum()
    println("\nLe coût par clic est de " + formatFloat("%.2f", totalCosts / totalClicks) + "€")
}

fun printClickRate(orders: ArrayList<Order>) {
    val totalClicks = orders.map { it.clicks }.sum()
    val totalImpressions = orders.map { it.impressions }.sum()
    val rate = formatFloat("%.2f", (totalClicks.toFloat() / totalImpressions.toFloat()) * 100)
    println("\nLe taux de clic est de $rate%")
}

fun printROI(orders: ArrayList<Order>) {
    val totalTurnover = orders.map { it.turnover.toFloat() }.sum()
    val totalCosts = orders.map { it.cost.toFloat() }.sum()
    val roi = formatFloat("%.2f", totalTurnover / totalCosts)
    println("\nLe ROI est de $roi%")
}

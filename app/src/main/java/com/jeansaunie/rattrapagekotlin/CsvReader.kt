package com.jeansaunie.rattrapagekotlin

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.text.NumberFormat
import java.util.*

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
    var yearMap = mutableMapOf<Int, MutableMap<String, Number>>()
    var deviceMap = mutableMapOf<String, Number>()

    try {
        val datas = ArrayList<Data>()
        var line: String?

        fileReader = BufferedReader(FileReader("data.csv"))

        // Read CSV header
        fileReader.readLine()

        // Read the file line by line starting from the second line
        line = fileReader.readLine()
        while (line != null) {
            val tokens = line.split(";")

            // Set data
            if (tokens.isNotEmpty()) setData(datas, tokens)

            line = fileReader.readLine()
        }

        // Print the new data list
        for (data in datas) {
            setYearMap(yearMap, data.year, data.month, data.turnover)
            setDeviceMap(deviceMap, data.device, data.turnover)
//            println(data)
        }

        printYearMap(yearMap)
        printDeviceMap(deviceMap)

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

fun setData(datas: ArrayList<Data>, tokens: List<String>) {
    datas.add(
        Data(
            Integer.parseInt(tokens[DATA_YEAR]),
            tokens[DATA_DEVICE],
            NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[DATA_ORDER]),
            tokens[DATA_IMPRESSIONS],
            Integer.parseInt(tokens[DATA_CLICS]),
            NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[DATA_COST]),
            NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[DATA_TURNOVER]),
            tokens[DATA_MONTH]
        )
    )
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

    yearMap.put(month, turnover.toInt() + value.toInt())
    map.put(year, yearMap)
}

fun setDeviceMap(map: MutableMap<String, Number>, device: String, value: Number) {
    var turnover: Number = 0

    if (map.contains(device)) turnover = map[device]!!

    map.put(device, turnover.toInt() + value.toInt())
}

fun printYearMap(yearMap: MutableMap<Int, MutableMap<String, Number>>) {
    for ((year, monthMap) in yearMap) {
        for ((month, turnover) in monthMap) {
            println("Le chiffre d’affaires de $month de l'année $year est de $turnover€.")
        }
    }
}

fun printDeviceMap(deviceMap: MutableMap<String, Number>) {
    for ((device, turnover) in deviceMap) {
        println("Les appareils $device ont réalisé un chiffre d'affaire de $turnover€.")
    }
}
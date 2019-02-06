package com.jeansaunie.rattrapagekotlin

data class Data(
    var year: Int,
    var device: String,
    var order: Number,
    var impressions: String,
    var clics: Int,
    var cost: Number,
    var turnover: Number,
    var month: String
) {

    override fun toString(): String {
        return "Data [year=" + year + ", device=" + device + ", order=" + order + ", " +
                "impressions=" + impressions + ", clics=" + clics + ", cost=" + cost + ", " +
                "turnover=" + turnover + ", month=" + month + "]"
    }

}
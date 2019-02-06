package com.jeansaunie.rattrapagekotlin

data class Order(
    var year: Int,
    var device: String,
    var orders: Number,
    var impressions: Int,
    var clicks: Int,
    var cost: Number,
    var turnover: Number,
    var month: String
) {

    override fun toString(): String {
        return "Order [year=" + year + ", device=" + device + ", orders=" + orders + ", " +
                "impressions=" + impressions + ", clicks=" + clicks + ", cost=" + cost + ", " +
                "turnover=" + turnover + ", month=" + month + "]"
    }

}
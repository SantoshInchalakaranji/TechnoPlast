package com.prplmnstr.technoplast.models


class Date {
    var day = 0
    var month = 0
    var year = 0
    var dateInStringFormat: String? = null

    constructor() {}
    constructor(day: Int, month: Int, year: Int, dateInStringFormat: String?) {
        this.day = day
        this.month = month
        this.year = year
        this.dateInStringFormat = dateInStringFormat
    }
}

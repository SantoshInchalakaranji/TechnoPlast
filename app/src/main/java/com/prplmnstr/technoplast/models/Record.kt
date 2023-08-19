package com.prplmnstr.technoplast.models

data class Record (var name: String = "",
                   var mould: String = "",
                   var productionWT: String = "",
                   var orderQty: String = "",
                   var loadTime: String = "",
                   var unloadTime: String = "",
                   var article: String = "",
                   var heating: String = "",
                   var numCavity: String = "",
                   var heatingAct: String = "",
                   var rawMaterial: String = "",
                   var totalMaterialUsed: String = "",
                   var pigment: String = "",
                   var totalMbUsed: String = "",

                   var date : String = "",

                   var one: Int = 0,
                   var two: Int = 0,
                   var three: Int = 0,
                   var four: Int = 0,
                   var five: Int = 0,
                   var six: Int = 0,
                   var seven: Int = 0,
                   var eight: Int = 0,
                   var nine: Int = 0,
                   var ten: Int = 0,
                   var eleven: Int = 0,
                   var twelve: Int = 0,

                   var qty: Int = 0,
                   var bag: Int = 0,
                   var start: String = "",
                   var end: String = "",
                   var lump: Int = 0,
                   var grindingMaterialLeft: Int = 0,

                   var operator: String = "",
                   var shift:String = ""







        )
{
    fun isEqual(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val otherData = other as Record

        return toString() == otherData.toString()
    }
}
// selectedMachine.name
//selectedMachine.mould
//selectedMachine.productionWT
//selectedMachine.orderQty
// selectedMachine.loadTime
//selectedMachine.unloadTime
//selectedMachine.article
// selectedMachine.heating
//selectedMachine.numCavity
//selectedMachine.heatingAct
//selectedMachine.rawMaterial
//selectedMachine.totalMaterialUsed
//selectedMachine.pigment
// selectedMachine.totalMbUsed
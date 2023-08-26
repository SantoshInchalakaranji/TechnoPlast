package com.prplmnstr.technoplast.utils

import android.graphics.Path.Op
import java.util.Arrays

class Constants {
    companion object {
        const val ADMIN = "Admin";
        const val OPERATOR = "Operator";
        const val SAVED_USER_TYPE = "userType";
        const val SHARED_PREFERENCE = "Myprefs"
        const val OPERATORS = "Operators"
        const val MACHINES = "Machines"
        const val MOULDS = "Moulds"
        const val MACHINE = "Machine"
        const val SHIFT_MODE = "Shift"
        const val DAY_SHIFT = "Day"
        const val NIGHT_SHIFT = "Night"
        const val RECORD = "Record"
        const val RECORDS = "Records"
         val USER_TYPE_SIGN_IN = Arrays.asList<String>(
           OPERATOR,
            ADMIN
        )
    }
}
package ru.mifkamaz.recyclercrossfadetest

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

object FormatFactory {

    private val LOCALE = Locale("ru", "RU")

    val ddMMyyyy_dot
        get() = SimpleDateFormat("dd.MM.yyyy", LOCALE)

    val yyyyMMdd_dash
        get() = SimpleDateFormat("yyyy-MM-dd", LOCALE)

    val dMMM_space
        get() = SimpleDateFormat("d MMM", LOCALE)

    val dMMMM_space
        get() = SimpleDateFormat("d MMMM", LOCALE)

    val ddEE_space_comma
        get() = SimpleDateFormat("dd, EE", LOCALE)

    val dd
        get() = SimpleDateFormat("dd", LOCALE)

    val EEMMMM_space_line_separator
        get() = SimpleDateFormat("EE${System.lineSeparator()}MMMM", LOCALE)

    val dMMMyyyy_space
        get() = SimpleDateFormat("d MMM yyyy", LOCALE)

    val currency: DecimalFormat
        get() {
            val symbols = DecimalFormatSymbols.getInstance()
            symbols.groupingSeparator = ' '
            return DecimalFormat("###,### \u20BD", symbols)
        }

    val number: DecimalFormat
        get() {
            val symbols = DecimalFormatSymbols.getInstance()
            symbols.groupingSeparator = ' '
            return DecimalFormat("###,###", symbols)
        }

}
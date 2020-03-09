package no.mhl.showroom.util


/**
 * Used for returning an Int that is ordinal into a zero index origin, by simply minusing 1 from it.
 */
val Int.indexOrigin
    get() = this - 1
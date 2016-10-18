package com.ridi.books.helper.text

import java.nio.CharBuffer
import java.util.*

/**
 * Created by kering on 16. 4. 12..
 */
class AlphaNumericComparator : Comparator<String> {
    override fun compare(lhs: String, rhs: String): Int {
        val lb = CharBuffer.wrap(lhs)
        val rb = CharBuffer.wrap(rhs)

        while (lb.hasRemaining() && rb.hasRemaining()) {
            moveWindow(lb)
            moveWindow(rb)

            val result = compare(lb, rb)
            if (result != 0) {
                return result
            }

            prepareForNextIteration(lb)
            prepareForNextIteration(rb)
        }

        return lhs.length - rhs.length
    }

    private fun moveWindow(buffer: CharBuffer) {
        var start = buffer.position()
        var end = buffer.position()
        val isNumerical = Character.isDigit(buffer.get(start))
        while (end < buffer.limit() && isNumerical == Character.isDigit(buffer.get(end))) {
            ++end
            if (isNumerical && start + 1 < buffer.limit()
                    && buffer.get(start) == '0' && Character.isDigit(buffer.get(end))) {
                ++start // trim leading zeros
            }
        }

        buffer.position(start).limit(end)
    }

    private fun compare(lhs: CharBuffer, rhs: CharBuffer): Int {
        if (isNumerical(lhs) && isNumerical(rhs)) {
            return compareNumerically(lhs, rhs)
        }
        return compareAsStrings(lhs, rhs)
    }

    private fun isNumerical(buffer: CharBuffer) = Character.isDigit(buffer[buffer.position()])

    private fun compareNumerically(lhs: CharBuffer, rhs: CharBuffer): Int {
        val diff = lhs.length - rhs.length
        if (diff != 0) {
            return diff
        }
        var i = 0
        while (i < lhs.remaining() && i < rhs.remaining()) {
            val result = lhs.get(i) - rhs.get(i)
            if (result != 0) {
                return result
            }
            ++i
        }
        return 0
    }

    private fun prepareForNextIteration(buffer: CharBuffer) = buffer.position(buffer.limit()).limit(buffer.capacity())

    private fun compareAsStrings(lhs: CharBuffer, rhs: CharBuffer) = lhs.toString().compareTo(rhs.toString())
}

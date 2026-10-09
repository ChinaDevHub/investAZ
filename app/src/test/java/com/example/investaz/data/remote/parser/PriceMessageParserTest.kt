package com.example.investaz.data.remote.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PriceMessageParserTest {

    private val parser = PriceMessageParser()

    @Test
    fun `parses positional keys into dto`() {
        val payload = """{"result":[{"0":"up","1":"APPLE","2":"340.39","3":"340.59","4":"335.90","5":"341.49","6":20,"7":"2026-10-08T22:59:59.000Z"}]}"""

        val dto = parser.parse(payload).single()

        assertEquals("APPLE", dto.symbol)
        assertEquals("up", dto.direction)
        assertEquals("340.39", dto.bid)
        assertEquals("340.59", dto.ask)
        assertEquals(20, dto.spread)
        assertEquals("2026-10-08T22:59:59.000Z", dto.time)
    }

    @Test
    fun `skips invalid items and keeps valid ones`() {
        val payload = """{"result":[
            {"0":"up","1":"","2":"1","3":"1","4":"1","5":"1","6":1,"7":""},
            {"0":"up","1":"BAD","2":"abc","3":"1","4":"1","5":"1","6":1,"7":""},
            null,
            {"0":"down","1":"EURUSD","2":"1.08","3":"1.09","4":"1.07","5":"1.10","6":3,"7":""}
        ]}"""

        assertEquals(listOf("EURUSD"), parser.parse(payload).map { it.symbol })
    }

    @Test
    fun `malformed or unexpected payloads yield empty list`() {
        listOf("", "not json", "null", "{}", """{"result":"oops"}""", "[1,2,3]").forEach {
            assertTrue("payload: $it", parser.parse(it).isEmpty())
        }
    }
}

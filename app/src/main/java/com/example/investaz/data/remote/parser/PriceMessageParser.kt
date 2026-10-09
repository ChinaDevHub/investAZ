package com.example.investaz.data.remote.parser

import android.util.Log
import com.example.investaz.data.remote.dto.PriceDto
import org.json.JSONException
import org.json.JSONObject

/**
 * Parses `{"result":[{"0":"up","1":"AAPL","2":"1.0","3":"1.1","4":"0.9","5":"1.2","6":5,"7":"<iso>"}]}`.
 * Never throws: malformed payloads yield an empty list, malformed items are skipped.
 */
class PriceMessageParser {

    fun parse(payload: String): List<PriceDto> = try {
        val items = JSONObject(payload).optJSONArray(KEY_RESULT)
        if (items == null) {
            Log.w(TAG, "Payload has no '$KEY_RESULT' array")
            emptyList()
        } else {
            (0 until items.length()).mapNotNull { index -> items.optJSONObject(index)?.toDtoOrNull() }
        }
    } catch (e: JSONException) {
        Log.w(TAG, "Malformed price payload", e)
        emptyList()
    }

    private fun JSONObject.toDtoOrNull(): PriceDto? {
        val dto = PriceDto(
            direction = optString("0"),
            symbol = optString("1"),
            bid = optString("2"),
            ask = optString("3"),
            low = optString("4"),
            high = optString("5"),
            spread = optInt("6"),
            time = optString("7"),
        )
        if (dto.isValid()) return dto
        Log.w(TAG, "Skipping invalid item: $this")
        return null
    }

    private fun PriceDto.isValid(): Boolean =
        symbol.isNotBlank() && listOf(bid, ask, low, high).all { it.toBigDecimalOrNull() != null }

    private companion object {
        const val TAG = "PriceMessageParser"
        const val KEY_RESULT = "result"
    }
}

package com.ksj.sauruspang.util

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

/**
 * - 하루 최대 1회 촬영 가능
 * - 5분마다 1회 자동 충전 (최대 1회)
 */
class CameraUsageManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "camera_usage_prefs"
        private const val KEY_USAGE_COUNT = "usage_count"
        private const val KEY_LAST_CHECK_TIME = "last_check_time"

        // 최대 촬영 가능 횟수 (변경 가능)
        const val MAX_USAGE = 1
        // 자동 충전 간격 (분 단위; 변경 가능)
        const val RECHARGE_INTERVAL_MINUTES = 5L
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * 앱 실행 시 자동 충전 체크 후 현재 사용 가능 횟수 반환
     */
    fun getUsageCount(): Int {
        checkRecharge()
        return prefs.getInt(KEY_USAGE_COUNT, MAX_USAGE)
    }

    /**
     * 촬영 시 1회 차감
     */
    fun decrementUsage() {
        val current = prefs.getInt(KEY_USAGE_COUNT, MAX_USAGE)
        if (current > 0) {
            prefs.edit().putInt(KEY_USAGE_COUNT, current - 1).apply()
        }
    }

    /**
     * 횟수 충전 (광고 등)
     */
    fun addUsage(amount: Int = 1) {
        val current = prefs.getInt(KEY_USAGE_COUNT, MAX_USAGE)
        val newCount = (current + amount).coerceAtMost(MAX_USAGE)
        prefs.edit().putInt(KEY_USAGE_COUNT, newCount).apply()
    }

    /**
     * 5분마다 자동 충전 (최대 MAX_USAGE까지)
     */
    private fun checkRecharge() {
        val now = System.currentTimeMillis()
        val lastCheck = prefs.getLong(KEY_LAST_CHECK_TIME, 0L)
        if (lastCheck == 0L) {
            prefs.edit().putLong(KEY_LAST_CHECK_TIME, now).apply()
            return
        }
        val minutesDiff = TimeUnit.MILLISECONDS.toMinutes(now - lastCheck)
        if (minutesDiff >= RECHARGE_INTERVAL_MINUTES) {
            val increments = (minutesDiff / RECHARGE_INTERVAL_MINUTES).toInt()
            if (increments > 0) {
                addUsage(increments) // 경과한 간격 수만큼 충전
                // 남은 시간을 반영하여 마지막 체크 시간 갱신
                val leftoverMillis = (minutesDiff % RECHARGE_INTERVAL_MINUTES) * TimeUnit.MINUTES.toMillis(1)
                val newLastCheck = now - leftoverMillis
                prefs.edit().putLong(KEY_LAST_CHECK_TIME, newLastCheck).apply()
            }
        }
    }
}

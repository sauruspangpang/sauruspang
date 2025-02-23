package com.ksj.sauruspang.util

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

/**
 *  - 하루 최대 4회
 *  - 6시간 지날 때마다 1회 자동 충전 (최대 4회)
 */
class CameraUsageManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "camera_usage_prefs"
        private const val KEY_USAGE_COUNT = "usage_count"
        private const val KEY_LAST_CHECK_TIME = "last_check_time"

        private const val MAX_USAGE = 4
        private const val RECHARGE_INTERVAL_HOURS = 6
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * - 앱 실행 시마다 checkRecharge()로 자동 회복 체크
     * - 회복 후 prefs에 갱신
     */
    fun getUsageCount(): Int {
        checkRecharge()
        return prefs.getInt(KEY_USAGE_COUNT, MAX_USAGE) // 기본값 4
    }

    /**
     * 사용 1회 차감
     */
    fun decrementUsage() {
        val current = getUsageCount()
        if (current > 0) {
            prefs.edit().putInt(KEY_USAGE_COUNT, current - 1).apply()
        }
    }

    /**
     * 리워드 광고 시 1회 충전 (최대 4)
     */
    fun addUsage(amount: Int = 1) {
        val current = getUsageCount()
        val newCount = (current + amount).coerceAtMost(MAX_USAGE)
        prefs.edit().putInt(KEY_USAGE_COUNT, newCount).apply()
    }

    /**
     * 6시간마다 1회씩 자동 충전(최대 4회)
     */
    private fun checkRecharge() {
        val now = System.currentTimeMillis()
        val lastCheck = prefs.getLong(KEY_LAST_CHECK_TIME, 0L)

        // 최초 실행 시점이라면 현재 시간을 기록만 하고 종료
        if (lastCheck == 0L) {
            prefs.edit().putLong(KEY_LAST_CHECK_TIME, now).apply()
            return
        }

        // 시간 차 계산 (밀리초 -> 시간)
        val hoursDiff = TimeUnit.MILLISECONDS.toHours(now - lastCheck)
        if (hoursDiff >= RECHARGE_INTERVAL_HOURS) {
            // 몇 번 충전 가능한지
            val increments = (hoursDiff / RECHARGE_INTERVAL_HOURS).toInt()

            if (increments > 0) {
                addUsage(increments)
                // leftover 시간을 반영하여 lastCheck 갱신
                val leftoverMillis =
                    (hoursDiff % RECHARGE_INTERVAL_HOURS) * TimeUnit.HOURS.toMillis(1)
                val newLastCheck = now - leftoverMillis
                prefs.edit().putLong(KEY_LAST_CHECK_TIME, newLastCheck).apply()
            }
        }
    }
}


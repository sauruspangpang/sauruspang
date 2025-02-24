package com.ksj.sauruspang.util

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

/**
 * - 하루 최대 4회 촬영 가능
 * - 6시간마다 1회 자동 충전 (최대 4회)
 */
class CameraUsageManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "camera_usage_prefs"
        private const val KEY_USAGE_COUNT = "usage_count"
        private const val KEY_LAST_CHECK_TIME = "last_check_time"

        // 최대 촬영 가능 횟수 (여기서 변경)
        const val MAX_USAGE = 4
        // 자동 충전 간격 (시간 단위; 여기서 변경)
        const val RECHARGE_INTERVAL_HOURS = 6L
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * 앱 실행 시마다 자동 충전 체크 후 현재 사용 가능 횟수 반환
     */
    fun getUsageCount(): Int {
        checkRecharge()
        return prefs.getInt(KEY_USAGE_COUNT, MAX_USAGE)
    }

    /**
     * 촬영 시 1회 차감
     */
    fun decrementUsage() {
        val current = getUsageCount()
        if (current > 0) {
            prefs.edit().putInt(KEY_USAGE_COUNT, current - 1).apply()
        }
    }

    /**
     * 횟수 충전 (광고 등으로 사용)
     * 기본 매개변수는 변경하지 않고, 호출 시 원하는 충전량을 지정합니다.
     */
    fun addUsage(amount: Int = 1) {
        val current = getUsageCount()
        val newCount = (current + amount).coerceAtMost(MAX_USAGE)
        prefs.edit().putInt(KEY_USAGE_COUNT, newCount).apply()
    }

    /**
     * 6시간마다 자동 충전 (최대 MAX_USAGE까지)
     */
    private fun checkRecharge() {
        val now = System.currentTimeMillis()
        val lastCheck = prefs.getLong(KEY_LAST_CHECK_TIME, 0L)

        if (lastCheck == 0L) {
            prefs.edit().putLong(KEY_LAST_CHECK_TIME, now).apply()
            return
        }

        val hoursDiff = TimeUnit.MILLISECONDS.toHours(now - lastCheck)
        if (hoursDiff >= RECHARGE_INTERVAL_HOURS) {
            val increments = (hoursDiff / RECHARGE_INTERVAL_HOURS).toInt()
            if (increments > 0) {
                addUsage(increments)
                // 남은 시간을 반영하여 마지막 체크 시간 갱신 (간단한 예시)
                val leftoverMillis = (hoursDiff % RECHARGE_INTERVAL_HOURS) * TimeUnit.HOURS.toMillis(1)
                val newLastCheck = now - leftoverMillis
                prefs.edit().putLong(KEY_LAST_CHECK_TIME, newLastCheck).apply()
            }
        }
    }
}

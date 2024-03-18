package com.muen.flappybird.util

import com.tencent.mmkv.MMKV

object MMKVManager {
    private val mmkv = MMKV.defaultMMKV()

    //缓存变量
    private const val KEY_BEST_SCORE = "best_score"

    /**
     * 最高分数
     */
    var bestScore: Int
        set(value) {
            mmkv.encode(KEY_BEST_SCORE,value)
        }
        get() = mmkv.decodeInt(KEY_BEST_SCORE)
}
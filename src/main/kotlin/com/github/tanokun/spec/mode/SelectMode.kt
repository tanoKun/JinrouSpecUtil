package com.github.tanokun.spec.mode

import org.bukkit.entity.Player

/**
 *
 */
interface SelectMode {
    val name: String

    /**
     * [amount]分、ランダムに選択します。
     *
     * @param candidate 観戦者の候補
     * @param amount 必要数
     *
     * @return 選ばれた観戦者
     */
    fun select(candidate: Collection<Player>, amount: Int): List<Player>
}
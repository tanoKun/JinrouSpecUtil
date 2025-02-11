package com.github.tanokun.spec.mode

import org.bukkit.entity.Player

const val MAX_PLAYER = 15

interface SelectMode {
    val name: String

    /**
     * [MAX_PLAYER] から離れている分、観戦者を選択します。
     *
     * @param candidate 観戦者の候補
     *
     * @return 選ばれた観戦者
     */
    fun select(candidate: Collection<Player>): List<Player>
}
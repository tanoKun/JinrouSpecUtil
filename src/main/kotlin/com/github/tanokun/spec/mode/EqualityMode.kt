package com.github.tanokun.spec.mode

import com.github.tanokun.spec.player.asJinrouPlayer
import org.bukkit.entity.Player

/**
 * 試合参加数を基準に、より多くの試合に参加している人からランダムに
 * 観戦者を選択します。
 *
 * 連続した観戦を許容し、少ない参加数の者を優遇する選択モードです。
 *
 */
object EqualityMode: SelectMode {
    override val name: String = "平等モード"

    override fun select(candidate: Collection<Player>): List<Player> {
        val need = candidate.size - MAX_PLAYER
        if (need <= 0) return emptyList()

        val sorted = candidate
            .shuffled()
            .sortedByDescending { it.asJinrouPlayer().getTotalPlayer() }

        return sorted.subList(0, need)
    }

    override fun toString(): String = "試合参加数が多い人順に、観戦者を選択します。"
}
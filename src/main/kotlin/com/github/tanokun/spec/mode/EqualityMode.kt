package com.github.tanokun.spec.mode

import com.github.tanokun.spec.player.asJinrouPlayer
import org.bukkit.entity.Player
import java.util.UUID

/**
 * 試合参加数を基準に、より多くの試合に参加している人からランダムに
 * 観戦者を選択します。
 *
 * 連続した観戦を許容し、少ない参加数の者を優遇する選択モードです。
 *
 */
object EqualityMode: SelectMode {
    override val name: String = "平等モード"

    override fun select(candidate: Collection<Player>, amount: Int): List<Player> {
        if (amount <= 0) return emptyList()
        if (candidate.size <= amount) return candidate.toList()

        val sorted = candidate
            .shuffled()
            .sortedByDescending { it.asJinrouPlayer().getTotalPlayer() }

        return sorted.subList(0, amount)
    }

    override fun toString(): String = "試合参加数が多い人順に、観戦者を選択します。"
}
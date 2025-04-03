package com.github.tanokun.spec.mode

import com.github.tanokun.spec.player.asJinrouPlayer
import org.bukkit.entity.Player

/**
 * 以下のルールに従い優先度を決め、ランダムに観戦者を選択します。
 * - 最後の試合が参加者であった人
 * - 最後の試合が観戦者であった人
 * - 試合数が0の人(初参加)
 *
 * [EqualityMode] とは違い参加数の平等が保証されません。
 */
object NonLastSpectatorMode: SelectMode {
    override val name: String = "運ゲーモード"

    private val comparator = compareByDescending<Player> { it.asJinrouPlayer().isPlayerAtLast() }
        .thenByDescending { it.asJinrouPlayer().isSpectatorAtLast() }
        .thenByDescending { it.asJinrouPlayer().getTotalPlayer() > 0 }

    override fun select(candidate: Collection<Player>, amount: Int): List<Player> {
        if (amount <= 0) return emptyList()
        if (candidate.size <= amount) return candidate.toList()

        val sortedLastPlayer = candidate
            .shuffled()
            .sortedWith(comparator)

        return sortedLastPlayer.subList(0, amount)
    }

    override fun toString(): String = "最後の試合が観戦者ではない人から選択します。"
}
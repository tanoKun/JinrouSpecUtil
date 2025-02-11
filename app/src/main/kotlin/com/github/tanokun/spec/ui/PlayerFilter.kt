package com.github.tanokun.spec.ui

import com.github.tanokun.spec.TriggerConfig
import org.bukkit.entity.Player

sealed interface PlayerFilter {
    fun filter(player: Player): Boolean

    object CurrentSpectator: PlayerFilter {
        override fun filter(player: Player): Boolean = player.scoreboardTags.contains(TriggerConfig.SPECTATOR_MAKER)
    }

    object CurrentPlayer: PlayerFilter {
        override fun filter(player: Player): Boolean = !player.scoreboardTags.contains(TriggerConfig.SPECTATOR_MAKER)
    }

    object All: PlayerFilter {
        override fun filter(player: Player): Boolean = true
    }
}
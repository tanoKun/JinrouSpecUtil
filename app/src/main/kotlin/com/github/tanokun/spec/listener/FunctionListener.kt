package com.github.tanokun.spec.listener

import com.github.tanokun.spec.TriggerConfig
import com.github.tanokun.spec.player.asJinrouPlayer
import com.github.tanokun.spec.ui.PlayerFilter
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent

object FunctionListener: Listener {

    @EventHandler
    fun onFunction(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isManager()) return
        if (!e.message.contains(TriggerConfig.START_MAKER)) return

        syncAllPlayer()
    }

    @EventHandler
    fun onFunction(e: ServerCommandEvent) {
        if (!e.command.contains(TriggerConfig.START_MAKER)) return

        syncAllPlayer()
    }

    private fun syncAllPlayer() {
        Bukkit.getOnlinePlayers().forEach {
            if (PlayerFilter.CurrentSpectator.filter(it)) it.asJinrouPlayer().asSpectator()
            else it.asJinrouPlayer().asPlayer()
        }
    }

    private fun Player.isManager() = this.hasPermission("com.tanokun.github.spec")
}
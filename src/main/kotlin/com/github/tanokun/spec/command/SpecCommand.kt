package com.github.tanokun.spec.command

import com.github.tanokun.spec.plugin
import com.github.tanokun.spec.ui.SelectUI
import com.github.tanokun.spec.ui.button.WHEN_BE_HEAD_TEXTURE
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder
import xyz.xenondevs.invui.item.builder.SkullBuilder
import java.util.*

object SpecCommand: CommandExecutor {
    private val selectUIs = HashMap<UUID, SelectUI>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            //caching
            Bukkit.getOnlinePlayers().forEach {
                try {
                    SkullBuilder.HeadTexture.of(it)
                } catch (_: Exception) {
                    SkullBuilder.HeadTexture(WHEN_BE_HEAD_TEXTURE)
                }
            }

            val selectUI = selectUIs.getOrPut(sender.uniqueId) { SelectUI(sender.uniqueId) }

            Bukkit.getScheduler().callSyncMethod(plugin) { selectUI.open() }
        })

        return true
    }
}
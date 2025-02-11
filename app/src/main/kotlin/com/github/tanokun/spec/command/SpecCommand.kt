package com.github.tanokun.spec.command

import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object SpecCommand: CommandExecutor {
    private val selectUIs = HashMap<UUID, SelectUI>()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false

        val selectUI = selectUIs.getOrPut(sender.uniqueId) { SelectUI(sender.uniqueId) }
        selectUI.open()

        return true
    }
}
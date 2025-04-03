package com.github.tanokun.spec

import com.github.tanokun.spec.command.SpecCommand
import com.github.tanokun.spec.listener.FunctionListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import xyz.xenondevs.invui.InvUI
import java.util.*

class SpectatorUtil: JavaPlugin() {
    init {
        plugin = this
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(FunctionListener, this)
        InvUI.getInstance().setPlugin(this)

        this.getCommand("spec")?.setExecutor(SpecCommand)
    }
}

private val cooltimeOnUI = HashSet<UUID>()

lateinit var plugin: Plugin
    private set

fun Player.setCooltimeOnUI() {
    cooltimeOnUI.add(this.uniqueId)
    Bukkit.getScheduler().runTaskLater(plugin, Runnable {
        cooltimeOnUI.remove(this.uniqueId)
    }, 5)
}

fun Player.isCooltimeOnUI() = cooltimeOnUI.contains(this.uniqueId)

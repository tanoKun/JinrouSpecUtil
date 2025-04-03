package com.github.tanokun.spec.ui.button.exchange

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.player.registerSpectator
import com.github.tanokun.spec.player.unregisterSpectator
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.PlayerFilter
import com.github.tanokun.spec.ui.RandomlyExchangeSpectatorsUI
import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class RandomlyExchangeButton(
    private val selectUI: SelectUI,
    private val ui: RandomlyExchangeSpectatorsUI
): AbstractItem() {
    override fun getItemProvider(): ItemProvider {
        val selected = ui.selectedSpectators
            .filterValues { it == true }
            .mapNotNull { Bukkit.getPlayer(it.key)?.displayName }

        return ItemBuilder(Material.END_CRYSTAL)
            .setDisplayName("§lランダムで入れ替える。")
            .addLoreLines(
                "§7現在選択中のモードから、入れ替えます。",
                "以下の選択者"
            )
            .addLoreLines(*selected.map { "§f・$it" }.toTypedArray())
    }

    override fun handleClick(clickType: ClickType, clicker: Player, e: InventoryClickEvent) {
        if (clicker.isCooltimeOnUI()) return
        clicker.setCooltimeOnUI()

        val selected = ui.selectedSpectators
            .filterValues { it == true }
            .map { Bukkit.getPlayer(it.key) }

        selected.forEach { it?.unregisterSpectator() }

        val candidate = Bukkit.getOnlinePlayers()
            .filter(PlayerFilter.CurrentPlayer::filter)
            .filter {
                println(it.name())
                ui.selectedSpectators[it.uniqueId] != true
            }

        selectUI.mode.select(candidate, selected.size).forEach {
            it.registerSpectator()
        }

        clicker.playSound(clicker, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
        clicker.closeInventory()
    }
}
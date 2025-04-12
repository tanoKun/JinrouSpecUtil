package com.github.tanokun.spec.ui.button

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.player.registerSpectator
import com.github.tanokun.spec.player.unregisterSpectator
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.PlayerFilter
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

private const val MAX_PLAYER = 15

class SelectButton(private val ui: SelectUI): AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA)
            .setDisplayName("§l観戦者選択")
            .addLoreLines("§7クリックで、一括で観戦者を選択します。")
    }

    override fun handleClick(clickType: ClickType, player: Player, e: InventoryClickEvent) {
        if (player.isCooltimeOnUI()) return
        player.setCooltimeOnUI()

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
        unregisterAll()
        val candidate = Bukkit.getOnlinePlayers().filter(PlayerFilter.CurrentPlayer::filter)
        ui.mode.select(candidate, candidate.size - MAX_PLAYER).forEach {
            it.registerSpectator()
        }

        player.closeInventory()
    }

    private fun unregisterAll() {
        Bukkit.getOnlinePlayers()
            .filter(PlayerFilter.CurrentSpectator::filter)
            .forEach(Player::unregisterSpectator)
    }
}
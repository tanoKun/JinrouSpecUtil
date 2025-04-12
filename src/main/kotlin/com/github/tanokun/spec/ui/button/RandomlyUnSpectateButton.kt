package com.github.tanokun.spec.ui.button

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.player.unregisterSpectator
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.PlayerFilter
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class RandomlyUnSpectateButton: AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.HEART_OF_THE_SEA)
            .setDisplayName("§lランダム観戦解除")
            .addLoreLines("§7余分なものをランダムで解除します。")
    }

    override fun handleClick(clickType: ClickType, player: Player, e: InventoryClickEvent) {
        if (player.isCooltimeOnUI()) return
        player.setCooltimeOnUI()

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)

        randomlySelect().forEach(Player::unregisterSpectator)

        player.closeInventory()
    }

    private fun randomlySelect(): List<Player> {
        val spectators = Bukkit.getOnlinePlayers().filter(PlayerFilter.CurrentSpectator::filter)
        val extra = spectators.size - MAX_PLAYER

        if (extra <= 0) return listOf()

        return spectators.shuffled().subList(0, extra)
    }
}
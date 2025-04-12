package com.github.tanokun.spec.ui.button

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.RandomlyExchangeSpectatorsUI
import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ShowExchangeButton(private val ui: SelectUI): AbstractItem() {
    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.BELL)
            .setDisplayName("§lランダム入れ替え")
            .addLoreLines("§7選択した観戦者と、ランダムで入れ替えます。")
    }

    override fun handleClick(clickType: ClickType, player: Player, e: InventoryClickEvent) {
        if (player.isCooltimeOnUI()) return
        player.setCooltimeOnUI()

        player.playSound(player, Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F)

        RandomlyExchangeSpectatorsUI(player, ui).open()
    }
}
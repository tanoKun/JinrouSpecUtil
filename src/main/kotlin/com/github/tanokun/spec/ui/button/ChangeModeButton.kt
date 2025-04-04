package com.github.tanokun.spec.ui.button

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.mode.EqualityMode
import com.github.tanokun.spec.mode.NonLastSpectatorMode
import com.github.tanokun.spec.mode.SelectMode
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ChangeModeButton: AbstractItem() {
    private var selectMode: SelectMode = NonLastSpectatorMode

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.OAK_SIGN)
            .setDisplayName("§l現在のモード: §b§l「${selectMode.name}」")
            .addLoreLines("$selectMode")
            .addLoreLines("§7§nクリックで切り替えます。")
    }

    override fun handleClick(clickType: ClickType, player: Player, e: InventoryClickEvent) {
        if (player.isCooltimeOnUI()) return
        player.setCooltimeOnUI()

        selectMode = if (selectMode == EqualityMode)
            NonLastSpectatorMode
        else
            EqualityMode

        notifyWindows()

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
    }

    fun select(candidate: Collection<Player>, amount: Int): List<Player> = selectMode.select(candidate, amount)
}
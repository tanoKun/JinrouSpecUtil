package com.github.tanokun.spec.ui.button.viewable

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.PlayerFilter
import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class SetViewableButton(
    private val ui: SelectUI,
    private val description: String,
    private val show: Material,
    private val filter: PlayerFilter,
    private val notify: () -> Unit
): AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        if (ui.viewable == filter) {
            return ItemBuilder(show)
                .setDisplayName("§b§l$description §7§l(現在)")
        }
        return ItemBuilder(Material.STRUCTURE_VOID)
            .setDisplayName("§l$description")
            .addLoreLines("§7クリックで切り替えます。")
    }

    override fun handleClick(clickType: ClickType, player: Player, e: InventoryClickEvent) {
        if (ui.viewable == filter) return
        if (player.isCooltimeOnUI()) return
        player.setCooltimeOnUI()

        ui.viewable = filter

        notifyWindows()
        notify()
        ui.update()

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
    }
}
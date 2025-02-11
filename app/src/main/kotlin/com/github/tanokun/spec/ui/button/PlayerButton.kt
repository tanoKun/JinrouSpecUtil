package com.github.tanokun.spec.ui.button

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.player.asJinrouPlayer
import com.github.tanokun.spec.player.registerSpectator
import com.github.tanokun.spec.player.unregisterPlayer
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.PlayerFilter
import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class PlayerButton(private val player: Player, private val selectUI: SelectUI): AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        if (PlayerFilter.CurrentSpectator.filter(player)) {
            return SkullBuilder(SkullBuilder.HeadTexture.of(player))
                .setDisplayName("§b§l${player.name}")
                .addLoreLines(
                    "§7現在の参加数: ${player.asJinrouPlayer().getTotalPlayer()}回",
                    "§7現在の観戦数: ${player.asJinrouPlayer().getTotalSpectator()}回",
                    "",
                    "§7クリックで、「§l参加者§7」にします。"
                )
                .addAllItemFlags()
        }

        return SkullBuilder(SkullBuilder.HeadTexture.of(player))
            .setDisplayName("§l${player.name}")
            .addLoreLines(
                "§7現在の参加数: ${player.asJinrouPlayer().getTotalPlayer()}回",
                "§7現在の観戦数: ${player.asJinrouPlayer().getTotalSpectator()}回",
                "",
                "§7クリックで、「§b§l観戦者§7」にします。"
            )
            .addAllItemFlags()
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (player.isCooltimeOnUI()) return
        player.setCooltimeOnUI()

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)

        if (PlayerFilter.CurrentSpectator.filter(player)) player.unregisterPlayer()
        else player.registerSpectator()

        selectUI.update()
    }
}
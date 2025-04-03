package com.github.tanokun.spec.ui.button.exchange

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.RandomlyExchangeSpectatorsUI
import com.github.tanokun.spec.ui.button.WHEN_BE_HEAD_TEXTURE
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class SelectedSpectatorButton(private val target: Player, private val ui: RandomlyExchangeSpectatorsUI): AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        val builder: AbstractItemBuilder<SkullBuilder> = try {
            SkullBuilder(SkullBuilder.HeadTexture.of(target))
        } catch (_: Exception) {
            SkullBuilder(SkullBuilder.HeadTexture(WHEN_BE_HEAD_TEXTURE))
        }

        return builder
            .setDisplayName("§b§l${target.name}")
            .addAllItemFlags()
    }

    override fun handleClick(clickType: ClickType, cilcker: Player, event: InventoryClickEvent) {
        if (cilcker.isCooltimeOnUI()) return
        cilcker.setCooltimeOnUI()

        cilcker.playSound(cilcker, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)

        ui.selectedSpectators[target.uniqueId] = false

        ui.update()
    }
}
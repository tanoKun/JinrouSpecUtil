package com.github.tanokun.spec.ui.button

import com.github.tanokun.spec.isCooltimeOnUI
import com.github.tanokun.spec.player.asJinrouPlayer
import com.github.tanokun.spec.player.registerSpectator
import com.github.tanokun.spec.player.unregisterSpectator
import com.github.tanokun.spec.setCooltimeOnUI
import com.github.tanokun.spec.ui.PlayerFilter
import com.github.tanokun.spec.ui.SelectUI
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

const val WHEN_BE_HEAD_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQyMDY1ZjJkMjA2YzBiNWQwMmMzNzY5ZGVmYzg5ZWU5YzUwMGZjMjYwMGY4NzA1NmQxYWEwNDk4NmRjZWEyMSJ9fX0="

class PlayerButton(private val target: Player, private val selectUI: SelectUI): AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        val builder: AbstractItemBuilder<SkullBuilder> = try {
            SkullBuilder(SkullBuilder.HeadTexture.of(target))
        } catch (_: Exception) {
            SkullBuilder(SkullBuilder.HeadTexture(WHEN_BE_HEAD_TEXTURE))
        }

        if (PlayerFilter.CurrentSpectator.filter(target)) {
            return builder
                .setDisplayName("§b§l${target.name}")
                .addLoreLines(*lore("§l参加者"))
                .addAllItemFlags()
        }

        return builder
            .setDisplayName("§l${target.name}")
            .addLoreLines(*lore("§b§l観戦者"))
            .addAllItemFlags()
    }

    override fun handleClick(clickType: ClickType, cilcker: Player, event: InventoryClickEvent) {
        if (cilcker.isCooltimeOnUI()) return
        cilcker.setCooltimeOnUI()

        cilcker.playSound(cilcker, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)

        if (PlayerFilter.CurrentSpectator.filter(target)) target.unregisterSpectator()
        else target.registerSpectator()

        selectUI.update()
    }

    private fun lore(next: String) =
        arrayOf(
            "§7現在の参加数: ${target.asJinrouPlayer().getTotalPlayer()}回",
            "§7現在の観戦数: ${target.asJinrouPlayer().getTotalSpectator()}回",
            "",
            "§7クリックで、「$next§7」にします。"
        )

}
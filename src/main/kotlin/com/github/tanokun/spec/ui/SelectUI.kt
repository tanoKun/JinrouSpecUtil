package com.github.tanokun.spec.ui

import com.github.tanokun.spec.mode.NonLastSpectatorMode
import com.github.tanokun.spec.mode.SelectMode
import com.github.tanokun.spec.player.asJinrouPlayer
import com.github.tanokun.spec.ui.button.ChangeModeButton
import com.github.tanokun.spec.ui.button.PlayerButton
import com.github.tanokun.spec.ui.button.SelectButton
import com.github.tanokun.spec.ui.button.ShowExchangeButton
import com.github.tanokun.spec.ui.button.page.BackPageButton
import com.github.tanokun.spec.ui.button.page.NextPageButton
import com.github.tanokun.spec.ui.button.viewable.SetViewableButton
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window
import java.util.*

class SelectUI(private val uniqueId: UUID) {
    private val player: Player
        get() = Bukkit.getPlayer(uniqueId) ?: throw IllegalStateException("player is offline.")

    val viewOnlySpectatorButton = SetViewableButton(
        ui = this,
        description = "観戦者のみ表示",
        show = Material.WITHER_ROSE,
        filter = PlayerFilter.CurrentSpectator
    ) {
        updateViewOnlyPlayerButton()
        updateViewAllButton()
        updateViewOnlyLastSpectatorButton()
    }

    val viewOnlyPlayerButton = SetViewableButton(
        ui = this,
        description = "参加者のみ表示",
        show = Material.PLAYER_HEAD,
        filter = PlayerFilter.CurrentPlayer
    ) {
        updateViewOnlySpectatorButton()
        updateViewAllButton()
        updateViewOnlyLastSpectatorButton()
    }

    val viewOnlyLastSpectatorButton = SetViewableButton(
        ui = this,
        description = "最終観戦者のみ表示",
        show = Material.SOUL_CAMPFIRE,
        filter = PlayerFilter.SpectatorAtLast
    ) {
        updateViewOnlySpectatorButton()
        updateViewOnlyPlayerButton()
        updateViewAllButton()
    }


    val viewAllButton = SetViewableButton(
        ui = this,
        description = "すべて表示",
        show = Material.SPYGLASS,
        filter = PlayerFilter.All
    ) {
        updateViewOnlySpectatorButton()
        updateViewOnlyPlayerButton()
        updateViewOnlyLastSpectatorButton()
    }

    val mode = ChangeModeButton()

    private val gui: PagedGui<Item> = PagedGui.items()
        .setStructure(
            "1 # x x x x x x x",
            "2 # x x x x x x x",
            "3 # x x x x x x x",
            "4 # x x x x x x x",
            "_ # # # # # # # #",
            "b c _ s e _ r _ n"
        )
        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .addIngredient('b', BackPageButton())
        .addIngredient('n', NextPageButton())
        .addIngredient('c', mode)
        .addIngredient('s', SelectButton(this))
        .addIngredient('e', ShowExchangeButton(this))
        .addIngredient('1', viewOnlyPlayerButton)
        .addIngredient('2', viewOnlySpectatorButton)
        .addIngredient('3', viewOnlyLastSpectatorButton)
        .addIngredient('4', viewAllButton)
        .addIngredient('#', SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
        .build()

    var viewable: PlayerFilter = PlayerFilter.All

    fun open() {
        gui.setContent(sortContents())

        Window.single()
            .setGui(gui)
            .setTitle("スペクテイター設定")
            .setViewer(player)
            .build()
            .open()
    }

    fun update() { gui.setContent(sortContents()) }

    private fun sortContents(): List<Item> {
        val comparator = compareByDescending<Player>(PlayerFilter.CurrentSpectator::filter)
            .thenByDescending(PlayerFilter.CurrentPlayer::filter)
            .thenByDescending { player.asJinrouPlayer().getTotalPlayer() }

        return Bukkit.getOnlinePlayers()
            .filter(viewable::filter)
            .sortedWith(comparator)
            .map { PlayerButton(it, this) }
    }

    private fun updateViewOnlySpectatorButton() {
        viewOnlySpectatorButton.notifyWindows()
    }

    private fun updateViewOnlyPlayerButton() {
        viewOnlyPlayerButton.notifyWindows()
    }

    private fun updateViewAllButton() {
        viewAllButton.notifyWindows()
    }

    private fun updateViewOnlyLastSpectatorButton() {
        viewOnlyLastSpectatorButton.notifyWindows()
    }
}
package com.github.tanokun.spec.ui

import com.github.tanokun.spec.ui.button.exchange.CandidateSpectatorButton
import com.github.tanokun.spec.ui.button.exchange.RandomlyExchangeButton
import com.github.tanokun.spec.ui.button.exchange.SelectedSpectatorButton
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window
import java.util.UUID

class RandomlyExchangeSpectatorsUI(private val player: Player, selectUI: SelectUI) {

    private val exchangeButton = RandomlyExchangeButton(selectUI, this)

    private val candidatesUI: PagedGui<Item> = PagedGui.items()
        .setStructure(
            "y y y y y y y y y",
            "y y y y y y y y y",
            "y y y y y y y y y",
            "y y y y y y y y y",
            "y y y y y y y y y",
            "y y y y y y y y y",
        )
        .addIngredient('y', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .build()

    private val selectedUI: PagedGui<Item> = PagedGui.items()
        .setStructure(
            "x x x x x x x x x",
            "x x x x x x x x x",
            "x x x x x x x x x",
            "# # # # e # # # #",
        )
        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .addIngredient('e', exchangeButton)
        .build()

    val selectedSpectators: HashMap<UUID, Boolean> = hashMapOf()

    init {
        Bukkit.getOnlinePlayers().filter(PlayerFilter.CurrentSpectator::filter).forEach {
            selectedSpectators[it.uniqueId] = false
        }
    }

    fun open() {
        update()

        Window.split()
            .setUpperGui(candidatesUI)
            .setLowerGui(selectedUI)
            .setTitle("ランダム入れ替え")
            .setViewer(player)
            .build()
            .open()
    }

    fun update() {
        exchangeButton.notifyWindows()

        candidatesUI.setContent(
            selectedSpectators
                .filterValues { it == false }
                .mapNotNull { CandidateSpectatorButton(Bukkit.getPlayer(it.key) ?: return@mapNotNull null, this) }
        )

        selectedUI.setContent(
            selectedSpectators
                .filterValues { it == true }
                .mapNotNull { SelectedSpectatorButton(Bukkit.getPlayer(it.key) ?: return@mapNotNull null, this) }
        )
    }
}
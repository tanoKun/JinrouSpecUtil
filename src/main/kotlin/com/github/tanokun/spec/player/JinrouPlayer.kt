package com.github.tanokun.spec.player

import com.github.tanokun.spec.TriggerConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class JinrouPlayer {
    private val states = arrayListOf<State>()

    /**
     * 次の試合が観戦判定になります。
     * 連続した呼び出しの場合、次の試合判定になります。
     */
    fun asSpectator() = states.add(State.SPECTATOR)

    /**
     * 次の試合が参加者判定になります。
     * 連続した呼び出しの場合、次の試合判定になります。
     */
    fun asPlayer() = states.add(State.PLAYER)

    /**
     * @return 観戦者として参加したゲーム数
     */
    fun getTotalSpectator(): Int = states.count { it.isSpectator() }

    /**
     * @return 参加者として参加したゲーム数
     */
    fun getTotalPlayer(): Int = states.count { it.isPlayer() }

    /**
     * @return 最後の試合が観戦者であるかどうか
     */
    fun isSpectatorAtLast(): Boolean = states.lastOrNull()?.isSpectator() == true

    /**
     * @return 最後の試合が参加者であるかどうか
     */
    fun isPlayerAtLast(): Boolean = states.lastOrNull()?.isPlayer() == true

    private enum class State {
        SPECTATOR,
        PLAYER,
        ;

        fun isSpectator() = this == SPECTATOR

        fun isPlayer() = this == PLAYER
    }
}

private val players = HashMap<UUID, JinrouPlayer>()

fun Player.asJinrouPlayer(): JinrouPlayer =
    players.getOrPut(this.uniqueId) { JinrouPlayer() }

fun Player.registerSpectator() {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as ${this.name} run ${TriggerConfig.REGISTER_COMMAND}")
    this.addScoreboardTag(TriggerConfig.SPECTATOR_MAKER)
}

fun Player.unregisterPlayer() {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as ${this.name} run ${TriggerConfig.UNREGISTER_COMMAND}")
    this.removeScoreboardTag(TriggerConfig.SPECTATOR_MAKER)
}
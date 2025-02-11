package com.github.tanokun.spec

object TriggerConfig {

    /**
     * 観戦者として登録するためのコマンド。
     */
    const val REGISTER_COMMAND: String = "function werewolf:admin/set_spectator"

    /**
     * 参加者として登録するためのコマンド。
     */
    const val UNREGISTER_COMMAND: String = "function werewolf:admin/remove_spectator"

    /**
     * 試合が始まったことを知らせる合図となるコマンド。
     * この合図を基準に前のゲームとする。
     */
    const val START_MAKER: String = "function werewolf:start"

    /**
     * 観戦者を表すスコアボードタグ
     */
    const val SPECTATOR_MAKER: String = "spectator"
}
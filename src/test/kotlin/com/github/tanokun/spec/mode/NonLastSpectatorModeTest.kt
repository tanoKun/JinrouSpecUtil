package com.github.tanokun.spec.mode

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import com.github.tanokun.spec.player.asJinrouPlayer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertContains
import kotlin.test.expect

class NonLastSpectatorModeTest {
    lateinit var serverMock: ServerMock

    @BeforeEach
    fun before() {
        serverMock = MockBukkit.mock()
    }

    @AfterEach
    fun after() {
        MockBukkit.unmock()
    }


    @Test
    fun whenWithinRange() {
        expect(emptyList()) { NonLastSpectatorMode.select(serverMock.onlinePlayers) }

        serverMock.setPlayers(15)
        expect(emptyList()) { NonLastSpectatorMode.select(serverMock.onlinePlayers) }
    }

    @Test
    fun whenNotWithinRange() {
        serverMock.setPlayers(16)
        expect(1) { NonLastSpectatorMode.select(serverMock.onlinePlayers).size }

        serverMock.setPlayers(20)
        expect(5) { NonLastSpectatorMode.select(serverMock.onlinePlayers).size }

        serverMock.setPlayers(24)
        expect(9) { NonLastSpectatorMode.select(serverMock.onlinePlayers).size }
    }

    @Test
    fun whenVariousPlayers() {
        serverMock.setPlayers(15)

        playerMock(UUID.fromString("e52429cf-d9bd-e6ed-b5d1-3d91fa6eddd3")).asJinrouPlayer().asPlayer()
        playerMock(UUID.fromString("8cbf4ac6-09d2-37e1-acdc-f8cf170ad45c")).asJinrouPlayer().asSpectator()
        playerMock(UUID.fromString("dddb4be6-ccaf-9c12-f24e-ee90aa5a5748")).asJinrouPlayer().asSpectator()

        val expect = arrayListOf(
            "player - dddb4be6-ccaf-9c12-f24e-ee90aa5a5748",
            "player - 8cbf4ac6-09d2-37e1-acdc-f8cf170ad45c",
            "player - e52429cf-d9bd-e6ed-b5d1-3d91fa6eddd3"
        )

        NonLastSpectatorMode.select(serverMock.onlinePlayers)
            .map { it.name }
            .forEach { actual ->
                assertContains(expect, actual)
            }
    }

    private fun playerMock(uuid: UUID): PlayerMock {
        val playerMock = PlayerMock(serverMock, "player - $uuid", uuid)
        serverMock.addPlayer(playerMock)

        return playerMock
    }
}
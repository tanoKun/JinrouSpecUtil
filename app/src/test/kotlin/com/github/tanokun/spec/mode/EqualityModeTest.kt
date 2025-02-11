package com.github.tanokun.spec.mode

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import com.github.tanokun.spec.player.asJinrouPlayer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test
import kotlin.test.expect


class EqualityModeTest {
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
        expect(emptyList()) { EqualityMode.select(serverMock.onlinePlayers) }

        serverMock.setPlayers(15)
        expect(emptyList()) { EqualityMode.select(serverMock.onlinePlayers) }
    }

    @Test
    fun whenNotWithinRange() {
        serverMock.setPlayers(16)
        expect(1) { EqualityMode.select(serverMock.onlinePlayers).size }

        serverMock.setPlayers(20)
        expect(5) { EqualityMode.select(serverMock.onlinePlayers).size }

        serverMock.setPlayers(24)
        expect(9) { EqualityMode.select(serverMock.onlinePlayers).size }
    }

    @Test
    fun whenDifferentCounts() {
        serverMock.setPlayers(14)

        playerMock(1, UUID.fromString("1b740939-5abe-9c73-ef33-271efe0b2e47"))

        //expect spectator
        playerMock(2, UUID.fromString("e52429cf-d9bd-e6ed-b5d1-3d91fa6eddd3"))
        playerMock(3, UUID.fromString("8cbf4ac6-09d2-37e1-acdc-f8cf170ad45c"))
        playerMock(4, UUID.fromString("dddb4be6-ccaf-9c12-f24e-ee90aa5a5748"))


        expect(
            arrayListOf(
                "player - dddb4be6-ccaf-9c12-f24e-ee90aa5a5748",
                "player - 8cbf4ac6-09d2-37e1-acdc-f8cf170ad45c",
                "player - e52429cf-d9bd-e6ed-b5d1-3d91fa6eddd3",
            )
        ) {
            EqualityMode.select(serverMock.onlinePlayers).map { it.name }
        }
    }

    private fun playerMock(asPlayerCount: Int, uuid: UUID) {
        val playerMock = PlayerMock(serverMock, "player - $uuid", uuid)
        serverMock.addPlayer(playerMock)

        repeat(asPlayerCount) { playerMock.asJinrouPlayer().asPlayer() }
    }
}
package com.example.l1z2

import java.util.*

enum class Sign{
    ROCK,
    PAPER,
    SCISSORS;

    companion object {
        fun pickRandom(): Sign {
            val r = Random()
            return values()[r.nextInt(3)]
        }
    }
}

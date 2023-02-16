package com.narsuf.elections.presentation.main.entities

internal sealed class MainInteraction {

    object ScreenOpened : MainInteraction()
    object Refresh : MainInteraction()
}

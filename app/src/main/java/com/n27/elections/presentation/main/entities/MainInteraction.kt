package com.n27.elections.presentation.main.entities

internal sealed class MainInteraction {

    object ScreenOpened : MainInteraction()
    object DialogDismissed : MainInteraction()
    object Refresh : MainInteraction()
}

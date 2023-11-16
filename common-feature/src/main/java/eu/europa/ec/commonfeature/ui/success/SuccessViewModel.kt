/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific language
 * governing permissions and limitations under the Licence.
 */

package eu.europa.ec.commonfeature.ui.success

import eu.europa.ec.commonfeature.config.SuccessUIConfig
import eu.europa.ec.uilogic.config.ConfigNavigation
import eu.europa.ec.uilogic.config.NavigationType
import eu.europa.ec.uilogic.mvi.MviViewModel
import eu.europa.ec.uilogic.mvi.ViewEvent
import eu.europa.ec.uilogic.mvi.ViewSideEffect
import eu.europa.ec.uilogic.mvi.ViewState
import eu.europa.ec.uilogic.navigation.Screen
import eu.europa.ec.uilogic.navigation.helper.generateComposableArguments
import eu.europa.ec.uilogic.navigation.helper.generateComposableNavigationLink
import eu.europa.ec.uilogic.serializer.UiSerializer
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

data class State(
    val successConfig: SuccessUIConfig
) : ViewState

sealed class Event : ViewEvent {
    data class ButtonClicked(val config: SuccessUIConfig.ButtonConfig) : Event()
    data object BackPressed : Event()
}

sealed class Effect : ViewSideEffect {
    sealed class Navigation : Effect() {
        data class SwitchScreen(
            val screenRoute: String
        ) : Navigation()

        data class PopBackStackUpTo(
            val screenRoute: String,
            val inclusive: Boolean
        ) : Navigation()

        data class DeepLink(
            val screen: Screen,
            val arguments: String,
            val flags: Int = 0
        ) : Navigation()
    }
}

@KoinViewModel
class SuccessViewModel constructor(
    private val uiSerializer: UiSerializer,
    @InjectedParam private val successConfig: String
) : MviViewModel<Event, State, Effect>() {
    override fun setInitialState(): State =
        State(
            successConfig = uiSerializer.fromBase64(
                successConfig,
                SuccessUIConfig::class.java,
                SuccessUIConfig.Parser
            ) ?: throw RuntimeException("SuccessUIConfig:: is Missing or invalid")
        )

    override fun handleEvents(event: Event) {
        when (event) {

            is Event.ButtonClicked -> {
                doNavigation(event.config.navigation)
            }

            is Event.BackPressed -> {
                doNavigation(
                    viewState.value.successConfig.onBackScreenToNavigate
                )
            }
        }
    }

    private fun doNavigation(navigation: ConfigNavigation) {

        val navigationEffect: Effect.Navigation = when (navigation.navigationType) {
            NavigationType.POP -> {
                Effect.Navigation.PopBackStackUpTo(
                    screenRoute = navigation.screenToNavigate.screenRoute,
                    inclusive = false
                )
            }

            NavigationType.PUSH -> {
                Effect.Navigation.SwitchScreen(
                    generateComposableNavigationLink(
                        screen = navigation.screenToNavigate,
                        arguments = generateComposableArguments(navigation.arguments),
                    )
                )
            }

            NavigationType.DEEPLINK -> Effect.Navigation.DeepLink(
                screen = navigation.screenToNavigate,
                arguments = generateComposableArguments(navigation.arguments),
                flags = navigation.flags,
            )
        }

        setEffect {
            navigationEffect
        }
    }
}
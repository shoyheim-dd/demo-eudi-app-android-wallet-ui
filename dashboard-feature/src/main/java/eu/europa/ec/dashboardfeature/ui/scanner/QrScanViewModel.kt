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

package eu.europa.ec.dashboardfeature.ui.scanner

import eu.europa.ec.businesslogic.di.PRESENTATION_SCOPE_ID
import eu.europa.ec.businesslogic.di.WalletPresentationScope
import eu.europa.ec.commonfeature.config.RequestUriConfig
import eu.europa.ec.commonfeature.extensions.getKoin
import eu.europa.ec.uilogic.mvi.MviViewModel
import eu.europa.ec.uilogic.mvi.ViewEvent
import eu.europa.ec.uilogic.mvi.ViewSideEffect
import eu.europa.ec.uilogic.mvi.ViewState
import eu.europa.ec.uilogic.navigation.ProximityScreens
import eu.europa.ec.uilogic.navigation.helper.generateComposableArguments
import eu.europa.ec.uilogic.navigation.helper.generateComposableNavigationLink
import eu.europa.ec.uilogic.serializer.UiSerializer
import org.koin.android.annotation.KoinViewModel

data object State : ViewState

sealed class Event : ViewEvent {
    data object GoBack : Event()
    data class OnQrScanned(val resultQr: String) : Event()

}

sealed class Effect : ViewSideEffect {
    sealed class Navigation : Effect() {
        data class SwitchScreen(val screenRoute: String) : Navigation()
        data object Pop : Navigation()
    }
}

@KoinViewModel
class QrScanViewModel(private val uiSerializer: UiSerializer) :
    MviViewModel<Event, State, Effect>() {

    override fun setInitialState(): State = State
    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GoBack -> setEffect { Effect.Navigation.Pop }
            is Event.OnQrScanned -> setEffect {
                event.resultQr.isNotBlank().let {
                    getKoin().getOrCreateScope<WalletPresentationScope>(PRESENTATION_SCOPE_ID)
                    Effect.Navigation.SwitchScreen(
                        screenRoute = generateComposableNavigationLink(
                            screen = ProximityScreens.Request,
                            arguments = generateComposableArguments(
                                mapOf(
                                    RequestUriConfig.serializedKeyName to uiSerializer.toBase64(
                                        RequestUriConfig(event.resultQr),
                                        RequestUriConfig.Parser
                                    )
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}
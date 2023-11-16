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

package eu.europa.ec.uilogic.controller

import eu.europa.ec.uilogic.config.ConfigUILogic

interface AnalyticsController {
    fun logScreen(name: String)
    fun logEvent(eventName: String, parameters: Map<String, String> = emptyMap())
}

class AnalyticsControllerImpl constructor(
    private val configUiLogic: ConfigUILogic,
) : AnalyticsController {

    override fun logScreen(name: String) {
        configUiLogic.analyticsProviders.forEach { analyticProvider ->
            analyticProvider.logScreen(name)
        }
    }

    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        configUiLogic.analyticsProviders.forEach { analyticProvider ->
            analyticProvider.logEvent(eventName, parameters)
        }
    }
}

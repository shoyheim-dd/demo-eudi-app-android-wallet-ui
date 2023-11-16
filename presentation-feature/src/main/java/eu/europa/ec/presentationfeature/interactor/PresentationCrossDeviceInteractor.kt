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

package eu.europa.ec.presentationfeature.interactor

import eu.europa.ec.businesslogic.extension.safeAsync
import eu.europa.ec.commonfeature.model.DocumentTypeUi
import eu.europa.ec.commonfeature.ui.request.model.UserDataDomain
import eu.europa.ec.commonfeature.ui.request.model.UserIdentificationDomain
import eu.europa.ec.resourceslogic.provider.ResourceProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class PresentationCrossDeviceInteractorPartialState {
    data class Success(val userDataDomain: UserDataDomain) :
        PresentationCrossDeviceInteractorPartialState()

    data class Failure(val error: String) : PresentationCrossDeviceInteractorPartialState()
}

interface PresentationCrossDeviceInteractor {
    fun getUserData(): Flow<PresentationCrossDeviceInteractorPartialState>
}

class PresentationCrossDeviceInteractorImpl(
    private val resourceProvider: ResourceProvider,
) : PresentationCrossDeviceInteractor {

    private val genericErrorMsg
        get() = resourceProvider.genericErrorMessage()

    override fun getUserData(): Flow<PresentationCrossDeviceInteractorPartialState> =
        flow {
            delay(2_000L)
            emit(
                PresentationCrossDeviceInteractorPartialState.Success(
                    userDataDomain = getFakeUserData()
                )
            )
        }.safeAsync {
            PresentationCrossDeviceInteractorPartialState.Failure(
                error = it.localizedMessage ?: genericErrorMsg
            )
        }

    private fun getFakeUserData(): UserDataDomain {
        return UserDataDomain(
            documentTypeUi = DocumentTypeUi.DIGITAL_ID,
            optionalFields = listOf(
                UserIdentificationDomain(
                    name = "Registration ID",
                    value = "EUDI123456"
                ),
                UserIdentificationDomain(
                    name = "Family Name",
                    value = "Doe"
                ),
                UserIdentificationDomain(
                    name = "First Name",
                    value = "Jane"
                ),
                UserIdentificationDomain(
                    name = "Room Number",
                    value = "A2"
                ),
                UserIdentificationDomain(
                    name = "Seat Number",
                    value = "128"
                ),
                UserIdentificationDomain(
                    name = "Registration ID",
                    value = "EUDI123456"
                ),
                UserIdentificationDomain(
                    name = "Family Name",
                    value = "Doe"
                ),
                UserIdentificationDomain(
                    name = "First Name",
                    value = "Jane"
                ),
                UserIdentificationDomain(
                    name = "Room Number",
                    value = "A2"
                ),
                UserIdentificationDomain(
                    name = "Seat Number",
                    value = "128"
                )
            ),
            requiredFieldsTitle = "Verification Data",
            requiredFields = listOf(
                UserIdentificationDomain(
                    name = "Issuance date",
                    value = null
                ),
                UserIdentificationDomain(
                    name = "Expiration date",
                    value = null
                ),
                UserIdentificationDomain(
                    name = "Country of issuance",
                    value = null
                ),
                UserIdentificationDomain(
                    name = "Issuing authority",
                    value = null
                )
            )
        )
    }
}
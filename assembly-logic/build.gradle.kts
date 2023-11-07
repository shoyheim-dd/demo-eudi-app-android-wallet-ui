/*
 *
 *  * Copyright (c) 2023 European Commission
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

plugins {
    id("eudi.android.library")
    id("eudi.android.library.compose")
}

android {
    namespace = "eu.europa.ec.assemblylogic"

    defaultConfig {
        manifestPlaceholders["deepLinkScheme"] = "eudi-wallet"
        manifestPlaceholders["deepLinkHost"] = "*"
    }
}

dependencies {

    // Logic Modules
    api(project(":resources-logic"))
    api(project(":business-logic"))
    api(project(":ui-logic"))
    api(project(":network-logic"))

    // Feature Modules
    api(project(":common-feature"))
    api(project(":startup-feature"))
    api(project(":login-feature"))
    api(project(":dashboard-feature"))
    api(project(":presentation-feature"))
}
/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api;

import org.gradle.api.provider.Provider;

/**
 * Provides a domain object of the given type.
 *
 * @param <T> type of domain object
 * @since 5.1
 */
@Incubating
public interface DomainObjectProvider<T> extends Provider<T> {
    /**
     * Configures the domain object with the given action. Actions are run in the order added.
     *
     * @param action A {@link Action} that can configure the domain object when required.
     * @since 5.1 
     */
    void configure(Action<? super T> action);
}

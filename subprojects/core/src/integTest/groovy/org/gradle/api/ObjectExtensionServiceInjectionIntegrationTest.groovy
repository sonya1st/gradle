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

package org.gradle.api

import org.gradle.integtests.fixtures.AbstractIntegrationSpec

import javax.inject.Inject


class ObjectExtensionServiceInjectionIntegrationTest extends AbstractIntegrationSpec {
    // Document current behaviour
    def "can create instance of extension with multiple constructors"() {
        buildFile << """
            class Thing {
                String a
                String b
                Thing(String a, String b = a) {
                    this.a = a
                    this.b = b
                }
            }
            
            def one = extensions.create("one", Thing, "a")
            def two = extensions.create("two", Thing, "a", "b")
            
            assert one.a == "a"
            assert one.b == "a"

            assert two.a == "a"
            assert two.b == "b"
        """

        expect:
        succeeds()
    }

    // Document current behaviour
    def "fails when extension constructor does not accept provided configurations"() {
        buildFile << """
            class Thing {
                Thing(String a, String b) {
                }
            }
            
            extensions.create("one", Thing, "a")
        """

        expect:
        fails()
        failure.assertHasCause("Could not create an instance of type Thing_Decorated.")
        failure.assertHasCause("Could not find any public constructor for class Thing_Decorated which accepts parameters [java.lang.String].")
    }

    // Document current behaviour
    def "fails when extension constructor accepts service types"() {
        buildFile << """
            class Thing {
                String a
                Thing(String a, ObjectFactory objects) {
                    this.a = a
                }
            }
            
            extensions.create("one", Thing, "a")
        """

        expect:
        fails()
        failure.assertHasCause("Could not create an instance of type Thing_Decorated.")
        failure.assertHasCause("Could not find any public constructor for class Thing_Decorated which accepts parameters [java.lang.String].")
    }

    // Document current behaviour
    def "fails when service injected using getter"() {
        buildFile << """
            import ${Inject.name}

            class Thing {
                Thing(String a) {
                    objects.property(String).set(a)
                }

                @Inject
                ObjectFactory getObjects() { }
            }
            
            extensions.create("one", Thing, "a")
        """

        expect:
        fails()
        failure.assertHasCause("Could not create an instance of type Thing_Decorated.")
        failure.assertHasCause("java.lang.NullPointerException (no error message)")
    }
}

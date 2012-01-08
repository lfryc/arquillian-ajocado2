/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.ajocado2.reusable;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.SessionId;

/**
 * @author <a href="mailto:lfryc@redhat.com>Lukas Fryc</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestPrivateAccessCompatibility {

    ReusableRemoteWebDriver driver;

    @Before
    public void initializeReusableRemoteWebDriver() {
        driver = new ReusableRemoteWebDriver();
    }

    @Test
    public void testSettingSessionIdCompatibility() {
        SessionId sessionId = mock(SessionId.class);

        driver.setReusedSessionId(sessionId);

        assertSame("Something must change internally in RemoteWebDriver, since sessionId cannot be set", sessionId,
                driver.getSessionId());
    }

    @Test
    public void testSettingCapabilitiesCompatibility() {
        Capabilities capabilities = mock(Capabilities.class);

        driver.setReusedCapabilities(capabilities);

        assertSame("Something must change internally in RemoteWebDriver, since capabilities cannot be set", capabilities,
                driver.getCapabilities());
    }
}

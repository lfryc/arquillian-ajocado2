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

import static junit.framework.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

/**
 * @author <a href="mailto:lfryc@redhat.com>Lukas Fryc</a>
 */
public class TestReusingBrowserSession extends AbstractInBrowserTest {

    @Test
    public void whenBrowserSessionIsCreatedThenCouldBeReused() throws UnableReuseSessionException {

        RemoteWebDriver driver = new RemoteWebDriver(HUB_URL, DesiredCapabilities.firefox());
        driver.navigate().to(SERVER_URL.toString());
        Capabilities reusedCapabilities = serializeDeserialize(driver.getCapabilities());
        SessionId reusedSessionId = new SessionId(serializeDeserialize(driver.getSessionId().toString()));

        ReusableRemoteWebDriver reusedDriver = new ReusableRemoteWebDriver(HUB_URL, reusedCapabilities, reusedSessionId);
        reusedDriver.navigate().to(HUB_URL.toString());
        reusedDriver.quit();
    }

    @Test
    public void whenBrowserSessionIsCreatedAndQuitAndTriedToReuseThenItShouldThrowException() {

        RemoteWebDriver driver = new RemoteWebDriver(HUB_URL, DesiredCapabilities.firefox());
        driver.navigate().to(SERVER_URL.toString());
        Capabilities reusedCapabilities = serializeDeserialize(driver.getCapabilities());
        SessionId reusedSessionId = new SessionId(serializeDeserialize(driver.getSessionId().toString()));
        driver.quit();

        try {
            new ReusableRemoteWebDriver(HUB_URL, reusedCapabilities, reusedSessionId);
            fail("Original driver had quited before, so session should not be reusable");
        } catch (UnableReuseSessionException e) {
            // exception should be thrown
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T serializeDeserialize(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            IOUtils.closeQuietly(out);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bais);
            return (T) object.getClass().cast(in.readObject());
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}

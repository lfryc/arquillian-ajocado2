package org.jboss.arquillian.ajocado2.reusable;

import java.net.URL;

import org.jboss.arquillian.ajocado.utils.URLUtils;

public class AbstractInBrowserTest {
    protected final URL SERVER_URL = URLUtils.buildUrl("http://127.0.0.1:4444/");
    protected final URL HUB_URL = URLUtils.buildUrl(SERVER_URL, "wd/hub");
}

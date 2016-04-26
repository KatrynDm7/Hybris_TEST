ATDD Runner extension
=====================

Abstract
--------

Some atdd tests require some web services to be up. During startup, platform spawns the services, but it happens after tests.
So, ATDD has to sapwn them by itself. The extension provides ATDD keywords and other required infrastructure for it.

How to use
----------

1. Declare bean in your Spring xml:
    <pre>
&lt;bean name="myWebApplication" parent="atddServletContainer"&gt;
    &lt;property name="connectionPath" value="http://localhost:8080/myapp"/&gt;
    &lt;!-- Path to war: absolute or relative to hybris/bin/platform --&gt;
    &lt;property name="warPath" value="../../ext-integration/myapp/myapp.war"/&gt;
    &lt;!-- Optional. Any properties to pass to the application
    &lt;property name="properties"&gt;
        &lt;props&gt;
            &lt;prop key="kernel.autoInitMode"&gt;create-drop&lt;/prop&gt;
        &lt;/props&gt;
    &lt;/property&gt;
&lt;/bean&gt;
    </pre>

2. Add app start/stop to your test suites (to only ones, that require the application):
    <pre>
*** Settings ***
Resource          atddrunner/AtddRunner_Keywords.txt
#
Suite Setup       start server "myWebApplication"
Suite Teardown    stop server "myWebApplication"
    </pre>

Notes
-----


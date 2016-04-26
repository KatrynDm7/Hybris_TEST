/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.cmscockpit.util;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created on 2014-12-02.
 */
@IntegrationTest
public class TestURLCOMInjectionSanitizer extends ServicelayerTest {

    @Test
    public void url_is_encoded_for_executing_command_with_pipe()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/bin/ls|");
        assertThat(StringUtils.contains(url, "|"), is(false));
        assertThat(StringUtils.contains(url, "%7C"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_semicolon()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/bin/ls;");
        System.out.print(url);
        assertThat(StringUtils.contains(url, ";"), is(false));
        assertThat(StringUtils.contains(url, "&"), is(false));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_two_semicolon()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/bin/ls;/bin/ls;");
        System.out.print(url);
        assertThat(StringUtils.contains(url, ";"), is(false));
        assertThat(StringUtils.contains(url, "&"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_in_double_quotes()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/\"`ls`\"");
        assertThat(StringUtils.contains(url, "\""), is(false));
        assertThat(StringUtils.contains(url, "%22"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_in_single_quotes()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/\"`ls`\"");
        assertThat(StringUtils.contains(url, "`"), is(false));
        assertThat(StringUtils.contains(url, "%60"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_dollarsign_and_brackets()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/\"$(ls)\"");
        assertThat(StringUtils.contains(url, "&"), is(true));
        assertThat(StringUtils.contains(url, "("), is(false));
        assertThat(StringUtils.contains(url, ")"), is(false));
        assertThat(StringUtils.contains(url, "%28"), is(true));
        assertThat(StringUtils.contains(url, "%29"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_two_ampersand()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=&&/bin/ls");
        assertThat(StringUtils.countMatches(url, "&"), is(1));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_greater_than_sign()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/bin/ls>tom.txt");
        assertThat(StringUtils.contains(url, ">"), is(false));
        assertThat(StringUtils.contains(url, "%3E"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_two_greater_than_signs()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=/bin/ls>>tom.txt");
        assertThat(StringUtils.contains(url, ">>"), is(false));
        assertThat(StringUtils.contains(url, "%3E%3E"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_less_than_sign()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc=<tom.txt");
        assertThat(StringUtils.contains(url, "<"), is(false));
        assertThat(StringUtils.contains(url, "%3C"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_with_curly_brackets()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://sensitive/cgi-bin/userData.pl?doc={ls}");
        assertThat(StringUtils.contains(url, "{"), is(false));
        assertThat(StringUtils.contains(url, "}"), is(false));
        assertThat(StringUtils.contains(url, "%7B"), is(true));
        assertThat(StringUtils.contains(url, "%7D"), is(true));
    }

    @Test
    public void url_is_encoded_for_executing_command_xml_encoding_ampersand_removed()
    {
        final String url = URLCOMInjectionSanitizer.sanitize("http://www.domain.com?balh=&;blah=blah");
        assertThat(StringUtils.contains(url, ";"), is(false));
        assertThat(StringUtils.countMatches(url, "&"), is(1));
    }

}

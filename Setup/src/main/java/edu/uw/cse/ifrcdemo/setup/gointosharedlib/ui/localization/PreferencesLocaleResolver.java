/*
 * Copyright (c) 2016-2022 University of Washington
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of the University of Washington nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE UNIVERSITY OF WASHINGTON AND CONTRIBUTORS “AS IS” AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE UNIVERSITY OF WASHINGTON OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package edu.uw.cse.ifrcdemo.setup.gointosharedlib.ui.localization;

import edu.uw.cse.ifrcdemo.translations.TranslationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * PreferencesLocaleResolver is a service class that extends AbstractLocaleResolver
 * to resolve and set locales based on application preferences.
 *
 * This resolver uses LocaleUtil to get and set the current locale, and
 * updates translations when the locale is changed.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
@Service
public class PreferencesLocaleResolver extends AbstractLocaleResolver {
  private final Logger logger;

  /**
   * Constructs a new PreferencesLocaleResolver with the specified logger.
   *
   * @param logger The Logger to be used for logging operations
   */
  public PreferencesLocaleResolver(Logger logger) {
    this.logger = logger;
  }

  /**
   * Resolves the current locale based on application preferences.
   *
   * @param request The HttpServletRequest (not used in this implementation)
   * @return The current Locale as determined by LocaleUtil
   */
  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    Locale locale = LocaleUtil.getCurrentLocale();

    logger.debug("Using locale {}", locale.toLanguageTag());
    return locale;
  }

  /**
   * Sets the current locale and updates translations accordingly.
   *
   * @param request The HttpServletRequest (not used in this implementation)
   * @param response The HttpServletResponse (not used in this implementation)
   * @param locale The Locale to set as the current locale
   */
  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    logger.debug("Setting locale to {}", locale);
    LocaleUtil.setLocale(locale);
    TranslationUtil.loadTranslationsFromLocale(locale);
  }
}

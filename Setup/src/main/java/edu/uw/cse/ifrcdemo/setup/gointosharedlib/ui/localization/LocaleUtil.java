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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * LocaleUtil is a utility class for managing the application's locale settings.
 * It provides methods to get and set the current locale, persisting the setting
 * using Java Preferences API.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
public class LocaleUtil {
  private static final String PREF_KEY = "locale";

  private static final Logger logger = LogManager.getLogger(LocaleUtil.class);

  /**
   * Retrieves the current locale setting.
   * If no locale has been set, it defaults to US locale.
   *
   * @return The current Locale object
   */
  public static Locale getCurrentLocale() {
    String localeStr = Preferences
        .userNodeForPackage(LocaleUtil.class)
        .get(PREF_KEY, null);

    Locale locale = Locale.US;
    if (localeStr != null) {
      logger.debug("Reading locale from pref {}", localeStr);
      locale = Locale.forLanguageTag(localeStr);
    }

    return locale;
  }

  /**
   * Sets the current locale and persists it using Java Preferences API.
   * This method also flushes the preferences to ensure the setting is saved,
   * which helps prevent intermittent exceptions on exit when run under JavaFX.
   *
   * @param locale The Locale to set as the current locale
   */
  public static void setLocale(Locale locale) {
    Preferences preferences = Preferences.userNodeForPackage(LocaleUtil.class);

    preferences.put(PREF_KEY, locale.toLanguageTag());
    try {
      // intermittent exception on exit when ran under javafx without this
      preferences.flush();
    } catch (BackingStoreException e) {
      logger.catching(Level.DEBUG, e);
    }
  }
}

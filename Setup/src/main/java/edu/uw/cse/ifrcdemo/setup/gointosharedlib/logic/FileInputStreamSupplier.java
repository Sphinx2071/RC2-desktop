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

package edu.uw.cse.ifrcdemo.setup.gointosharedlib.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * FileInputStreamSupplier is a class that implements the Supplier interface to provide
 * InputStreams for a given file path. This class is useful for creating InputStreams
 * on demand, which can be particularly helpful in scenarios where multiple streams
 * might be needed for the same file, or when the stream creation needs to be deferred.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
public class FileInputStreamSupplier implements Supplier<InputStream> {
  private final Path filePath;

  /**
   * Constructs a new FileInputStreamSupplier for the specified file path.
   *
   * @param filePath The Path object representing the file for which InputStreams will be supplied
   */
  public FileInputStreamSupplier(Path filePath) {
    this.filePath = filePath;
  }

  /**
   * Creates and returns a new InputStream for the file path specified in the constructor.
   * This method is called each time a new InputStream is needed.
   *
   * @return A new InputStream for the specified file
   * @throws UncheckedIOException if an IOException occurs when creating the InputStream
   */
  @Override
  public InputStream get() {
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}

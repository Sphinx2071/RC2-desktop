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

package edu.uw.cse.ifrcdemo.setup.ui.common;

import edu.uw.cse.ifrcdemo.sharedlib.consts.ServerConsts;
import org.opendatakit.suitcase.model.CloudEndpointInfo;

import java.net.MalformedURLException;

/**
 * CloudEndpointAuthFormModel is an interface that defines the structure for
 * authentication form models used to connect to cloud endpoints.
 * It provides methods for getting and setting server URL, username, and password,
 * as well as a default method to convert the model to a CloudEndpointInfo object.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this interface was introduced]
 */
public interface CloudEndpointAuthFormModel {
    String getServerUrl();

    void setServerUrl(String serverUrl);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    /**
     * Converts the form model to a CloudEndpointInfo object.
     * This default implementation creates a CloudEndpointInfo using the server URL,
     * a default app ID, username, and password.
     *
     * @return a CloudEndpointInfo object representing the authentication information
     * @throws MalformedURLException if the server URL is not a valid URL
     */
    default CloudEndpointInfo toCloudEndpointInfo() throws MalformedURLException {
        return new CloudEndpointInfo(
                getServerUrl(),
                ServerConsts.APP_ID_DEFAULT,
                getUsername(),
                getPassword()
        );
    }
}

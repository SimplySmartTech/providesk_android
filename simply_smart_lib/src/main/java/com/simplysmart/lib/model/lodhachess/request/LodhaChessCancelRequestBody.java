/*
 * Copyright 2016. Alejandro Sánchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.simplysmart.lib.model.lodhachess.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by shekhar on 23/03/17.
 */
@Root(name = "soap:Body", strict = false)
public class LodhaChessCancelRequestBody {

    @Element(name = "GiveCancelRequest",required = false)
    private LodhaChessCancelRequestData GiveCancelRequest;

    public LodhaChessCancelRequestData getGiveCancelRequest() {
        return GiveCancelRequest;
    }

    public void setGiveCancelRequest(LodhaChessCancelRequestData giveCancelRequest) {
        GiveCancelRequest = giveCancelRequest;
    }
}

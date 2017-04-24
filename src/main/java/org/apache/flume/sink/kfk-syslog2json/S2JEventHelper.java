/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.flume.sink.kfksyslog2json;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.apache.commons.io.HexDump;
import org.apache.flume.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;

public class S2JEventHelper {

  private static final String HEXDUMP_OFFSET = "00000000";
  private static final String EOL = System.getProperty("line.separator", "\n");
  private static final int DEFAULT_MAX_BYTES = 16;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(S2JEventHelper.class);

  public static String dumpEvent(Event event) {
    return dumpEvent(event, DEFAULT_MAX_BYTES);
  }

  public static String dumpEvent(Event event, int maxBytes) {
    StringBuilder buffer = new StringBuilder();
    if (event == null || event.getBody() == null) {
      buffer.append("null");
    } else if (event.getBody().length == 0) {
      buffer.append("null");
    } else {
      byte[] body = event.getBody();
      byte[] data = Arrays.copyOf(body, Math.min(body.length, maxBytes));
      buffer.append(new String(data,StandardCharsets.UTF_8));
      String result = buffer.toString();
      if (result.endsWith(EOL) && buffer.length() > EOL.length()) {
        buffer.delete(buffer.length() - EOL.length(), buffer.length()).toString();
      }
    }
    return "{ headers:" + event.getHeaders() + " body:" + buffer + " }";
  }
}

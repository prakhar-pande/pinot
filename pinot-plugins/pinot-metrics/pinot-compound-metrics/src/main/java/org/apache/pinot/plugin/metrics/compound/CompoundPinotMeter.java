/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pinot.plugin.metrics.compound;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.pinot.spi.metrics.PinotMeter;


public class CompoundPinotMeter extends AbstractCompoundPinotMetric<PinotMeter> implements PinotMeter {
  public CompoundPinotMeter(List<PinotMeter> meters) {
    super(meters);
  }

  @Override
  public void mark() {
    for (PinotMeter meter : _metrics) {
      meter.mark();
    }
  }

  @Override
  public void mark(long unitCount) {
    for (PinotMeter meter : _metrics) {
      meter.mark(unitCount);
    }
  }

  @Override
  public long count() {
    return getSomeMeter().count();
  }

  @Override
  public Object getMetered() {
    return getSomeMeter().getMetered();
  }

  @Override
  public TimeUnit rateUnit() {
    return getSomeMeter().rateUnit();
  }

  @Override
  public String eventType() {
    return getSomeMeter().eventType();
  }

  @Override
  public double fifteenMinuteRate() {
    return getSomeMeter().fifteenMinuteRate();
  }

  @Override
  public double fiveMinuteRate() {
    return getSomeMeter().fiveMinuteRate();
  }

  @Override
  public double meanRate() {
    return getSomeMeter().meanRate();
  }

  @Override
  public double oneMinuteRate() {
    return getSomeMeter().oneMinuteRate();
  }
}
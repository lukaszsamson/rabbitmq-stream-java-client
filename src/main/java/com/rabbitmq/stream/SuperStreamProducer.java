// Copyright (c) 2026 Broadcom. All Rights Reserved.
// The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
//
// This software, the RabbitMQ Stream Java client library, is dual-licensed under the
// Mozilla Public License 2.0 ("MPL"), and the Apache License version 2 ("ASL").
// For the MPL, please see LICENSE-MPL-RabbitMQ. For the ASL,
// please see LICENSE-APACHE2.
//
// This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
// either express or implied. See the LICENSE file for specific language governing
// rights and limitations of this software.
//
// If you have any questions regarding licensing, please contact us at
// info@rabbitmq.com.
package com.rabbitmq.stream;

import java.util.Map;

/**
 * API to send messages to a RabbitMQ super stream.
 *
 * <p>A super stream producer is a {@link Producer} backed by several partitions; each partition is
 * an independent stream with its own broker-side deduplication state. Instances are created by
 * {@link ProducerBuilder#superStream(String)} and may be downcast from the {@link Producer}
 * returned by {@link ProducerBuilder#build()}.
 *
 * @see Producer
 * @see ProducerBuilder#superStream(String)
 */
public interface SuperStreamProducer extends Producer {

  /**
   * Get the last publishing ID for a named producer, per partition.
   *
   * <p>Broker-side deduplication state for a named producer is tracked independently on each
   * partition of a super stream. This method returns the last publishing ID recorded by the broker
   * for the producer name on every partition, in routing order. Callers that resume publishing
   * with explicit publishing IDs should pick the entry for the partition the next message will be
   * routed to and use {@code value + 1} as the publishing ID; using any aggregate scalar (such as
   * {@link Producer#getLastPublishingId()}) is unsafe because no single value can describe the
   * state of multiple independently advancing partitions without either losing safety (silent
   * deduplication on partitions whose state is higher) or losing replay (gaps on partitions whose
   * state is lower).
   *
   * @return an unmodifiable map from partition stream name to last publishing ID, in routing order
   * @throws IllegalStateException if the producer has no name
   */
  Map<String, Long> getLastPublishingIds();
}

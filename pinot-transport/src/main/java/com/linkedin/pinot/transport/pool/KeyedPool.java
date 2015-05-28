/**
 * Copyright (C) 2014-2015 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.transport.pool;

import com.linkedin.pinot.transport.common.AsyncResponseFuture;
import com.linkedin.pinot.transport.common.KeyedFuture;
import com.linkedin.pinot.transport.common.NoneType;
import com.linkedin.pinot.transport.metrics.PoolStatsProvider;
import com.yammer.metrics.core.Histogram;


/**
 * Async Pool library.
 *
 * The implementation will be just a wrapper over R2's async pool (AsyncPoolImpl). AsyncPool
 * provides an efficient way to checkout same kind resources. This interface provides an abstraction
 * above AsyncPool to manage different objects ( For e.g : Connection pool for connections keyed by the server
 * identifier. In short, this will be a map of AsyncPool.
 *
 * @param <T>
 */
public interface KeyedPool<K, T> extends PoolStatsProvider<Histogram> {

  /**
   * Start the pool.
   */
  void start();

  /**
   * Get an object from the pool.
   *
   * If a valid object is available, it will be passed to the callback (possibly by the thread
   * that invoked <code>get</code>.
   *
   * The pool will determine if an idle object is valid by calling ResourceManager's
   * <code>validate</code> method.
   *
   * If none is available, the method returns immediately.  If the pool is not yet at
   * max capacity, object creation will be initiated.
   *
   * The resources will be checked-out in FIFO order as objects are returned to the pool (either
   * by other users, or as new object creation completes) or as the timeout expires.
   *
   * After finishing with the object, the user must return the object to the pool with
   * <code>checkinObject</code>.
   *
   * @param key the key identifying the inner pool which manages the resources.
   * @return A {@link AsyncResponseFuture} whose get() method will return the actual resource
   */
  public KeyedFuture<K, T> checkoutObject(K key);

  /**
   * Return a previously checked out object to the pool.  It is an error to return an object to
   * the pool that is not currently checked out from the pool.
   *
   * @param object the object to be returned
   */
  public void checkinObject(K key, T object);

  /**
   * Dispose of a checked out object which is not operating correctly.  It is an error to
   * <code>destroyObject</code> an object which is not currently checked out from the pool.
   *
   * @param key the key identifying the inner pool
   * @param object the object to be disposed
   */
  public void destroyObject(K key, T object);

  /**
   * Initiate an orderly shutdown of the pool.  The pool will immediately stop accepting
   * new get(com.linkedin.common.callback.Callback) requests.  Shutdown is complete when
   * <ul>
   *   <li>No pending requests are waiting for objects</li>
   *   <li>All objects have been returned to the pool, via either put(Object) or dispose(Object)</li>
   * </ul>
   *
   * @return composite Future which you can call get() to wait for shutdown.
   */
  public KeyedFuture<K, NoneType> shutdown();
}

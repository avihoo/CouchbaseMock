/**
 *     Copyright 2011 Couchbase, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.couchbase.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.couchbase.mock.memcached.MemcachedServer;

/**
 * Representation of a CacheBucket (aka memcached)
 *
 * @author Trond Norbye
 */
public class MemcachedBucket extends Bucket {
    public MemcachedBucket(CouchbaseMock cluster, BucketConfiguration config)
            throws IOException
    {
        super(cluster, config);
    }

    @Override
    public String getJSON() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("name", name);
        map.put("authType", "sasl");
        map.put("bucketType", "memcached");

        map.put("flushCacheUri", "/pools/" + poolName + "/buckets/" + name + "/controller/doFlush");
        map.put("nodeLocator", "ketama");
        map.put("proxyPort", 0);
        map.put("replicaNumber", 0);
        map.put("saslPassword", getPassword());
        map.put("streamingUri", "/pools/" + poolName + "/bucketsStreaming/" + name);
        map.put("uri", "/pools/" + poolName + "/buckets/" + name);

        List<String> nodes = new ArrayList<String>();
        for (MemcachedServer server : activeServers()) {
            nodes.add(server.toString());
        }
        map.put("nodes", nodes);

        return JSONObject.fromObject(map).toString();
    }

    @Override
    public BucketType getType() {
        return BucketType.MEMCACHED;
    }
}

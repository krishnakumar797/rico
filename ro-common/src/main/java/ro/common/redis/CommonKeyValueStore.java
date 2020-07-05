/* Licensed under Apache-2.0 */
package ro.common.redis;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import ro.common.utils.Doc;

/**
 * Generic CommonCache class
 *
 * @author r.krishnakumar
 * @param <T>
 */
@DependsOn("RedisConfig")
public abstract class CommonKeyValueStore<T extends Doc> {

  @Resource(name = "redisTemplate")
  private HashOperations<String, byte[], byte[]> hashOperations;

  private HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();

  /**
   * Method to store redis document with key
   *
   * @param key
   * @param redisDoc
   */
  protected final void storeData(String key, T redisDoc) {
    Map<byte[], byte[]> mappedHash = mapper.toHash(redisDoc);
    hashOperations.putAll(key, mappedHash);
  }

  /**
   * Method to retrieve redis document using the key
   *
   * @param key
   * @return
   */
  @SuppressWarnings("unchecked")
  protected final T getData(String key) {
    Map<byte[], byte[]> loadedHash = hashOperations.entries(key);
    return (T) mapper.fromHash(loadedHash);
  }
}

/* Licensed under Apache-2.0 */
package ro.common.kafka;

import com.google.protobuf.ByteString;
import java.util.function.Consumer;
import org.nustaq.serialization.FSTConfiguration;
import ro.common.exception.DataParsingException;

/**
 * Common wrapper for the message objects
 *
 * @author r.krishnakumar
 */
public final class MessageWrapper {

  private Long id;

  private String status;

  private byte[] data;

  private ByteString s;

  private FSTConfiguration conf;

  private MessageWrapper() {}

  void setByteData(byte[] data) {
    this.data = data;
  }

  ByteString getByteString() {
    return s;
  }

  void setFSTConfiguration(FSTConfiguration conf) {
    this.conf = conf;
  }

  static MessageWrapper newInstance() {
    return new MessageWrapper();
  }

  MessageWrapper with(Consumer<MessageWrapper> builderFunction) {
    builderFunction.accept(this);
    return this;
  }

  MessageWrapper build() {
    return this;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Method to return deserialized data
   *
   * @param <T>
   * @param clazz
   * @return
   * @throws DataParsingException
   */
  public <T> T getData(Class<T> clazz) throws DataParsingException {
    T obj = null;
    try {
      obj = clazz.cast(conf.asObject(this.data));
    } catch (Exception e) {
      throw new DataParsingException("Invalid data object", e);
    }
    return obj;
  }

  /**
   * Method to serialize the data object
   *
   * @param obj
   * @throws DataParsingException
   */
  public void setData(Object data) throws DataParsingException {
    try {
      byte[] barray = conf.asByteArray(data);
      this.s = ByteString.copyFrom(barray);
    } catch (Exception e) {
      throw new DataParsingException("Invalid data object", e);
    }
  }
}

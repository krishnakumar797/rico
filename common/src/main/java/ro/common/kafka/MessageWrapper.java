/* Licensed under Apache-2.0 */
package ro.common.kafka;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.google.protobuf.ByteString;
import ro.common.exception.DataParsingException;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

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

  private Kryo conf;

  private MessageWrapper() {}

  void setByteData(byte[] data) {
    this.data = data;
  }

  ByteString getByteString() {
    return s;
  }

  void setKryoConfiguration(Kryo conf) {
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
      Input input = new Input(this.data);
      Object object =  conf.readObject(input, Object.class);
      obj = clazz.cast(object);
    } catch (Exception e) {
      throw new DataParsingException("Invalid data object", e);
    }
    return obj;
  }

  /**
   * Method to serialize the data object
   *
   * @param data
   * @throws DataParsingException
   */
  public void setData(Object data) throws DataParsingException {
    try {
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      Output output = new Output(b);
      conf.writeObject(output, data);
      byte[] barray = output.toBytes();
      this.s = ByteString.copyFrom(barray);
    } catch (Exception e) {
      throw new DataParsingException("Invalid data object", e);
    }
  }
}

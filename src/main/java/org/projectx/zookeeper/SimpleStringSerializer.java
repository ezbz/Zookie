package org.projectx.zookeeper;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class SimpleStringSerializer implements ZkSerializer {
  private Charset charset = Charset.forName("UTF-8");

  public void setEncoding(final String encoding) {
    charset = Charset.forName(encoding);
  }

  @Override
  public byte[] serialize(final Object data) throws ZkMarshallingError {
    if (null == data) {
      return null;
    }
    return ByteBuffer.wrap(data.toString().getBytes(charset)).array();
  }

  @Override
  public Object deserialize(final byte[] bytes) throws ZkMarshallingError {
    if (null == bytes) {
      return null;
    }
    return charset.decode(ByteBuffer.wrap(bytes)).toString();
  }

}

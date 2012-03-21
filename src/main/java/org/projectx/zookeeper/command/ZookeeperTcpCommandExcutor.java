package org.projectx.zookeeper.command;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class ZookeeperTcpCommandExcutor {

  @SuppressWarnings("unchecked")
  public List<String> execute(final String host, final Integer port, final String command) {
    Socket socket;
    try {
      socket = new Socket(InetAddress.getByName(host), port);
    } catch (final UnknownHostException e) {
      throw new IllegalStateException("could not connect to host: " + host + " and port: " + port, e);
    } catch (final IOException e) {
      throw new IllegalStateException("could not connect to host: " + host + " and port: " + port, e);
    }

    try {
      IOUtils.write(command + "\n", socket.getOutputStream());
    } catch (final IOException e) {
      throw new IllegalStateException("could not execute command to host: " + host + " and port: " + port + ", command: " + command, e);
    }

    try {
      return IOUtils.readLines(socket.getInputStream());
    } catch (final IOException e) {
      throw new IllegalStateException("could not connect to host: " + host + " and port: " + port, e);
    } finally {
      try {
        socket.close();
      } catch (final IOException e) {
        throw new IllegalStateException("Error disconnecting from host", e);
      }
    }

  }
}

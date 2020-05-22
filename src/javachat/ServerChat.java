/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javachat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/** @author volkangurbuz */
public class ServerChat implements Serializable {

  private static ServerSocket serverSocket = null;
  private static Socket clientSocket = null;
  // max connection client
  private static final int maxClient = 2;
  // thread array for all clients
  private static final ClientThread[] threads = new ClientThread[maxClient];
  private static ObjectInputStream objectInputStream;

  public static void main(String args[]) {

    int portNo = 3333;
    if (args.length < 1) {
      System.out.println("Port number is: " + portNo);
    } else {
      portNo = Integer.valueOf(args[0]).intValue();
    }

    try {
      serverSocket = new ServerSocket(portNo);
    } catch (IOException e) {
      System.out.println(e);
    }

    // creating sockets and thread for each of the clients

    while (true) {
      try {
        clientSocket = serverSocket.accept();
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        PlayerImpl player = (PlayerImpl) objectInputStream.readObject();
        int indexOfThread = 0;

        for (; indexOfThread < maxClient; indexOfThread++) {

          if (threads[indexOfThread] == null) {
            ClientThread clientThread =
                new ClientThread(clientSocket, threads, player, indexOfThread);

            (threads[indexOfThread] = clientThread).start();

            break;
          }
        }

        if (indexOfThread == maxClient) {
          PrintStream ps = new PrintStream(clientSocket.getOutputStream());
          ps.println("server is already max");
          ps.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}

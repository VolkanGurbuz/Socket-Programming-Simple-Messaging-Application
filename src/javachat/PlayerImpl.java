package javachat;

import javachat.api.ChatInterface;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlayerImpl implements ChatInterface, Runnable, Serializable {

  String playerName;
  private String serverName;
  private int port;
  private static Socket clientSocket = null;
  private static PrintStream ps = null;
  private static BufferedReader d;
  private static BufferedReader giris = null;
  private static boolean isClose = false;
  private static ObjectOutputStream objectInputStream;

  public PlayerImpl(String playerName, String serverName, int port, boolean isGameStarter) {
    this.playerName = playerName;
    this.serverName = serverName;
    this.port = port;
  }

  public PlayerImpl() {}

  @Override
  public void joinChannel() {
    try { // if connection is success do a messaging
      clientSocket = new Socket(serverName, port);
      giris = new BufferedReader(new InputStreamReader(System.in));
      ps = new PrintStream(clientSocket.getOutputStream());
      d = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
      objectInputStream = new ObjectOutputStream(clientSocket.getOutputStream());

      System.out.println("Connection is success.");
    } catch (UnknownHostException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println("error");
    }

    if (clientSocket != null && ps != null && d != null) {
      try {

        // read thread from server
        new Thread(new PlayerImpl()).start();
        while (!isClose) {
          objectInputStream.writeObject(this);
          ps.println(giris.readLine().trim());
        }

        ps.close();
        d.close();
        clientSocket.close();
      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      }
    }
  }

  @Override
  public void run() {
    String answer;
    try {
      while ((answer = d.readLine()) != null) {
        System.out.println(answer);
        if (answer.indexOf("Bye bye") != -1) {
          break;
        }
      }
      isClose = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }
}

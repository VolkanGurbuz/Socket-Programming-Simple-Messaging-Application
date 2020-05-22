package javachat;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/*
 * Thread class which created for Client sockets
 *
 */
class ClientThread extends Thread {

  private BufferedReader bufferedReader;
  private PrintStream printStream = null;
  private Socket clientSocket;
  private final ClientThread[] threads;
  private int maxClientSize;
  private PlayerImpl player;
  private int indexOfThread;
  private boolean isStartGame = false;

  public ClientThread(
      Socket clientSocket, ClientThread[] threads, PlayerImpl player, int indexOfThread) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    this.maxClientSize = threads.length;
    this.player = player;
    this.indexOfThread = indexOfThread;
  }

  @Override
  public void run() {
    ClientThread[] threads = this.threads;

    try {
      printStream = new PrintStream(clientSocket.getOutputStream());
      bufferedReader =
          new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
      String name = player.playerName;

      printStream.println("Hi " + name + "! welcome to chat game. ");

      for (int currentIndex = 0; currentIndex < maxClientSize; currentIndex++) {
        if (threads[currentIndex] != null && currentIndex != indexOfThread) {
          threads[currentIndex].printStream.println(name + " is online to start the game.");
        }
      }

      while (true) {

        String message = bufferedReader.readLine().trim();
        if (message.startsWith("/quit")) {
          break;
        }

        for (int i = 0; i < maxClientSize; i++) {
          if (threads[i] != null && i != indexOfThread) {

            threads[i].printStream.println("<" + name + ">: " + message);
          }
        }
      }

      stopGame();

    } catch (IOException e) {
    }
  }

  public void stopGame() throws IOException {

    printStream.println("Game finished, ciao!");
    isStartGame = false;
    bufferedReader.close();
    printStream.close();
    clientSocket.close();
  }

  private boolean isGameStart() {
    isStartGame = !Arrays.asList(threads).subList(0, maxClientSize).contains(null);
    return isStartGame;
  }
}

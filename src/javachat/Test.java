package javachat;

public class Test {

  public static void main(String[] args) {

    PlayerImpl initiatorPlayer = new PlayerImpl("Client1", "localhost", 3333, true);
    initiatorPlayer.joinChannel();
  }
}

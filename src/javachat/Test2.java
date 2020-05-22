package javachat;

public class Test2 {

  public static void main(String[] args) {

    PlayerImpl player = new PlayerImpl("Client2", "localhost", 3333, false);

    player.joinChannel();
  }
}

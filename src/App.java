import com.main.server.SimpleWebServer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author santanubaruah
 *
 * Main class to the Application Entry
 */
public class App {

  private static Logger LOGGER = Logger.getLogger(App.class.getName());

  {
    LOGGER.setLevel(Level.INFO);
  }

  private static int serverRunningTime = 60 * 1000; //1 minute

  public static void main(String[] args) throws Exception {
    //Starts the server at 9001 port ( customizable via args/config if needed )
    SimpleWebServer server = new SimpleWebServer(9001);

    if(null == server) {
      LOGGER.warning("Server could not be created");
      System.exit(1); // Unsuccessful termination
    }

    new Thread(server).start(); // Signals the server Runnable to start

    //Should be handled by shutdown hooks etc.
    try {
      Thread.sleep(serverRunningTime); //1 minute
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Stopping Server");
    server.stop();
    System.exit(0); // Successful termination
  }
}

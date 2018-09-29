package com.main.server;

import com.main.utilities.ServerUtility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author santanubaruah
 *
 * Implementation of a simple Multithreaded, Pooled web server
 */
public class SimpleWebServer implements Runnable  {
  private static Logger LOGGER = Logger.getLogger(SimpleWebServer.class.getName());

  {
    LOGGER.setLevel(Level.INFO);
  }

  private static final int DEFAULT_THREAD_POOL_SIZE = 10;
  private static final int DEFAULT_PORT = 4502;

  private int port;
  private ServerSocket serverSocket = null;
  private boolean isServerRunning = true; //When the thread is running, first the server will be assumed to be running
  private Thread currentThread = null;
  private ExecutorService threadPool; //For maintaining a Thread pool of limited size

  /**
   * Sets default port and default thread pool size
   */
  public SimpleWebServer() {
    this.setPort(DEFAULT_PORT);
    threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    LOGGER.info("Server will be starting at " + this.getPort() + " with threads : " + DEFAULT_THREAD_POOL_SIZE );
  }

  /**
   * Sets custom port number and default thread pool size
   * @param port
   */
  public SimpleWebServer(int port) {
    this.setPort(port);
    threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    LOGGER.info("Server will be starting at " + this.getPort() + " with threads : " + DEFAULT_THREAD_POOL_SIZE );
  }

  /**
   * Sets custom port number and custom thread pool size
   * @param port
   * @param threadPoolSize
   */
  public SimpleWebServer(int port, int threadPoolSize) {
    this.setPort(port);
    threadPool = Executors.newFixedThreadPool(threadPoolSize);
    LOGGER.info("Server will be starting at " + this.getPort() + " with threads : " + threadPoolSize );
  }

  @Override
  public void run() {
    synchronized (this) {
      this.currentThread = Thread.currentThread();
      LOGGER.info("Current thread for the server is : " + this.getCurrentThread().getName());
    }

    try {
      ServerUtility.createServerSocket(this); //Creates a ServerSocket instance for the server
    } catch (IOException e) {
      LOGGER.severe("Something went wrong while trying to set the ServerSocket instance " +
          "for the server at port " + this.getPort());
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    while(this.isServerRunning()) {
      Socket clientSocket; // To accept client connections

      try {
        if(null != this.getServerSocket()) {
          /*Listens for a connection to be made to the client socket and accepts
          it. The method blocks until a connection is made.*/
          clientSocket = this.getServerSocket().accept();
          if(null != clientSocket) {
            if(null != this.getThreadPool()) {
              LOGGER.info("Executing the server request");
              /*Every new client connection is executed in one of the many available
              threads in the pool using a ServerHandler object*/
              this.getThreadPool().execute(new ServerHandler(clientSocket));
            } else {
              LOGGER.warning("Thread pool is null");
            }
          } else {
            LOGGER.warning("clientSocket is null");
          }
        } else {
          LOGGER.warning("ServerSocket is null");
        }
      } catch(IOException e) {
        if(!isServerRunning()) {
          LOGGER.info("Served has been stopped");
          System.out.println("Server is not running anymore") ;
          break; //Breaks out of outer while loop
        }
        throw new RuntimeException("Server encountered an error",e);
      }
    } // End of while loop - Server isRunning is false

    //Shuts down the thread pool
    if(!this.getThreadPool().isShutdown()) {
      LOGGER.info("Shutting down the thread pool for a graceful " +
          "completion of the previously submitted tasks");
      this.getThreadPool().shutdown();
      LOGGER.info("Server stopped");
    } else {
      LOGGER.warning("Thread pool was shutdown already");
      System.exit(-1);
    }

    System.out.println("Server has been stopped at port : " + this.getPort());
  } //End of Server's lifecycle (Runnable::run)

  public synchronized void stop(){
    this.setServerRunning(false);
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      LOGGER.warning("Error while trying to close server");
      throw new RuntimeException("Error while trying to closing", e);
    }
  }

  //Getters and Setters
  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public ServerSocket getServerSocket() {
    return serverSocket;
  }

  public void setServerSocket(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public boolean isServerRunning() {
    return isServerRunning;
  }

  public void setServerRunning(boolean serverRunning) {
    isServerRunning = serverRunning;
  }

  public Thread getCurrentThread() {
    return currentThread;
  }

  public ExecutorService getThreadPool() {
    return threadPool;
  }
}

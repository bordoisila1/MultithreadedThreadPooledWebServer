package com.main.server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleThreadedServer implements Runnable{

  private static Logger LOGGER = Logger.getLogger(SingleThreadedServer.class.getName());

  {
    LOGGER.setLevel(Level.INFO);
  }

  private int port;
  private ServerSocket socket = null;
  private  Thread currThread = null;
  private boolean isRunning = true;

  public SingleThreadedServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    LOGGER.info("Thread execution has started");
    synchronized (this) {
      this.currThread = Thread.currentThread();
    }

    //Creates a new ServerSocket instance and assigns it to this.socket
    try {
      LOGGER.info("Trying to create a ServerSocket instance");
      this.setSocket(new ServerSocket(this.getPort()));
      if(null != this.getSocket()) {
        LOGGER.info("ServerSocket created");
      }
    } catch(IOException e) {
      LOGGER.warning("Port " + port + " could not be obtained for the  server");
      e.printStackTrace();
    }

    while (isRunning()) {
      //Open a Client Socket
      Socket clientSocket = null;
      try {
        LOGGER.info("Trying to obtain the client Socket");
        clientSocket = this.socket.accept();
        if(null != clientSocket) {
          LOGGER.info("client Socket accepted");
        }
      } catch(IOException e) {
        if(isRunning()) {
          LOGGER.warning("Server has been stopped");
          return;}
          LOGGER.warning("Could not accept Client Connection");
          e.printStackTrace();
      }
      try {
        handleClientRequest(clientSocket);
      } catch(Exception e) {
        LOGGER.warning("Exception while handling Client Request");
        e.printStackTrace();
      }

    }
  }

  public static void handleClientRequest(Socket clientSocket) throws Exception{
    LOGGER.info("Trying to handle client request");
    //Client Socket is handled in hte desired way
    InputStream inputStream = clientSocket.getInputStream();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    OutputStream outputStream = clientSocket.getOutputStream();
    long time = System.currentTimeMillis();

    String input = bufferedReader.readLine();
    StringTokenizer parse = new StringTokenizer(input);
    LOGGER.info("*****INPUT*****");
    LOGGER.info(input);
    LOGGER.info("*****Parsed Token******");
    while (parse.hasMoreTokens()) {
     LOGGER.info("\n" + parse.nextToken());
    }


    String responseDocument = "<html><body>" +
        "Singlethreaded Server: " +
        time +
        "</body></html>";

    byte[] responseDocumentBytes = responseDocument.getBytes();

    String responseHeader =
        "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: " + responseDocument.length() +
            "\r\n\r\n";

    byte[] responseHeaderBytes = responseHeader.getBytes("UTF-8");

    outputStream.write(responseHeaderBytes);
    outputStream.write(responseDocumentBytes);
    outputStream.close();
    inputStream.close();
    LOGGER.info("Client request handling is complete");
  }

  private synchronized boolean isRunning() {
    return this.isRunning;
  }

  public synchronized void stop(){
    this.isRunning = false;
    try {
      this.socket.close();
    } catch (IOException e) {
      throw new RuntimeException("Error closing server", e);
    }
  }

  public void setSocket(ServerSocket socket) {
    this.socket = socket;
  }

  public int getPort() {
    return this.port;
  }

  public ServerSocket getSocket() {
    return socket;
  }
}

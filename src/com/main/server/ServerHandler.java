package com.main.server;

import com.main.utilities.ServerUtility;
import com.main.utilities.ServerUtility.HttpResponseHeader;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author santanubaruah
 *
 * Does everything required to handle a Client request
 */
public class ServerHandler implements Runnable  {
  private static Logger LOGGER = Logger.getLogger(ServerHandler.class.getName());

  {
    LOGGER.setLevel(Level.INFO);
  }

  /**
   * Contains every necessary info on the client request
   */
  protected Socket clientSocket;

  public ServerHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    try {
      this.handleClientRequest(clientSocket);
    } catch (Exception e) {
      LOGGER.severe("Server encountered an Error while handling clientSocket");
      System.exit(-1);
    }
  }

  /**
   * Does everything for handling the client request
   *
   * @param clientSocket
   * @throws Exception
   */
  private void handleClientRequest(Socket clientSocket) throws Exception {
    LOGGER.info("Trying to handle client request");

    /**
     * Default responseMessage
     */
    String responseMessage = HttpResponseHeader.STATUS_CODE_500
        + " : "
        + HttpResponseHeader.STATUS_MESSAGE_INTERNAL_SERVER_ERROR;

    /**
     * Default responseHeader
     */
    String responseHeader = new HttpResponseHeader()
        .buildResponseHeader(HttpResponseHeader.STATUS_CODE_500, responseMessage.length());

    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
    OutputStream outputStream = clientSocket.getOutputStream();
    try {
      if (null != bufferedReader) {

        // First line contains Request METHOD and Path. Sufficient for this use case
        final String input = bufferedReader.readLine();

        final String requestMethod = ServerUtility.getRequestMethod(input);
        LOGGER.info("Request method is : " + requestMethod);

        final String requestPath = ServerUtility.getRequestPath(input);
        LOGGER.info("Request path is : " + requestPath);

        //Handles GET
        if (null != requestMethod
            && requestMethod.equalsIgnoreCase("get")) {
          LOGGER.info("Trying to handle a GET request for the path : " + requestPath);

          File file = new File(requestPath);

          LOGGER.info("****Current path is****");

          if (ServerUtility.resourceExists(requestPath)) { // Handles 200

            responseMessage = ServerUtility.readFileContents(requestPath);//TODO - Fetch File Data - Default - Fallback to index.html data

            //Prepares response header
            responseHeader = new HttpResponseHeader()
                .buildResponseHeader(HttpResponseHeader.STATUS_CODE_200, responseMessage.length());
          } else { // Handles 404
            LOGGER.info("Resource not found at path : " + requestPath);

            responseMessage = HttpResponseHeader.STATUS_CODE_404
                + " : "
                + HttpResponseHeader.STATUS_MESSAGE_NOT_FOUND;

            //Prepares response header
            responseHeader = new HttpResponseHeader()
                .buildResponseHeader(HttpResponseHeader.STATUS_CODE_404, responseMessage.length());
          }
        } else if (requestMethod != null
            && !requestMethod.equalsIgnoreCase("get")) { // Handles 405
          LOGGER.warning(requestMethod + HttpResponseHeader.STATUS_MESSAGE_METHOD_NOT_ALLOWED);

          responseMessage = HttpResponseHeader.STATUS_CODE_405
              + " : "
              + HttpResponseHeader.STATUS_MESSAGE_METHOD_NOT_ALLOWED;

          //Prepares response header
          responseHeader = new HttpResponseHeader()
              .buildResponseHeader(HttpResponseHeader.STATUS_CODE_405, responseMessage.length());
        } else { // Handles 500
          throw new Exception
              ("Something went wrong during handling the client request with Request method :" + requestMethod );
        }
      } else { // Handles 422
        LOGGER.warning("Request data can't be read");

        responseMessage = HttpResponseHeader.STATUS_CODE_422
            + " : "
            + HttpResponseHeader.STATUS_MESSAGE_UNPROCESSABLE_ENTITY;

        //Prepares response header
        responseHeader = new HttpResponseHeader()
            .buildResponseHeader(HttpResponseHeader.STATUS_CODE_422, responseMessage.length());
      }
    } catch(Exception e) { //Handles 500 from above
      //Sends default response header and response message
      e.printStackTrace();
    } finally {
      LOGGER.info("Writing response to the OutputStream");

      //Write the message to the OutputStream
      outputStream.write(responseHeader.getBytes());
      outputStream.write(responseMessage.getBytes());

      //Close the OutputStream
      outputStream.close();
    }
  } // End of method ServerHandler::handleClientRequest
}

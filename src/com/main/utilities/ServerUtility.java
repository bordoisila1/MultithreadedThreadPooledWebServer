package com.main.utilities;

import com.main.server.SimpleWebServer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author santanubaruah
 *
 * Utility class to perform tasks related to the server
 */
public class ServerUtility {
  private static Logger LOGGER = Logger.getLogger(ServerUtility.class.getName());

  {
    LOGGER.setLevel(Level.INFO);
  }

  /**
   * For the sake of simplicity, this program relies on the /<PROJECT_DIR>/src/resources directory for assets
   */
  private static final String DEFAULT_RESOURCES_PATH = System.getProperty("user.dir") + "/src/resources";

  /**
   * Sets the serverSocket attribute of the SimpleWebServer instance
   *
   * @param server
   * @throws IOException
   */
  public static void createServerSocket(SimpleWebServer server) throws IOException {

    LOGGER.info("Creating ServerSocket instance");

    if(null != server) {
      server.setServerSocket(new ServerSocket(server.getPort()));
      return;
    }

    LOGGER.warning("Something went wrong with the SimpleWebServer");
    System.exit(-1);
  }

  /**
   * Returns the request METHOD. Currently only GET and HEAD are supported
   *
   * @param input
   * @return
   */
  public static String getRequestMethod(String input) {
    String method = null;
    if(null != input) {
      method = input.split(" ")[0]; // Keeping it simple
    }
    return method;
  }

  /**
   * Returns the request path. No assumptions made about the Extension
   *
   * @param input
   * @return
   */
  public static String getRequestPath(String input) {
    String requestPath = null;
    if(null != input) {
      requestPath = input.split(" ")[1]; // Keeping it simple
    }
    return requestPath;
  }

  /**
   * Simple utility method to tell if the resource exists in the given path
   *
   * @param filePath
   * @return
   */
  public static boolean resourceExists(String filePath) {
    boolean resourceDoesExist = false;

    File file = new File(DEFAULT_RESOURCES_PATH + filePath);

    if(null != file && file.isFile()) {
      resourceDoesExist = true;
    }

    return resourceDoesExist;
  }

  /**
   *
   * @param filePath
   * @return
   */
  public static String readFileContents(String filePath) throws Exception{
    filePath = DEFAULT_RESOURCES_PATH + filePath;
    String contents = new String(Files.readAllBytes(Paths.get(filePath)), HttpResponseHeader.DEFAULT_CHARSET);
    return contents;
  }

  /**
   * Class to have a mini HttpResponseHeader object
   */
  public static class HttpResponseHeader {
    public static final String CHARSET = "charset";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_CONTENT_TYPE = "text/html";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String PROTOCOL_NAME = "HTTP";
    public static final String PROTOCOL_VERSION = "1.1";
    public static final int STATUS_CODE_200 = 200;
    public static final int STATUS_CODE_404 = 404;
    public static final int STATUS_CODE_405 = 405;
    public static final int STATUS_CODE_422 = 422;
    public static final int STATUS_CODE_500 = 500;
    public static final String STATUS_MESSAGE_OK = "OK";
    public static final String STATUS_MESSAGE_METHOD_NOT_ALLOWED = "METHOD NOT ALLOWED";
    public static final String STATUS_MESSAGE_NOT_FOUND = "NOT FOUND";
    public static final String STATUS_MESSAGE_INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
    public static final String STATUS_MESSAGE_UNPROCESSABLE_ENTITY = "UNPROCESSABLE_ENTITY";

    /**
     * Builds a response header String
     *
     * @return
     */
    public String buildResponseHeader(int statusCode, int contentLength) {
      String responseHeader;

      switch (statusCode) {
        case STATUS_CODE_200 :
          responseHeader = PROTOCOL_NAME + "/"
              + PROTOCOL_VERSION + " "
              + STATUS_CODE_200 + " "
              + STATUS_MESSAGE_OK + "\r\n"
              + CONTENT_TYPE + ": "
              + DEFAULT_CONTENT_TYPE + " "
              + CHARSET + "=" + DEFAULT_CHARSET + "\r\n"
              + CONTENT_LENGTH + ": " + contentLength + "\r\n\r\n";
          break;
        case STATUS_CODE_404 :
          responseHeader = PROTOCOL_NAME + "/"
              + PROTOCOL_VERSION + " "
              + STATUS_CODE_404 + " "
              + STATUS_MESSAGE_OK + "\r\n"
              + CONTENT_TYPE + ": "
              + DEFAULT_CONTENT_TYPE + " "
              + CHARSET + "=" + DEFAULT_CHARSET + "\r\n"
              + CONTENT_LENGTH + ": " + contentLength + "\r\n\r\n";
          break;
        case STATUS_CODE_405:
          responseHeader = PROTOCOL_NAME + "/"
              + PROTOCOL_VERSION + " "
              + STATUS_CODE_405 + " "
              + STATUS_MESSAGE_METHOD_NOT_ALLOWED + "\r\n"
              + CONTENT_TYPE + ": "
              + DEFAULT_CONTENT_TYPE + " "
              + CHARSET + "=" + DEFAULT_CHARSET + "\r\n"
              + CONTENT_LENGTH + ": " + contentLength + "\r\n\r\n";
          break;
        case STATUS_CODE_422:
          responseHeader = PROTOCOL_NAME + "/"
              + PROTOCOL_VERSION + " "
              + STATUS_CODE_422 + " "
              + STATUS_MESSAGE_UNPROCESSABLE_ENTITY + "\r\n"
              + CONTENT_TYPE + ": "
              + DEFAULT_CONTENT_TYPE + " "
              + CHARSET + "=" + DEFAULT_CHARSET + "\r\n"
              + CONTENT_LENGTH + ": " + contentLength + "\r\n\r\n";
          break;
        default :
          responseHeader = PROTOCOL_NAME + "/"
              + PROTOCOL_VERSION + " "
              + STATUS_CODE_500 + " "
              + STATUS_MESSAGE_INTERNAL_SERVER_ERROR + "\r\n"
              + CONTENT_TYPE + ": "
              + DEFAULT_CONTENT_TYPE + " "
              + CHARSET + "=" + DEFAULT_CHARSET + "\r\n"
              + CONTENT_LENGTH + ": " + contentLength + "\r\n\r\n";
      }
      return responseHeader;
    }
  }
}

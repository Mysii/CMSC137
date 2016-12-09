/*
DE BELEN, Azha Vianca de Belen
2012-02593
CMSC 137 B-2L
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server {
    private static final int port = 1372;   //port to be used
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(port); // Initialize server socket
            System.out.println("Azha Vianca de Belen");
            System.out.println("2012-02593");
            System.out.println("CMSC 137 B-2L");
            System.out.println("\n\n");
            System.out.println("Server running on port: " + port);
            
            while (true) {
                new ThreadSocket(server.accept());
            }
        } catch (Exception e) {
        }
    }
}
class ThreadSocket extends Thread {
    private Socket socket;
    ThreadSocket(Socket socket) {
        this.socket = socket;
        this.start();
    }
    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processRequest() throws Exception {
        
            InputStream is = socket.getInputStream();
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            line = in.readLine();
            String request_method = line;
            System.out.println("HTTP-HEADER: " + line);
            //System.out.println(line);
            
            String headerLine = line;               
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();
            
            System.out.println(httpMethod);
            
            line = "";
            // looks for post data
            int postDataI = -1;
            
            int i = 0;
            
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                
                if (line.indexOf("Content-Type: multipart/form-data") != -1) {
                    System.out.println("XXX");
                    
                    String boundary = line.split("boundary=")[1];
                  // The POST boundary                       
                  
                  while (true) {
                    line = in.readLine();
                    if (line.indexOf("Content-Length:") != -1) {
                        String contentLength = line.split(" ")[1];
                        System.out.println("Content Length = " + contentLength);
                        break;
                    }                   
                  }
                    
                }else{
                    
                }
                
                System.out.println("HTTP-HEADER: " + line);
                if (line.indexOf("Content-Length:") > -1) {
                    postDataI = new Integer(
                            line.substring(
                                    line.indexOf("Content-Length:") + 16,
                                    line.length())).intValue();
                }
            }
            String postData = "";
            // read the post data
            if (postDataI > 0) {
                char[] charArray = new char[postDataI];
                in.read(charArray, 0, postDataI);
                postData = new String(charArray);
            }
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html; charset=utf-8");
            out.println("Server: MINISERVER");
            // this blank line signals the end of the headers
            out.println("");
            // Send the HTML page
            
            out.println("<H2>Request Method->" + request_method + "</H2>");
            //out.println("<H2>Post->" + postData + "</H2>");
            //out.println("<form name=\"input\" action=\"form_submited\" enctype=\"multipart/form-data\" method=\"post\">");
            //out.println("<form name=\"input\" action=\"form_submited\" method=\"post\">");
            
            //out.println("Enter the name of the File <input name=\"file\" type=\"file\"><br>");
            //out.println("Username: <input type=\"text\" name=\"user\"><input type=\"submit\" value=\"Submit\"></form>");
            out.close();
            socket.close();
        
    }


}

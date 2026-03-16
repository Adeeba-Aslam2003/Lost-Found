import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class Server {

    static List<String> items = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/items", new ItemHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:8080");
    }

    static class ItemHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String response = "";

            if (method.equals("GET")) {
                response = items.toString();
            }

          else if (method.equals("POST")) {

    InputStream inputStream = exchange.getRequestBody();
    Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
    String item = scanner.hasNext() ? scanner.next() : "";

    items.add(item);

    response = "Item added: " + item;
}

            else if (method.equals("DELETE")) {

                if (!items.isEmpty()) {
                    String removed = items.remove(0);
                    response = "Item removed: " + removed;
                } else {
                    response = "No items to delete";
                }
            }

            exchange.sendResponseHeaders(200, response.length());

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
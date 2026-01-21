import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class SimpleServer {

    private static final String FRONTEND_DIR = "frontend";

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/", SimpleServer::serveStatic);
            server.createContext("/addItem", SimpleServer::addItem);
            server.createContext("/consume", SimpleServer::consumeItem);
            server.createContext("/logWaste", SimpleServer::logWaste);

            server.start();
            System.out.println("✅ Website running at http://localhost:8080");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- SERVE HTML / CSS / JS ----------------
    private static void serveStatic(HttpExchange exchange) throws IOException {
        addCORS(exchange);

        String uri = exchange.getRequestURI().getPath();
        if (uri.equals("/")) uri = "/index.html";

        Path filePath = Path.of(FRONTEND_DIR + uri);

        if (!Files.exists(filePath)) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String contentType = getContentType(uri);
        exchange.getResponseHeaders().add("Content-Type", contentType);

        byte[] bytes = Files.readAllBytes(filePath);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    // ---------------- ADD ITEM ----------------
    private static void addItem(HttpExchange exchange) {
        try {
            addCORS(exchange);
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) return;

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String name = getValue(body, "name");
            int qty = Integer.parseInt(getValue(body, "quantity"));
            LocalDate expiry = LocalDate.parse(getValue(body, "expiry"));

            new InventoryService().addItem(name, qty, expiry);
            respond(exchange, "Item added successfully");

        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }

    // ---------------- CONSUME ----------------
    private static void consumeItem(HttpExchange exchange) {
        try {
            addCORS(exchange);
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) return;

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String name = getValue(body, "name");
            int qty = Integer.parseInt(getValue(body, "quantity"));

            new InventoryService().consumeItem(name, qty);
            new UsageService().logUsage(name, qty);

            respond(exchange, "Food consumed using FIFO");

        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }

    // ---------------- WASTE ----------------
    private static void logWaste(HttpExchange exchange) {
        try {
            addCORS(exchange);
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) return;

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String name = getValue(body, "name");
            int qty = Integer.parseInt(getValue(body, "quantity"));
            String reason = getValue(body, "reason");

            new WasteService().logWaste(name, qty, reason);
            respond(exchange, "Waste logged");

        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }

    // ---------------- HELPERS ----------------
    private static void respond(HttpExchange exchange, String msg) throws IOException {
        exchange.sendResponseHeaders(200, msg.length());
        exchange.getResponseBody().write(msg.getBytes());
        exchange.close();
    }

    private static void sendError(HttpExchange exchange, String error) {
        try {
            exchange.sendResponseHeaders(500, error.length());
            exchange.getResponseBody().write(error.getBytes());
            exchange.close();
        } catch (Exception ignored) {}
    }

    private static void addCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    }

    private static String getContentType(String path) {
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        return "text/html";
    }

    private static String getValue(String json, String key) {
        return json.split(key + "\":\"")[1].split("\"")[0];
    }
}

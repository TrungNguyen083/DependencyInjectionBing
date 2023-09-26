package org.example.restapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.dilibrary.MyContainer;
import org.example.restapi.annotation.GetMapping;
import org.example.restapi.annotation.PathVariable;
import org.example.restapi.annotation.PostMapping;
import org.example.restapi.annotation.RequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.restapi.untils.ResponseUtils.sendJsonResponse;


public class ControllerManager {
    private final HttpServer server;
    private final Map<String, Method> uriToMethodMap = new HashMap<>();
    private final Map<String, Class<?>> methodParameterMap = new HashMap<>(); // Declare methodParameterMap
    private Object controllerInstance;
    private final MyContainer container;

    public ControllerManager(int port) throws Exception {
        this.server = HttpServer.create();
        this.server.bind(new InetSocketAddress(port), 0);
        container = new MyContainer();
        // Scan and register controller classes in a specific base package
        container.scanAndRegisterControllers();
        this.controllerInstance = null;
    }

    public void start() {
        server.setExecutor(null); // Use default executor
        server.start();
    }

    public void addController(Class<?> controller) {
        controllerInstance = container.getBean(controller);
        Stream.of(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class))
                .forEach(method -> {
                    GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
                    PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
                    if (getAnnotation != null || postAnnotation != null) {
                        String value = getAnnotation != null ? getAnnotation.value() : postAnnotation.value();
                        uriToMethodMap.put(value, method);

                        // Extract parameter type from @PathVariable annotation
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Arrays.stream(method.getParameterAnnotations())
                                .flatMap(Arrays::stream)
                                .filter(annotationItem -> annotationItem instanceof PathVariable)
                                .map(annotationItem -> (PathVariable) annotationItem)
                                .filter(pathVariable -> value.contains("{" + pathVariable.value() + "}") && parameterTypes.length == 1)
                                .findFirst()
                                .ifPresent(pathVariable -> methodParameterMap.put(value, parameterTypes[0]));

                        server.createContext(value, new CustomHttpHandler());
                    }
                });
    }

    private class CustomHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String patternPath = transformToPattern(path);
            Method method = uriToMethodMap.get(patternPath);

            if (method != null) {
                try {
                    Class<?> parameterType = methodParameterMap.get(patternPath);
                    if(exchange.getRequestMethod().equalsIgnoreCase("POST")){
                        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                                .lines()
                                .collect(Collectors.joining("\n"));
                        if (hasRequestBodyAnnotation(method)) {
                            String response = (String) method.invoke(controllerInstance, requestBody);
                            sendJsonResponse(exchange, 200, response);
                        }
                    }
                    else if(exchange.getRequestMethod().equalsIgnoreCase("GET")){
                        if (parameterType != null) {
                            String id = extractIdFromPath(path);
                            String response = (String) method.invoke(controllerInstance, id);
                            sendJsonResponse(exchange, 200, response);
                        } else {
                            String response = (String) method.invoke(controllerInstance);
                            sendJsonResponse(exchange, 200, response);
                        }
                    }
                } catch (Exception e) {
                    sendJsonResponse(exchange, 500, "Internal Server Error");
                    throw new RuntimeException(e);
                }
            } else {
                sendJsonResponse(exchange, 404, "Method Not Found");
            }
        }

        private String extractIdFromPath(String path) {
            String[] segments = path.split("/");
            return segments[segments.length - 1];
        }
    }

    private static String transformToPattern(String inputPath) {
        String[] inputPathArray = inputPath.split("/");
        if(inputPathArray.length > 2){
            inputPathArray[inputPathArray.length - 1] = "{guid}";
        }
        return String.join("/", inputPathArray);
    }

    public boolean hasRequestBodyAnnotation(Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        for (Annotation[] annotation : annotations) {
            for (Annotation annotationItem : annotation) {
                if (annotationItem instanceof RequestBody) {
                    return true;
                }
            }
        }
        return false;
    }
}

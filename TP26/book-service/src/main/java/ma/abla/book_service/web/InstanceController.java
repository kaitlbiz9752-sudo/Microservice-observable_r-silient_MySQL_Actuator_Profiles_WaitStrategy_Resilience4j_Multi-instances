package ma.abla.book_service.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/api/debug/instance")
    public String getInstanceInfo() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "local");
        StringBuilder response = new StringBuilder();
        response.append("instance=").append(hostname);
        response.append(" internalPort=").append(serverPort);
        return response.toString();
    }
}
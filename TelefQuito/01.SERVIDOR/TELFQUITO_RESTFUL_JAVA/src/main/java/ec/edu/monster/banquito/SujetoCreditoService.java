/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package ec.edu.monster.banquito;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:WSSujetoCredito
 * [sujeto-credito]<br>
 * USAGE:
 * <pre>
 *        SujetoCreditoService client = new SujetoCreditoService();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author danie
 */
public class SujetoCreditoService {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/BANQUITO_RESTFUL_JAVA/resources";

    public SujetoCreditoService() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("sujeto-credito");
    }

    public <T> T verificarSujetoCredito(Class<T> responseType, String cedula) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (cedula != null) {
            resource = resource.queryParam("cedula", cedula);
        }
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}

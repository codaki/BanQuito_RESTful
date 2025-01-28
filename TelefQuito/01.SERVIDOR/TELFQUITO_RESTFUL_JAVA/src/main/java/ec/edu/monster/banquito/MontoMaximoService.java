/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package ec.edu.monster.banquito;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:WSMontoMaximo
 * [monto-maximo]<br>
 * USAGE:
 * <pre>
 *        MontoMaximoService client = new MontoMaximoService();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author danie
 */
public class MontoMaximoService {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/BANQUITO_RESTFUL_JAVA/resources";

    public MontoMaximoService() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("monto-maximo");
    }

    public <T> T calcularMontoMaximo(Class<T> responseType, String cod_cliente) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (cod_cliente != null) {
            resource = resource.queryParam("cod_cliente", cod_cliente);
        }
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}

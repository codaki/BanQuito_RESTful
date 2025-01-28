/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package ec.edu.monster.banquito;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Jersey REST client generated for REST resource:WSGenerarTabla
 * [generar-tabla]<br>
 * USAGE:
 * <pre>
 *        GenerarTablaService client = new GenerarTablaService();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author danie
 */
public class GenerarTablaService {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/BANQUITO_RESTFUL_JAVA/resources";

    public GenerarTablaService() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("generar-tabla");
    }

    public <T> T consultarCreditosPorCedula(Class<T> responseType, String cedula) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (cedula != null) {
            resource = resource.queryParam("cedula", cedula);
        }
        resource = resource.path("creditos");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T consultarTablaAmortizacion(Class<T> responseType, String cedula) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (cedula != null) {
            resource = resource.queryParam("cedula", cedula);
        }
        resource = resource.path("tabla-amortizacion");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

   public Response crearCreditoYTabla(int codCliente, double monto, int plazoMeses) throws ClientErrorException {
        // Construimos un objeto JSON dinámico para enviar como cuerpo de la petición
        Map<String, Object> requestEntity = new HashMap<>();
        requestEntity.put("codCliente", codCliente);
        requestEntity.put("monto", monto );
        requestEntity.put("plazoMeses", plazoMeses);
        System.out.println("creartablas");
        System.out.println(codCliente);
        System.out.println(monto);
        System.out.println(plazoMeses);

        // Enviamos el POST request
        return webTarget.path("tabla-amortizacion")
                        .request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                        .post(jakarta.ws.rs.client.Entity.json(requestEntity), Response.class);
    }

    public void close() {
        client.close();
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package ec.edu.monster.ws;

import ec.edu.monster.servicio.MontoMaximoService;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * REST Web Service
 *
 * @author danie
 */
@Path("monto-maximo")
@RequestScoped
public class WSMontoMaximo {

  
    public WSMontoMaximo() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularMontoMaximo(@QueryParam("cod_cliente") int codigo) {
        MontoMaximoService montoMaximoService = new MontoMaximoService();
        try {
            double montoMaximo = montoMaximoService.calcularMontoMaximoCredito(codigo);
            return Response.ok(montoMaximo).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al calcular el monto máximo: " + e.getMessage()).build();
        }
    }
   
}

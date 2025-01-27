/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package ec.edu.monster.ws;

import ec.edu.monster.servicio.SujetoCreditoService;
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

/**
 * REST Web Service
 *
 * @author danie
 */
@Path("sujeto-credito")
@RequestScoped
public class WSSujetoCredito {

   
    public WSSujetoCredito() {
    }
@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarSujetoCredito(@QueryParam("cedula") String cedula) {
        SujetoCreditoService sujetoCreditoService = new SujetoCreditoService();
        int resultado = sujetoCreditoService.verificar(cedula);
        if (resultado > 0) {
            return Response.ok(resultado).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("El cliente no cumple los requisitos.").build();
        }
    }
}

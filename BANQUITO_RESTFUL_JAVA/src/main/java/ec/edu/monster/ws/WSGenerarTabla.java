
package ec.edu.monster.ws;

import ec.edu.monster.model.Credito;
import ec.edu.monster.model.Tabla;
import ec.edu.monster.servicio.GenerarTablaService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.sql.SQLException;
import java.util.List;

/**
 * REST Web Service
 *
 * @author danie
 */
@Path("generar-tabla")
@RequestScoped
public class WSGenerarTabla {
    /**
     * Creates a new instance of WSGenerarTabla
     */
    public WSGenerarTabla() {
    }
    
     @Path("tabla-amortizacion")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearCreditoYTabla(Credito credito) {
        if (credito.getPlazoMeses() < 3 || credito.getPlazoMeses() > 18) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El plazo debe ser entre 3 y 18 meses.").build();
        }

        GenerarTablaService generarTablaService = new GenerarTablaService();
        try {
            generarTablaService.crearCreditoYTablaAmortizacion(credito);
            return Response.ok("Crédito y tabla de amortización creados exitosamente.").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el crédito y tabla de amortización: " + e.getMessage()).build();
        }
    }
    @Path("tabla-amortizacion")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarTablaAmortizacion(@QueryParam("cedula") String cedula) {
        GenerarTablaService generarTablaService = new GenerarTablaService();
        try {
            List<Tabla> amortizaciones = generarTablaService.consultarTablaAmortizacion(cedula);
            return Response.ok(amortizaciones).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al consultar la tabla de amortización: " + e.getMessage()).build();
        }
    }

    @Path("creditos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarCreditosPorCedula(@QueryParam("cedula") String cedula) {
        GenerarTablaService generarTablaService = new GenerarTablaService();
        try {
            List<Credito> creditos = generarTablaService.getCreditosByCedula(cedula);
            return Response.ok(creditos).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al consultar los créditos: " + e.getMessage()).build();
        }
    }
}

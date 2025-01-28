
package ec.edu.monster.ws;

import ec.edu.monster.DAO.ClienteComercializadoraDAO;
import ec.edu.monster.DAO.CompraDAO;
import ec.edu.monster.DAO.TelefonoDAO;
import ec.edu.monster.banquito.GenerarTablaService;
import ec.edu.monster.banquito.MontoMaximoService;
import ec.edu.monster.banquito.SujetoCreditoService;
import ec.edu.monster.bdd.DBConnection;
import ec.edu.monster.models.Carrito;
import ec.edu.monster.models.Factura;
import ec.edu.monster.models.Tabla;
import ec.edu.monster.servicio.CompraService;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@Path("wscompra")
@RequestScoped
public class Wscompra {

   private CompraService compraService;
    public Wscompra() {
          try {
            Connection connection = DBConnection.getConnection();
            CompraDAO compraDAO = new CompraDAO(connection);
            TelefonoDAO telefonoDAO = new TelefonoDAO(connection);
            ClienteComercializadoraDAO clientecDAO = new ClienteComercializadoraDAO(connection);
            SujetoCreditoService sujetoCreditoService = new SujetoCreditoService();
            MontoMaximoService montoMaximoService = new MontoMaximoService();
            GenerarTablaService generarTablaService = new GenerarTablaService();

            this.compraService = new CompraService(compraDAO, telefonoDAO, clientecDAO, sujetoCreditoService,
                    montoMaximoService, generarTablaService);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @POST
    @Path("efectivo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Accepts form-urlencoded data
    @Produces(MediaType.TEXT_PLAIN)
    public Response comprarEfectivo(Carrito carrito, @QueryParam("cedula") String cedula) {
        try {
            String result = compraService.realizarCompraEfectivo(carrito, cedula, "Efectivo");
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al realizar la compra en efectivo: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("credito")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Accepts form-urlencoded data
    @Produces(MediaType.TEXT_PLAIN)
    public Response comprarCredito(Carrito carrito, @QueryParam("cedula") String cedula, @QueryParam("plazoMeses") int plazoMeses) {
        try {
            String result = compraService.realizarCompraCredito(carrito, cedula, "Crédito Directo", plazoMeses);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al realizar la compra con crédito: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("factura")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerFactura(@QueryParam("cedula") String cedula) {
        List<Factura> facturas = compraService.obtenerFactura(cedula);
        return Response.ok(facturas).build();
    }

    @GET
    @Path("tablaAmortizacion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarTablaAmortizacion(@QueryParam("cedula") String cedula) {
        List<Tabla> tabla = compraService.consultarTablaAmortizacion(cedula);
        return Response.ok(tabla).build();
    }

    
}

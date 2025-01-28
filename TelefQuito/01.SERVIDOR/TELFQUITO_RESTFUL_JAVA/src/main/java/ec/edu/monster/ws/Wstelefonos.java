
package ec.edu.monster.ws;

import ec.edu.monster.DAO.TelefonoDAO;
import ec.edu.monster.bdd.DBConnection;
import ec.edu.monster.models.Telefonos;
import ec.edu.monster.servicio.TelefonoService;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("wstelefonos")
@RequestScoped
public class Wstelefonos {

    private TelefonoService telefonoService;
    public Wstelefonos() {
        try {
            Connection connection = DBConnection.getConnection();
            TelefonoDAO telefonoDAO = new TelefonoDAO(connection);
            this.telefonoService = new TelefonoService(telefonoDAO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTelefonoById(@PathParam("id") int id) {
        try {
            Telefonos telefono = telefonoService.getTelefonoById(id);
            return Response.ok(telefono).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el teléfono: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTelefonos() {
        try {
            List<Telefonos> telefonos = telefonoService.getAllTelefonos();
            return Response.ok(telefonos).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los teléfonos: " + e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertTelefono(Telefonos telefono) {
        try {
            String result = telefonoService.insertTelefono(telefono);
            return Response.ok(result).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al insertar el teléfono: " + e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateTelefono(Telefonos telefono) {
        try {
            String result = telefonoService.updateTelefono(telefono);
            return Response.ok(result).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el teléfono: " + e.getMessage()).build();
        }
    }
}

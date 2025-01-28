
package ec.edu.monster.ws;

import ec.edu.monster.servicio.LoginService;
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


@Path("wslogin")
@RequestScoped
public class Wslogin {

   
    public Wslogin() {
        
    }
@POST
@Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Accepts form-urlencoded data
@Produces(MediaType.TEXT_PLAIN)
public Response auth(@QueryParam("username") String username, @QueryParam("password") String password) {
    LoginService service = new LoginService();
    boolean resultado = service.login(username, password);

    if (resultado) {
        return Response.ok("Autenticación exitosa").build();
    } else {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Usuario o contraseña incorrectos").build();
    }
}
    
}

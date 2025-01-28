package ec.edu.monster.servicio;

import ec.edu.monster.DAO.ClienteComercializadoraDAO;
import ec.edu.monster.DAO.CompraDAO;
import ec.edu.monster.models.Compras;
import ec.edu.monster.banquito.SujetoCreditoService;
import ec.edu.monster.banquito.MontoMaximoService;
import ec.edu.monster.banquito.GenerarTablaService;
import ec.edu.monster.models.Telefonos;
import ec.edu.monster.DAO.TelefonoDAO;
import ec.edu.monster.models.Carrito;
import ec.edu.monster.models.CreditoRequest;
import ec.edu.monster.models.Factura;
import ec.edu.monster.models.Tabla;
import ec.edu.monster.models.TelefonoCarrito;
import jakarta.ws.rs.core.Response;
import java.util.List;

import java.time.LocalDate;

public class CompraService {
    private CompraDAO compraDAO;
    private TelefonoDAO telefonoDAO;
    private ClienteComercializadoraDAO clientecDAO;
    private SujetoCreditoService sujetoCreditoService;
    private MontoMaximoService montoMaximoService;
    private GenerarTablaService generarTablaService;

    public CompraService(CompraDAO compraDAO, TelefonoDAO telefonoDAO, ClienteComercializadoraDAO clientecDAO,
            SujetoCreditoService sujetoCreditoService, MontoMaximoService montoMaximoService,
            GenerarTablaService generarTablaService) {
        this.compraDAO = compraDAO;
        this.telefonoDAO = telefonoDAO;
        this.clientecDAO = clientecDAO;
        this.sujetoCreditoService = sujetoCreditoService;
        this.montoMaximoService = montoMaximoService;
        this.generarTablaService = generarTablaService;
    }



        public String realizarCompraEfectivo(Carrito carrito, String codCedula, String formaPago) throws Exception {
    int idCliente = clientecDAO.getCodClienteByCedula(codCedula);
    if (idCliente == -1) {
        return "No se encontró el cliente";
    }

    if (carrito.getTelefonos().isEmpty()) {
        return "El carrito está vacío";
    }

    for (TelefonoCarrito item : carrito.getTelefonos()) {
        int telefonoId = item.getTelefonoId();
        int cantidad = item.getCantidad();

        // Validar que la cantidad sea mayor que 0
        if (cantidad <= 0) {
            return "La cantidad para el teléfono con ID " + telefonoId + " no es válida";
        }

        Telefonos telefono = telefonoDAO.getTelefonoById(telefonoId);
        double precioOriginal = telefono.getPrecio();

        for (int i = 0; i < cantidad; i++) {
            double descuento = precioOriginal * 0.42; // Aplicar descuento del 42%
            double precioFinal = precioOriginal - descuento;

            Compras compra = new Compras();
            compra.setCodTelefono(telefonoId);
            compra.setCodcCliente(idCliente);
            compra.setFormaPago(formaPago);
            compra.setFecha(LocalDate.now().toString());
            compra.setDescuento(descuento);
            compra.setPreciofinal(precioFinal);

            if (!compraDAO.createCompraEfectivo(compra)) {
                return "Compra en efectivo fallida para el teléfono con ID: " + telefonoId;
            }
        }
    }

    return "Compra en efectivo exitosa!!";
}

   public String realizarCompraCredito(Carrito carrito, String cedula, String formaPago, int plazoMeses) throws Exception {
    if (carrito.getTelefonos().isEmpty()) {
        return "El carrito está vacío";
    }

    int esSujetoCredito = sujetoCreditoService.verificarSujetoCredito(Integer.class, cedula);
    if (esSujetoCredito == 0) {
        return "El cliente no es sujeto de crédito.";
    }

    // Calcular el monto total del carrito
    double montoTotal = 0.0;
    for (TelefonoCarrito item : carrito.getTelefonos()) {
        int telefonoId = item.getTelefonoId();
        int cantidad = item.getCantidad();

        if (cantidad <= 0) {
            return "La cantidad para el teléfono con ID " + telefonoId + " no es válida.";
        }

        Telefonos telefono = telefonoDAO.getTelefonoById(telefonoId);
        montoTotal += telefono.getPrecio() * cantidad;
    }
       System.out.println("monto-total"+montoTotal);

    // Verificar que el monto total no exceda el monto máximo de crédito
    double montoMaximo = montoMaximoService.calcularMontoMaximo(Double.class, String.valueOf(esSujetoCredito));
       System.out.println("monto-maximo"+montoMaximo);
    if (montoTotal > montoMaximo) {
        return "El monto total del carrito excede el monto máximo de crédito aprobado.";
    }
    CreditoRequest request = new CreditoRequest(esSujetoCredito, montoTotal, plazoMeses);
    System.out.println("pipipi");
        Response resultadoResponse = generarTablaService.crearCreditoYTabla(esSujetoCredito, montoTotal, plazoMeses);
   
        System.out.println(resultadoResponse.readEntity(String.class));
        if (resultadoResponse.getStatus() != 200) {
          
            return "Error al crear la tabla de amortización.";
        }

    // Registrar las compras individuales en la base de datos
    int idCliente = clientecDAO.getCodClienteByCedula(cedula);
    if (idCliente == -1) {
        return "No se encontró el cliente.";
    }

    for (TelefonoCarrito item : carrito.getTelefonos()) {
        int telefonoId = item.getTelefonoId();
        int cantidad = item.getCantidad();

        for (int i = 0; i < cantidad; i++) {
            Telefonos telefono = telefonoDAO.getTelefonoById(telefonoId);

            Compras compra = new Compras();
            compra.setCodTelefono(telefonoId);
            compra.setCodcCliente(idCliente);
            compra.setFormaPago(formaPago);
            compra.setFecha(LocalDate.now().toString());
            compra.setPreciofinal(telefono.getPrecio());

            if (!compraDAO.createCompraCredito(compra)) {
                return "Compra a crédito fallida para el teléfono con ID: " + telefonoId;
            }
        }
    }

    return "Compra a crédito exitosa!!";
}


    public List<Factura> obtenerFactura(String cedula) {
        return compraDAO.obtenerFactura(cedula);
    }

    public List<Tabla> consultarTablaAmortizacion(String cedula) {
        return generarTablaService.consultarTablaAmortizacion(List.class,cedula);
    }
}
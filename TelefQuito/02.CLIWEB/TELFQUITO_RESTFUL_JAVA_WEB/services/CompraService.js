const axios = require("axios");

class CompraService {
  constructor() {
    this.baseUrl = "http://localhost:8080/TELFQUITO_RESTFUL_JAVA/resources";
  }

  async comprarEfectivo(carrito, codCedula) {
    try {
      console.log("Comprando en efectivo", carrito, codCedula);
      const response = await axios.post(`${this.baseUrl}/wscompra/efectivo`, {
        carrito,
        codCedula,
      });
      return response.data.result;
    } catch (error) {
      console.error("Compra Efectivo Error:", error);
      throw error;
    }
  }

  async comprarCredito(carrito, codCedula, tarjetaCredito) {
    try {
      console.log("Comprando con crédito", carrito, codCedula, tarjetaCredito);
      const response = await axios.post(`${this.baseUrl}/wscompra/credito`, {
        carrito,
        codCedula,
        tarjetaCredito,
      });
      return response.data.result;
    } catch (error) {
      console.error("Compra Crédito Error:", error);
      throw error;
    }
  }
  async obtenerFactura(cedula) {
    try {
      const response = await axios.get(`${this.baseUrl}/wscompra/factura`, {
        params: { cedula },
      });
      return response.data;
    } catch (error) {
      console.error("Obtener Factura Error:", error);
      throw error;
    }
  }

  async consultarTablaAmortizacion(cedula) {
    try {
      const response = await axios.get(
        `${this.baseUrl}/wscompra/tablaAmortizacion`,
        {
          params: { cedula },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Consultar Tabla Amortizacion Error:", error);
      throw error;
    }
  }
}

module.exports = new CompraService();

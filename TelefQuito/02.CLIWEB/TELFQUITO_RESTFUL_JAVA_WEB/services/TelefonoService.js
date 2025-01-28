const axios = require("axios");

class TelefonoService {
  constructor() {
    this.baseUrl = "http://localhost:8080/TELFQUITO_RESTFUL_JAVA/resources";
  }

  async getTelefonoById(id) {
    try {
      const response = await axios.get(`${this.baseUrl}/wstelefonos/${id}`);
      return response.data.telefono;
    } catch (error) {
      console.error("Get Telefono By Id Error:", error);
      throw error;
    }
  }

  async getAllTelefonos() {
    try {
      const response = await axios.get(`${this.baseUrl}/wstelefonos`);
      return response.data;
    } catch (error) {
      console.error("Get All Telefonos Error:", error);
      throw error;
    }
  }

  async insertTelefono(telefono) {
    try {
      const response = await axios.post(`${this.baseUrl}/telefonos`, telefono);
      return response.data.result;
    } catch (error) {
      console.error("Insert Telefono Error:", error);
      throw error;
    }
  }

  async updateTelefono(telefono) {
    try {
      const response = await axios.put(
        `${this.baseUrl}/telefonos/${telefono.id}`,
        telefono
      );
      return response.data.result;
    } catch (error) {
      console.error("Update Telefono Error:", error);
      throw error;
    }
  }
}

module.exports = new TelefonoService();

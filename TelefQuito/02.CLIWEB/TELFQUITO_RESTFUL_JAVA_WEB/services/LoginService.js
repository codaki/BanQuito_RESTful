const axios = require("axios");

class AuthService {
  constructor() {
    this.baseUrl = "http://localhost:8080/TELFQUITO_RESTFUL_JAVA/resources";
  }

  async authenticate(username, password) {
    try {
      console.log("Authenticating...");
      console.log(username, password);

      // Realizar la solicitud con parámetros en la URL
      const response = await axios.post(
        `${this.baseUrl}/wslogin`,
        null, // Sin cuerpo
        {
          params: {
            username,
            password,
          },
        }
      );

      return response.data;
    } catch (error) {
      console.error("Authentication Error:", error);
      throw error;
    }
  }
}

module.exports = new AuthService();

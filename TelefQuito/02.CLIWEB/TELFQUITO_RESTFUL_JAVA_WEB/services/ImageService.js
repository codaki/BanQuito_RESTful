const axios = require("axios");

class ImageService {
  constructor() {
    this.baseUrl = "http://localhost:8080/TELFQUITO_REST_API";
  }

  async GetImage(filename) {
    try {
      const response = await axios.get(`${this.baseUrl}/images/${filename}`);
      return response.data.imageBase64;
    } catch (error) {
      console.error("ImageError:", error);
      throw error;
    }
  }

  async UploadImage(filename, imageBase64) {
    try {
      const response = await axios.post(`${this.baseUrl}/images`, {
        fileName: filename,
        imageData: imageBase64,
      });
      return response.data.result;
    } catch (error) {
      console.error("ImageError:", error);
      throw error;
    }
  }
}

module.exports = new ImageService();

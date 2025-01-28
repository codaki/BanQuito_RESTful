const express = require("express");
const bodyParser = require("body-parser");
const path = require("path");
const authService = require("./services/LoginService");
const compraService = require("./services/CompraService.js");
const telefonoService = require("./services/TelefonoService");
const telefonoController = require("./public/controller/telefonoController");
const imageService = require("./services/ImageService");
const app = express();
const PORT = process.env.PORT || 3003;

// Middleware
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, "public")));
app.use(express.static(path.join(__dirname, "views")));

// Routes
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "views", "login.html"));
});

app.get("/consultarCreditos", (req, res) => {
  res.sendFile(path.join(__dirname, "views", "consultarCreditos.html"));
});

app.get("/crudTelefonos", (req, res) => {
  res.sendFile(path.join(__dirname, "views", "crudTelefonos.html"));
});

app.post("/login", async (req, res) => {
  const { username, password } = req.body;
  console.log("ruta", username, password);

  try {
    const isAuthenticated = await authService.authenticate(username, password);
    res.json({ success: isAuthenticated });
  } catch (error) {
    res.status(500).json({ success: false, message: "Authentication error" });
  }
});

app.get("/account", (req, res) => {
  res.sendFile(path.join(__dirname, "views", "account.html"));
});

app.get("/telefonos", (req, res) => {
  res.sendFile(path.join(__dirname, "views", "telefonos.html"));
});

app.get("/getAllTelefonos", async (req, res) => {
  try {
    const result = await telefonoService.getAllTelefonos();
    res.json({ success: true, result });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Get all teléfonos error",
      error: error.message,
    });
  }
});

app.get("/getImage", async (req, res) => {
  const { filename } = req.query;

  try {
    const imageBase64 = await imageService.GetImage(filename);
    res.json({ success: true, image: imageBase64 });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error fetching image",
      error: error.message,
    });
  }
});

app.post("/uploadImage", async (req, res) => {
  const { fileName, imageData } = req.body;

  try {
    const result = await imageService.UploadImage(fileName, imageData);
    res.json({ success: true, result });
  } catch (error) {
    res.status(500).json({ success: false, message: "Error uploading image" });
  }
});

app.get("/obtenerFactura", async (req, res) => {
  const { cedula } = req.query;

  try {
    const result = await compraService.obtenerFactura(cedula);
    res.json({ success: true, result });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error fetching factura",
      error: error.message,
    });
  }
});

app.get("/consultarTablaAmortizacion", async (req, res) => {
  const { cedula } = req.query;

  try {
    const result = await compraService.consultarTablaAmortizacion(cedula);
    res.json({ success: true, result });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error fetching tabla amortizacion",
      error: error.message,
    });
  }
});

// Integrar el controlador de teléfono
telefonoController(app);

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

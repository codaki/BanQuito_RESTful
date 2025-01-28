import { showModal } from "./modal.js";
import { getCart, clearCart } from "./cart.js";

document.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  const telefonoId = urlParams.get("id");

  if (telefonoId) {
    loadTelefonoData(telefonoId);
  }

  document.getElementById("tipoCompra").addEventListener("change", async (e) => {
    const tipoCompra = e.target.value;
    document.querySelectorAll(".compra-opcion").forEach((el) => {
      el.classList.add("hidden");
      el.querySelectorAll("input").forEach((input) => {
        input.removeAttribute("required");
      });
    });
    if (tipoCompra === "efectivo") {
      const compraEfectivo = document.getElementById("compraEfectivo");
      compraEfectivo.classList.remove("hidden");

      const totalPrice = await calculateTotalPrice();
      const descuento = totalPrice * 0.42;
      const precioFinal = totalPrice - descuento;
      document.getElementById("precioOriginal").textContent =
        "$" + totalPrice.toFixed(2);
      document.getElementById("descuento").textContent =
        "$" + descuento.toFixed(2);
      document.getElementById("precioFinal").textContent =
        "$" + precioFinal.toFixed(2);

    } else if (tipoCompra === "credito") {
      const compraCredito = document.getElementById("compraCredito");
      compraCredito.classList.remove("hidden");
      compraCredito.querySelectorAll("input").forEach((input) => {
        input.setAttribute("required", "required");
      });

      const totalPriceC = await calculateTotalPrice();
      console.log(totalPriceC);
      const descuentoC = totalPriceC * 0.0;
      const precioFinalC = totalPriceC - descuentoC;
      document.getElementById("precioOriginalC").textContent =
        "$" + totalPriceC.toFixed(2);
      document.getElementById("descuentoC").textContent =
        "$" + descuentoC.toFixed(2);
      document.getElementById("precioFinalC").textContent =
        "$" + precioFinalC.toFixed(2);
    }
  });

  document.getElementById("compraForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const tipoCompra = document.getElementById("tipoCompra").value;
    const carrito = getCart().map(item => ({
      telefonoId: item.id,
      cantidad: item.quantity
    }));
    const cedula = document.getElementById("cedula").value;
    const plazoMeses = tipoCompra === "credito"
      ? parseInt(document.getElementById("plazoMeses").value, 10)
      : null;

    if (tipoCompra === "credito" && (plazoMeses < 3 || plazoMeses > 18)) {
      showModal(
        "Advertencia",
        "El plazo debe estar entre 3 y 18 meses.",
        true,
        null
      );
      return;
    }

    try {
      const response = await fetch(
        tipoCompra === "efectivo" ? "/comprarEfectivo" : "/comprarCredito",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            carrito,
            cedula,
            ...(tipoCompra === "credito" && { plazoMeses }),
          }),
        }
      );

      const result = await response.json();
      console.log(result);

      if (result.success) {
        if (tipoCompra === "credito" && result.result === "Compra exitosa a credito!!") {
          showModal("Éxito", "Compra realizada exitosamente.", false, () => {
            clearCart();
            window.location.href = '/telefonos';
          });
        } else if (result.result === "Compra en efectivo exitosa!!") {
          showModal("Resultado", result.result, false, () => {
            clearCart();
            window.location.href = '/telefonos';
          });
        } else {
          showModal("Resultado", result.result, false);
        }
      } else {
        showModal("Error", "Fallo al realizar la compra.", true);
      }
    } catch (error) {
      console.error("Compra error:", error);
      showModal("Error", error.message, true);
    }
  });

  loadCartItems();
});

async function loadTelefonoData(id) {
  try {
    const response = await fetch("/getTelefonoById", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id }),
    });

    const result = await response.json();

    if (result.success) {
      const telefono = result.result;
      document.getElementById("telefonoId").value = telefono.codTelefono;
      document.getElementById("nombre").textContent = telefono.nombre;
      document.getElementById("precio").textContent = "$" + telefono.precio;
      document.getElementById("marca").textContent = telefono.marca;
    } else {
      showModal(
        "Error",
        "Fallo en recuperar los datos del teléfono.",
        true,
        null
      );
    }
  } catch (error) {
    console.error("Load telefono data error:", error);
    showModal("Error", error.message, true, null);
  }
}

async function loadCartItems() {
  const cartItemsContainer = document.getElementById("cartItems");
  const cart = getCart();
  let totalPrice = 0;

  for (const item of cart) {
    try {
      const response = await fetch("/getTelefonoById", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id: item.id }),
      });

      const result = await response.json();

      if (result.success) {
        const telefono = result.result;

        const itemElement = document.createElement("div");
        itemElement.classList.add("gridCentrao", "grid4columns");

        const nombreElement = document.createElement("div");
        nombreElement.classList.add("gridCentrao", "grid2rows");
        nombreElement.innerHTML = `<strong>Nombre:</strong> <span>${telefono.nombre}</span>`;

        const precioElement = document.createElement("div");
        precioElement.classList.add("gridCentrao", "grid2rows");
        precioElement.innerHTML = `<strong>Precio:</strong> <span>$${telefono.precio}</span>`;

        const marcaElement = document.createElement("div");
        marcaElement.classList.add("gridCentrao", "grid2rows");
        marcaElement.innerHTML = `<strong>Marca:</strong> <span>${telefono.marca}</span>`;

        const cantidadElement = document.createElement("div");
        cantidadElement.classList.add("gridCentrao", "grid2rows");
        cantidadElement.innerHTML = `<strong>Cantidad:</strong> <span>${item.quantity}</span>`;

        itemElement.appendChild(nombreElement);
        itemElement.appendChild(precioElement);
        itemElement.appendChild(marcaElement);
        itemElement.appendChild(cantidadElement);

        cartItemsContainer.appendChild(itemElement);

        // Add to total price
        totalPrice += telefono.precio * item.quantity;
      } else {
        console.error("Failed to fetch phone details for item ID:", item.id);
      }
    } catch (error) {
      console.error("Error fetching phone details for item ID:", item.id, error);
    }
  }

  // Update total price for efectivo
  const tipoCompra = document.getElementById("tipoCompra").value;
  if (tipoCompra === "efectivo") {
    const descuento = totalPrice * 0.42;
    const precioFinal = totalPrice - descuento;
    document.getElementById("precioOriginal").textContent =
      "$" + totalPrice.toFixed(2);
    document.getElementById("descuento").textContent =
      "$" + descuento.toFixed(2);
    document.getElementById("precioFinal").textContent =
      "$" + precioFinal.toFixed(2);
  }
}

async function calculateTotalPrice() {
  const cart = getCart();
  let totalPrice = 0;

  for (const item of cart) {
    try {
      const response = await fetch("/getTelefonoById", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id: item.id }),
      });

      const result = await response.json();

      if (result.success) {
        const telefono = result.result;
        totalPrice += telefono.precio * item.quantity;
      } else {
        console.error("Failed to fetch phone details for item ID:", item.id);
      }
    } catch (error) {
      console.error("Error fetching phone details for item ID:", item.id, error);
    }
  }

  return totalPrice;
}
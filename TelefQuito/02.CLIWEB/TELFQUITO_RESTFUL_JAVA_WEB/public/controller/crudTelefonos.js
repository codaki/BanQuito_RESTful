import { showModal } from "./modal.js";

document.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  const telefonoId = urlParams.get("id");

  if (telefonoId) {
    loadTelefonoData(telefonoId);
  }

  document.getElementById("telefonoForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const telefonoId = document.getElementById("telefonoId").value;
    const nombre = document.getElementById("nombre").value;
    const precio = document.getElementById("precio").value;
    const marca = document.getElementById("marca").value;
    const disponible = document.getElementById("disponible").value === "true" ? 1 : 0;
    const imagen = document.getElementById("imagen").files[0];

    // Only require image for new phones
    if (!imagen && !telefonoId) {
      showModal("Error", "Debe seleccionar una imagen para continuar.", true);
      return;
    }

    try {
      let imageName = null;

      // Only process image if a new one was selected
      if (imagen) {
        const reader = new FileReader();
        const imageData = await new Promise((resolve, reject) => {
          reader.onload = () => resolve(reader.result.split(",")[1]);
          reader.onerror = reject;
          reader.readAsDataURL(imagen);
        });

        const currentDate = new Date().toISOString().split("T")[0];
        imageName = `${nombre.slice(0, 3).toUpperCase()}_${currentDate}_${imagen.name}`;

        const uploadResponse = await fetch("/uploadImage", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            fileName: imageName,
            imageData: imageData,
          }),
        });

        const uploadResult = await uploadResponse.json();
        if (!uploadResult.success) {
          showModal("Error", "Error al subir la imagen. Intente de nuevo.", true);
          return;
        }
      }

      const telefono = {
        codTelefono: telefonoId, // Use codTelefono instead of id
        nombre,
        precio,
        marca,
        disponible,
        ...(imageName && { imgUrl: imageName }) // Only include imgUrl if there's a new image
      };

      console.log("Sending telefono data:", telefono);

      const response = await fetch(telefonoId ? "/updateTelefono" : "/insertTelefono", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ telefono }),
      });

      const result = await response.json();
      if (result.success) {
        showModal(
          "Éxito",
          `Teléfono ${telefonoId ? "actualizado" : "guardado"} exitosamente.`,
          false,
          () => {
            window.location.href = "/telefonos";
          }
        );
      } else {
        showModal("Error", "Error al guardar el teléfono.", true);
      }
    } catch (error) {
      console.error("Error uploading image or saving data:", error);
      showModal("Error", "Ocurrió un error durante la subida de la imagen o el guardado de los datos.", true);
    }
  });
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
      document.getElementById("nombre").value = telefono.nombre;
      document.getElementById("precio").value = telefono.precio;
      document.getElementById("marca").value = telefono.marca;
      document.getElementById("disponible").value = telefono.disponible ? "true" : "false";
      document.getElementById("form-title").innerText = "Actualizar Teléfono";
      document.getElementById("submitButton").innerText = "Actualizar";

      // Remove required attribute from image input when updating
      document.getElementById("imagen").removeAttribute("required");

      // Fetch and display the image
      if (telefono.imgUrl) {
        try {
          const imageResponse = await fetch(`/getImage?filename=${telefono.imgUrl}`);
          const imageResult = await imageResponse.json();
          if (imageResult.success) {
            const imagePreview = document.getElementById("imagePreview");
            imagePreview.src = `data:image/jpeg;base64,${imageResult.image}`;
            imagePreview.style.display = "block";
          } else {
            console.error("Error fetching image:", imageResult.message);
          }
        } catch (error) {
          console.error("Error fetching image:", error);
        }
      }
    } else {
      showModal("Error", "Fallo en recuperar los datos del teléfono.", true);
    }
  } catch (error) {
    console.error("Load telefono data error:", error);
    showModal(
      "Error",
      "Un error ha ocurrido durante la carga de los datos del teléfono. Por favor, revise la conexión.",
      true
    );
  }
}
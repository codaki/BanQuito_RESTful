import { getCart, addToCart, removeFromCart } from './cart.js';

let telefonosData = new Map();

document.addEventListener('DOMContentLoaded', async () => {
    await loadCartItems();
});

async function loadCartItems() {
    const cart = getCart();
    const cartList = document.getElementById('cart-list');
    const cartTotal = document.getElementById('cartTotal');
    cartList.innerHTML = ''; // Clear existing items
    let total = 0;

    for (const item of cart) {
        const telefono = await fetchTelefonoData(item.id);
        if (telefono) {
            const telefonoItem = await createCartItem(telefono, item.quantity);
            cartList.appendChild(telefonoItem);
            total += telefono.precio * item.quantity;
        }
    }

    cartTotal.innerText = total.toFixed(2);
}

async function fetchTelefonoData(id) {
    // Check if we already have the data
    if (telefonosData.has(id)) {
        return telefonosData.get(id);
    }

    try {
        const response = await fetch(`/getTelefonoById`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id }),
        });

        const result = await response.json();
        if (result.success) {
            // Store the telefono data for future use
            telefonosData.set(id, result.result);
            return result.result;
        }
        console.error("Error fetching telefono:", result.message);
        return null;
    } catch (error) {
        console.error("Error fetching telefono:", error);
        return null;
    }
}

async function fetchImage(imgUrl) {
    try {
        const response = await fetch(`/getImage?filename=${imgUrl}`);
        const result = await response.json();
        if (result.success) {
            return result.image;
        }
        throw new Error(result.message);
    } catch (error) {
        console.error(`Error loading image:`, error);
        return 'defaultImageBase64'; // Return default image if fetch fails
    }
}

async function createCartItem(telefono, quantity) {
    const imgBased64 = await fetchImage(telefono.imgUrl);

    const telefonoItem = document.createElement('div');
    telefonoItem.className = 'cart-item';
    telefonoItem.innerHTML = `
        <div class="imgCarrito">
            <img src="data:image/jpeg;base64,${imgBased64}" class="telfImg" alt="${telefono.nombre}" />
        </div>
        <div class="datosCarrito">
            <div class="font-bold text-black">Nombre: ${telefono.nombre}</div>
            <div class="font-normal text-gray-700">Marca: ${telefono.marca}</div>
            <div class="font-normal text-gray-700 precio-value" data-precio="${telefono.precio}">Precio: $${telefono.precio}</div>
        </div>
        <div class="cantidadCarrito">
            <div class="font-normal text-gray-700">Cantidad: <input type="number" class="cart-quantity" value="${quantity}" min="0" data-id="${telefono.codTelefono}" /></div>
        </div>
    `;

    const quantityInput = telefonoItem.querySelector(".cart-quantity");
    quantityInput.addEventListener("change", async () => {
        const newQuantity = parseInt(quantityInput.value, 10);
        const id = telefono.codTelefono;

        if (newQuantity > 0) {
            // Update cart with absolute quantity instead of difference
            const cart = getCart();
            const currentItem = cart.find(item => item.id === id);
            if (currentItem) {
                addToCart(id, newQuantity - currentItem.quantity);
            }
        } else {
            removeFromCart(id);
            telefonoItem.remove();
        }

        await updateCartTotal();
    });

    return telefonoItem;
}

async function updateCartTotal() {
    const cart = getCart();
    const cartTotal = document.getElementById('cartTotal');
    let total = 0;

    for (const item of cart) {
        const telefono = await fetchTelefonoData(item.id);
        if (telefono) {
            total += telefono.precio * item.quantity;
        }
    }

    cartTotal.innerText = total.toFixed(2);
}
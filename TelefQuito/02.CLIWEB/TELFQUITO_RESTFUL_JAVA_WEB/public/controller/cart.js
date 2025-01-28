export function getCart() {
    const cart = localStorage.getItem('telfCarrito');
    return cart ? JSON.parse(cart) : [];
}

export function addToCart(id, quantity = 1) {
    const cart = getCart();
    const existingItem = cart.find(item => item.id === id);

    if (existingItem) {
        existingItem.quantity += quantity;
    } else {
        cart.push({ id, quantity });
    }

    localStorage.setItem('telfCarrito', JSON.stringify(cart));
}

export function removeFromCart(id) {
    let cart = getCart();
    cart = cart.filter(item => item.id !== id);
    localStorage.setItem('telfCarrito', JSON.stringify(cart));
}

export function clearCart() {
    localStorage.removeItem('telfCarrito');
}
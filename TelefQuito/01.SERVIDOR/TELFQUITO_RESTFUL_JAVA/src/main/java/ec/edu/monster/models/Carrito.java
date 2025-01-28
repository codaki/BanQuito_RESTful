
package ec.edu.monster.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"telefonos"})
public class Carrito {
    private List<TelefonoCarrito> telefonos;

    public Carrito() {
        this.telefonos = new ArrayList<>();
    }

    @XmlElement(name = "telefonoCarrito")
    public List<TelefonoCarrito> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<TelefonoCarrito> telefonos) {
        this.telefonos = telefonos;
    }

    public void agregarTelefono(int telefonoId, int cantidad) {
        this.telefonos.add(new TelefonoCarrito(telefonoId, cantidad));
    }

    public void vaciarCarrito() {
        this.telefonos.clear();
    }
}
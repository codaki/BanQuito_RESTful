
package ec.edu.monster.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TelefonoCarrito {
    private int telefonoId;
    private int cantidad;

    public TelefonoCarrito() {}

    public TelefonoCarrito(int telefonoId, int cantidad) {
        this.telefonoId = telefonoId;
        this.cantidad = cantidad;
    }

    @XmlElement
    public int getTelefonoId() {
        return telefonoId;
    }

    public void setTelefonoId(int telefonoId) {
        this.telefonoId = telefonoId;
    }

    @XmlElement
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
package ec.edu.monster.models;

public class CreditoRequest {
    private int codCliente;
    private double montoTotal;
    private int plazoMeses;

    public CreditoRequest() {}

    public CreditoRequest(int codCliente, double montoTotal, int plazoMeses) {
        this.codCliente = codCliente;
        this.montoTotal = montoTotal;
        this.plazoMeses = plazoMeses;
    }

    // Getters and setters
    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }
}
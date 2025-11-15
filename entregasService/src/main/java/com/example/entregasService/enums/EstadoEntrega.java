package com.example.entregasService.enums;

/**
 * Enum que define los estados posibles de una entrega
 */
public enum EstadoEntrega {
    /**
     * Entrega creada pero sin transportista asignado
     */
    PENDIENTE("pendiente"),
    
    /**
     * Entrega con transportista asignado y en camino al destino
     */
    EN_CAMINO("en_camino"),
    
    /**
     * Entrega completada exitosamente
     */
    ENTREGADA("entregada"),
    
    /**
     * Entrega cancelada por algún motivo
     */
    CANCELADA("cancelada");
    
    private final String valor;
    
    EstadoEntrega(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    /**
     * Obtiene el enum desde un string
     * @param valor El valor del estado como string
     * @return El enum correspondiente
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static EstadoEntrega fromValor(String valor) {
        for (EstadoEntrega estado : EstadoEntrega.values()) {
            if (estado.valor.equalsIgnoreCase(valor)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de entrega inválido: " + valor + 
            ". Valores permitidos: pendiente, en_camino, entregada, cancelada");
    }
    
    /**
     * Verifica si el valor es un estado válido
     * @param valor El valor a verificar
     * @return true si es válido, false en caso contrario
     */
    public static boolean esValido(String valor) {
        try {
            fromValor(valor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return this.valor;
    }
}

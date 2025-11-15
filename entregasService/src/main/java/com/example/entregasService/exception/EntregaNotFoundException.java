package com.example.entregasService.exception;

public class EntregaNotFoundException extends RuntimeException {
    
    public EntregaNotFoundException(String mensaje) {
        super(mensaje);
    }
    
    public EntregaNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

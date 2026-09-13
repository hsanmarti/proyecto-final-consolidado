package cl.dgt.consolidado.services;

public class ContribuyenteNoEncontradoException extends RuntimeException {

    public ContribuyenteNoEncontradoException(String rut) {
        super("No existe el contribuyente " + rut);
    }
}

package pe.edu.idat.biblioteca.service;

import pe.edu.idat.biblioteca.dto.rol.RolRequest;
import pe.edu.idat.biblioteca.dto.rol.RolResponse;

import java.util.List;

public interface RolService {
    RolResponse createRol (RolRequest rolRequest);
    List<RolResponse> listarRoles();
    RolResponse findById(Long id);
    RolResponse updateRol(Long id,RolRequest rolRequest);
    void deleteRol(Long id);
}

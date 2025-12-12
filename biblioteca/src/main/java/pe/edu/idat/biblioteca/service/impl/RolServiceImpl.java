package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.dto.rol.RolRequest;
import pe.edu.idat.biblioteca.dto.rol.RolResponse;

import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.exception.RolAlreadyExistsException;
import pe.edu.idat.biblioteca.exception.RolNotFoundException;
import pe.edu.idat.biblioteca.mapper.RolMapper;
import pe.edu.idat.biblioteca.repository.RolRepository;
import pe.edu.idat.biblioteca.service.RolService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Transactional
    @Override
    public RolResponse createRol(RolRequest rolRequest) {
        if(rolRepository.findByNombre(rolRequest.nombre()).isPresent()){
            throw new RolAlreadyExistsException("El rol ya existe");
        }
        Rol rol =rolMapper.toEntity(rolRequest);

        return rolMapper.toResponse(rolRepository.save(rol));

    }

    @Transactional(readOnly = true)
    @Override
    public List<RolResponse> listarRoles() {
        return rolRepository
                .findAll()
                .stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public RolResponse findById(Long id) {
        Rol rol =rolRepository.findById(id)
                .orElseThrow(()->new RolNotFoundException("rol no econtrado"+id));

        return rolMapper.toResponse(rol);
    }

    @Transactional
    @Override
    public RolResponse updateRol(Long id, RolRequest rolRequest) {
        Rol rol=rolRepository.findById(id)
                .orElseThrow(()->new RuntimeException("rol inexistente: "+ id));

        rolMapper.updateEntityFromRequest(rolRequest,rol);

        return rolMapper.toResponse(rolRepository.save(rol));
    }

    @Transactional(readOnly = true)
    @Override
    public void deleteRol(Long id) {
        Rol rol=rolRepository.findById(id)
                .orElseThrow(()->new RuntimeException("rol inexistente: "+ id));

        rolRepository.delete(rol);
    }
}

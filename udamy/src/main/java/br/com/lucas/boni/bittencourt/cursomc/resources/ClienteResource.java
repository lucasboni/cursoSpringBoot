package br.com.lucas.boni.bittencourt.cursomc.resources;

import br.com.lucas.boni.bittencourt.cursomc.domain.Cliente;
import br.com.lucas.boni.bittencourt.cursomc.dto.ClienteDTO;
import br.com.lucas.boni.bittencourt.cursomc.dto.ClienteNewDTO;
import br.com.lucas.boni.bittencourt.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cliente> find(@PathVariable Integer id) {

        Cliente buscar = service.find(id);
        return ResponseEntity.ok().body(buscar);
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> findAll() {

        List<Cliente> buscar = service.findAll();
        List<ClienteDTO> listDto = buscar.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "linerPage", defaultValue = "24") Integer linerPage,
                                                     @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<Cliente> buscar = service.findPage(page, linerPage, orderBy, direction);
        Page<ClienteDTO> listDto = buscar.map(obj -> new ClienteDTO(obj));
        return ResponseEntity.ok().body(listDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id) {
        Cliente obj = service.fromDT0(objDTO);
        obj.setId(id);
        obj = service.update(obj);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Cliente buscar = service.find(id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDT0) {      //@RequestBody faz o json ser convertido para o objeto
        Cliente obj = service.fromDT0(objDT0);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();//cria a urido novo recurso que foi criado
        return ResponseEntity.created(uri).build();

    }

}

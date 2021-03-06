package br.com.lucas.boni.bittencourt.cursomc.resources;

import br.com.lucas.boni.bittencourt.cursomc.domain.Categoria;
import br.com.lucas.boni.bittencourt.cursomc.domain.Pedido;
import br.com.lucas.boni.bittencourt.cursomc.domain.Produto;
import br.com.lucas.boni.bittencourt.cursomc.dto.CategoriaDTO;
import br.com.lucas.boni.bittencourt.cursomc.dto.ProdutoDTO;
import br.com.lucas.boni.bittencourt.cursomc.resources.utils.URL;
import br.com.lucas.boni.bittencourt.cursomc.services.PedidoService;
import br.com.lucas.boni.bittencourt.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Produto> find(@PathVariable Integer id) {

        Produto buscar = service.find(id);
        return ResponseEntity.ok().body(buscar);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ProdutoDTO>> findPage(@RequestParam(value = "nome", defaultValue = "") String nome,
                                                     @RequestParam(value = "categorias", defaultValue = "") String categorias,
                                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "linerPage", defaultValue = "24") Integer linerPage,
                                                     @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        List<Integer> ids = URL.decodeIntList(categorias);
        String nomeDecode = URL.decodeParam(nome);
        Page<Produto> buscar = service.search(nome,ids,page, linerPage, orderBy, direction);
        Page<ProdutoDTO> listDto = buscar.map(obj -> new ProdutoDTO(obj));
        return ResponseEntity.ok().body(listDto);
    }

}

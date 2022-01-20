package br.com.solucoescjm.mc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.solucoescjm.mc.domain.Categoria;
import br.com.solucoescjm.mc.repositories.CategoriaRepository;
import br.com.solucoescjm.mc.services.exception.DataIntegrityException;
import br.com.solucoescjm.mc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
		repo.deleteById(id);
		  }catch(DataIntegrityViolationException e ) { throw new
		  DataIntegrityException("Não é possível excluir categoria que tem produtos");
		  }
		 
	}
}

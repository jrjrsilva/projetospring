package br.com.solucoescjm.mc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solucoescjm.mc.domain.Cidade;
import br.com.solucoescjm.mc.domain.Cliente;
import br.com.solucoescjm.mc.domain.Endereco;
import br.com.solucoescjm.mc.domain.enums.TipoCliente;
import br.com.solucoescjm.mc.dto.ClienteDTO;
import br.com.solucoescjm.mc.dto.ClienteNewDTO;
import br.com.solucoescjm.mc.repositories.CidadeRepository;
import br.com.solucoescjm.mc.repositories.ClienteRepository;
import br.com.solucoescjm.mc.repositories.EnderecoRepository;
import br.com.solucoescjm.mc.services.exception.DataIntegrityException;
import br.com.solucoescjm.mc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository repoEndereco;

	@Autowired
	private CidadeRepository repoCidade;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		repo.save(obj);
		repoEndereco.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj,obj);
		return repo.save(newObj);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		// TODO Auto-generated method stub
		newObj.setNome(obj.getNome());	
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
		repo.deleteById(id);
		  }catch(DataIntegrityViolationException e ) { throw new
		  DataIntegrityException("Não é possível excluir Cliente que tem pedidos");
		  }
		 
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
		
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage,String orderBy, String direction ){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),	TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade();
		cid.setId(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(),
				 objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end); cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			 cli.getTelefones().add(objDto.getTelefone2()); } if (objDto.getTelefone3() !=
					  null) { cli.getTelefones().add(objDto.getTelefone3()); }
		
	
		return cli;
	}
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(),objDto.getEmail(), null, null);
	}
}

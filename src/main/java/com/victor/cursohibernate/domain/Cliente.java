package com.victor.cursohibernate.domain;

import static com.victor.cursohibernate.domain.enums.TipoCliente.getSafeTipoCliente;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.victor.cursohibernate.domain.enums.TipoCliente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Cliente implements Serializable
{
	//private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;
	private String nome;
	private String email;
	private String cpfOuCnpj;
	private Integer tipoCliente;

	//JsonManagedReference para com referencia ciclica, porem dava alguns problemas com json e foi usado apenas um @JsonIgnore no backReference
	@OneToMany(mappedBy = "cliente")
	private List<Endereco> enderecos = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "TELEFONE")
	private Set<String> telefones = new HashSet<>();

	//@JsonBackReference
	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<>();

	public Cliente()
	{

	}

	public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipoCliente)
	{
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfOuCnpj;
		this.tipoCliente = tipoCliente.getCode();
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCpfOuCnpj()
	{
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj)
	{
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public TipoCliente getTipoCliente()
	{
		TipoCliente tc = getSafeTipoCliente(this.tipoCliente);
		return tc;
	}

	public void setTipoCliente(Integer tipoCliente)
	{
		this.tipoCliente = tipoCliente;
	}

	public List<Pedido> getPedidos()
	{
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos)
	{
		this.pedidos = pedidos;
	}

	public void setTipoCliente(TipoCliente tipoCliente)
	{
		this.tipoCliente = tipoCliente.getCode();
	}

	public List<Endereco> getEnderecos()
	{
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos)
	{
		this.enderecos = enderecos;
	}

	public Set<String> getTelefones()
	{
		return telefones;
	}

	public void setTelefones(Set<String> telefones)
	{
		this.telefones = telefones;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		Cliente cliente = (Cliente) o;

		return id != null ? id.equals(cliente.id) : cliente.id == null;
	}

	@Override
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}
}
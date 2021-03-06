package com.victor.cursohibernate.services;

import com.victor.cursohibernate.domain.ItemPedido;
import com.victor.cursohibernate.domain.PagamentoComBoleto;
import com.victor.cursohibernate.domain.Pedido;
import com.victor.cursohibernate.domain.enums.EstadoPagamento;
import com.victor.cursohibernate.repositoriesDAO.ItemPedidoRepository;
import com.victor.cursohibernate.repositoriesDAO.PagamentoRepository;
import com.victor.cursohibernate.repositoriesDAO.PedidoRepository;
import com.victor.cursohibernate.services.exceptions.ObjectNotFoundException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService
{
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;

	public Pedido buscar(Integer id)
	{
		Optional<Pedido> obj = pedidoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
			"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscar(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());

		//System.out.println(obj);
		//emailService.sendOrderConfirmation(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
}

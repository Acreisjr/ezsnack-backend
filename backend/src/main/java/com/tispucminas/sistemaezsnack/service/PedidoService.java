package com.tispucminas.sistemaezsnack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tispucminas.sistemaezsnack.dto.CreatePedidoDTO;
import com.tispucminas.sistemaezsnack.dto.PedidoResponseDTO;
import com.tispucminas.sistemaezsnack.exception.IllegalOperationException;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.mapper.PedidoMapper;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;
import com.tispucminas.sistemaezsnack.model.ItemPedido;
import com.tispucminas.sistemaezsnack.model.Pedido;
import com.tispucminas.sistemaezsnack.model.Enums.PedidoStatusEnum;
import com.tispucminas.sistemaezsnack.repository.ContaRepository;
import com.tispucminas.sistemaezsnack.repository.ItemEstoqueCantinaRepository;
import com.tispucminas.sistemaezsnack.repository.PedidoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemEstoqueCantinaRepository itemRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    @Transactional
    public void createPedido(CreatePedidoDTO createPedidoDTO) throws NotFoundException, IllegalOperationException {
        Pedido pedido = new Pedido();
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());
        pedido.setData(LocalDateTime.now());
    
        List<ItemPedido> itensPedidos = createPedidoDTO.getItens().stream().map(dto -> {
            ItemEstoqueCantina item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item não encontrado!"));
    
            return ItemPedido.builder()
                .pedido(pedido)
                .quantidade(dto.getQuantidade())
                .itemEstoqueCantina(item)
                .valorTotal(calculaValorItem(dto.getQuantidade(), item.getPreco()))
                .build();
        }).collect(Collectors.toList());
    
        pedido.setItensPedidos(itensPedidos);
    
        BigDecimal valorTotal = calculaValorTotal(itensPedidos);
        pedido.setPrecoTotal(valorTotal);
    
        Conta conta = contaService.verificaSaldoSuficiente(createPedidoDTO.getContaId(), valorTotal);

        conta.resetLimiteDiarioCasoNovoDia();

        if (conta.isTemLimiteDiario()) {

            if (conta.getLimiteDiario().compareTo(valorTotal) < 0) {
                throw new IllegalOperationException("Pedido excede o limite diário da conta.");
            }
    
            conta.setLimiteDiario(conta.getLimiteDiario().subtract(valorTotal));
            contaRepository.save(conta);
        }

        pedido.setConta(conta);
    
        pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido negarPedido(Long id) throws Exception {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("Pedido não encontrado"));

        if (!pedido.getStatus().equals(PedidoStatusEnum.PENDENTE.getDescricao())) {
            throw new Exception("Apenas pedidos pendentes podem ser negados.");
        }

        pedido.setStatus(PedidoStatusEnum.NEGADO.getDescricao());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido aprovarPedido(Long id) throws Exception {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("Pedido não encontrado"));

        if (!pedido.getStatus().equals(PedidoStatusEnum.PENDENTE.getDescricao())) {
            throw new Exception("Apenas pedidos pendentes podem ser aprovados.");
        }

        if (pedido.getConta().getSaldo().compareTo(pedido.getPrecoTotal()) < 0) {
            throw new IllegalOperationException("Saldo insuficiente do aluno para realizar pedido.");
        }
    
        Conta conta = pedido.getConta();
        conta.setSaldo(conta.getSaldo().subtract(pedido.getPrecoTotal()));
        contaRepository.save(conta);
        
        pedido.setStatus(PedidoStatusEnum.APROVADO.getDescricao());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido marcarEntreguePedido(Long id) throws Exception {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

        if (!pedido.getStatus().equals(PedidoStatusEnum.APROVADO.getDescricao())) {
            throw new Exception("Apenas pedidos aprovados podem ser marcados como entregue.");
        }

        Conta conta = pedido.getConta();

        conta.setSaldo(conta.getSaldo().subtract(pedido.getPrecoTotal()));
        
        contaRepository.save(conta);

        pedido.setStatus(PedidoStatusEnum.ENTREGUE.getDescricao());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelarPedido(Long id) throws Exception {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("Pedido não encontrado"));

        if (!pedido.getStatus().equals(PedidoStatusEnum.APROVADO.getDescricao())) {
            throw new Exception("Apenas pedidos aprovados podem ser cancelados.");
        }
        
        // Retornar o saldo à conta quando o pedido for cancelado
        Conta conta = pedido.getConta();
        conta.setSaldo(conta.getSaldo().add(pedido.getPrecoTotal()));
        contaRepository.save(conta);

        pedido.setStatus(PedidoStatusEnum.CANCELADO.getDescricao());
        return pedidoRepository.save(pedido);
    }

    public PedidoResponseDTO getPedidoById(Long id) throws NotFoundException {

        Optional<Pedido> optionalPedido = pedidoRepository.findById(id);

        if(optionalPedido.isPresent()){
            return PedidoMapper.INSTANCE.toResponse(optionalPedido.get());
        }
        throw new NotFoundException("Pedido não encontrado");
    }

    public List<PedidoResponseDTO> getAllPedidos(Long escolaId, Long alunoId) {
        
        if(escolaId != null){
            if(alunoId != null){
                return PedidoMapper.INSTANCE.toResponseList(pedidoRepository.findByEscolaIdAndAlunoId(escolaId, alunoId));
            }
            return PedidoMapper.INSTANCE.toResponseList(pedidoRepository.findByEscolaId(escolaId));
        }
        
        return PedidoMapper.INSTANCE.toResponseList(pedidoRepository.findAll());
    }

    @Transactional
    public void deletePedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    private BigDecimal calculaValorItem(int quantidade, BigDecimal preco) {
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }

    private BigDecimal calculaValorTotal(List<ItemPedido> itensPedido){
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido itemPedido : itensPedido) {
            total = total.add(itemPedido.getValorTotal());
        }
        return total;
    }
}

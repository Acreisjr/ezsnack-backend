package com.tispucminas.sistemaezsnack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tispucminas.sistemaezsnack.dto.ContaResponseDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateResponsavelDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateSaldoDTO;
import com.tispucminas.sistemaezsnack.exception.IllegalOperationException;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.mapper.ContaMapper;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.repository.ContaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class ContaService {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ContaRepository contaRepository;

    public ContaResponseDTO getById(Long id) throws NotFoundException{
        Optional<Conta> contaOptional = contaRepository.findById(id);

        if(contaOptional.isPresent()){
            return ContaMapper.INSTANCE.contaToContaResponseDTO(contaOptional.get());
        }

        throw new NotFoundException("Conta não encontrada!");
    }

     public List<ContaResponseDTO> findAll() {
        List<Conta> contas = contaRepository.findAll();

        return contas.stream()
                     .map(ContaMapper.INSTANCE::contaToContaResponseDTO)
                     .collect(Collectors.toList());
    }

    public Conta create(Aluno aluno) {
        Conta conta = Conta.builder().build();
        conta.setAluno(aluno);
        conta.setResponsavel(aluno.getResponsavel());
        conta.setSaldo(BigDecimal.valueOf(0.0));

        return contaRepository.save(conta);
    }

    public Conta updateResponsavel(Long id, ContaUpdateResponsavelDTO dto) throws Exception {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new Exception("Conta não encontrada!"));

        Responsavel responsavel = (Responsavel) usuarioService.getById(dto.getResponsavelId());

        conta.setResponsavel(responsavel);

        return contaRepository.save(conta);
    }

    public Conta aumentarSaldo(Long id, ContaUpdateSaldoDTO dto) throws Exception {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new Exception("Conta não encontrada!"));

        conta.aumentarSaldo(dto.getSaldo());

        return contaRepository.save(conta);
    }

    public Conta diminuirSaldo(Long id, ContaUpdateSaldoDTO dto) throws Exception {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new Exception("Conta não encontrada!"));

        conta.diminuirSaldo(dto.getSaldo());

        return contaRepository.save(conta);
    }


    public void debitaValorTotal(Long id, BigDecimal valorTotal) throws IllegalOperationException, NotFoundException{
        
        Conta conta = verificaSaldoSuficiente(id, valorTotal);

        conta.setSaldo(conta.getSaldo().subtract(valorTotal));

        contaRepository.save(conta);
    }

    public Conta verificaSaldoSuficiente(Long id, BigDecimal valorTotal) throws NotFoundException, IllegalOperationException{
        
        Optional<Conta> contaOptional = contaRepository.findById(id);

        Conta conta;

        if(contaOptional.isPresent()){
            conta = contaOptional.get();
            if(conta.getSaldo().compareTo(valorTotal) == -1){
                throw new IllegalOperationException("Saldo insuficiente !");
            }
        
            return conta;
        }
        throw new NotFoundException("Conta não encontrada!");
    }

    public Conta setTemLimiteDiario(Long id, boolean temLimiteDiario) throws NotFoundException, Exception {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new Exception("Conta não encontrada!"));
        conta.setTemLimiteDiario(temLimiteDiario);
        return contaRepository.save(conta);
    }

    public Conta updateLimiteDiario(Long id, BigDecimal novoLimite) throws NotFoundException, Exception {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new Exception("Conta não encontrada!"));

        if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite diário não pode ser negativo.");
        }

        conta.setLimiteDiarioPadrao(novoLimite);

        conta.resetLimiteDiarioCasoNovoDia();

        return contaRepository.save(conta);
    }
    
}

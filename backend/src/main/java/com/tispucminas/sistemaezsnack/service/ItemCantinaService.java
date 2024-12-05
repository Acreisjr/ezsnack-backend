package com.tispucminas.sistemaezsnack.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tispucminas.sistemaezsnack.dto.ItemCantinaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.ItemCantinaUpdateDTO;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;
import com.tispucminas.sistemaezsnack.repository.EscolaRepository;
import com.tispucminas.sistemaezsnack.repository.ItemCantinaRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ItemCantinaService {

    private final ItemCantinaRepository itemCantinaRepository;
    private final EscolaRepository escolaRepository; 
    private static final Logger LOGGER = Logger.getLogger(ItemCantinaService.class.getName());

    public List<ItemEstoqueCantina> findAll(Long escolaId) {
        List<ItemEstoqueCantina> itens;
        LOGGER.info("Chamando método findAll para listar todos os itens.");
        if(escolaId != null){
            itens = itemCantinaRepository.findAllByEscolaId(escolaId);
        }else{
            itens = itemCantinaRepository.findAll();
        }
        LOGGER.info("Número de itens encontrados: " + itens.size());
        return itens;
    }

    public ItemEstoqueCantina findById(Long id) {
        LOGGER.info("Chamando método findById para buscar o item com ID: " + id);
        Optional<ItemEstoqueCantina> itemCantina = itemCantinaRepository.findById(id);
        if (itemCantina.isPresent()) {
            LOGGER.info("Item encontrado com ID: " + id);
            return itemCantina.get();
        } else {
            LOGGER.warning("Item não encontrado com ID: " + id);
            return null;
        }
    }

    public ItemEstoqueCantina create(ItemCantinaCreateDTO dto) {
        LOGGER.info("Chamando método create para criar um novo item.");
        try {
            ItemEstoqueCantina itemCantina = new ItemEstoqueCantina();
            itemCantina.setNome(dto.getNome());
            itemCantina.setTipo(dto.getTipo());
            itemCantina.setDisponivel(dto.getDisponibilidade());
            itemCantina.setPreco(dto.getPreco());

            // Busca a escola pelo ID
            Escola escola = escolaRepository.findById(dto.getEscolaId())
                    .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada com o ID fornecido"));
            
            // Atribui a escola ao item
            itemCantina.setEscola(escola);

            ItemEstoqueCantina savedItem = itemCantinaRepository.save(itemCantina);
            LOGGER.info("Item criado com sucesso: " + savedItem.getId());
            return savedItem;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao criar item", e);
            return null;
        }
    }

    public ItemEstoqueCantina update(Long id, ItemCantinaUpdateDTO dto) {
        LOGGER.info("Chamando método update para atualizar o item com ID: " + id);
        try {
            Optional<ItemEstoqueCantina> existingItem = itemCantinaRepository.findById(id);
            if (existingItem.isPresent()) {
                ItemEstoqueCantina itemCantina = existingItem.get();
                itemCantina.setNome(dto.getNome());
                itemCantina.setTipo(dto.getTipo());
                itemCantina.setDisponivel(dto.getDisponibilidade());
                itemCantina.setPreco(dto.getPreco());
                
                ItemEstoqueCantina updatedItem = itemCantinaRepository.save(itemCantina);
                LOGGER.info("Item atualizado com sucesso: " + updatedItem.getId());
                return updatedItem;
            } else {
                LOGGER.warning("Item não encontrado com ID: " + id);
                return null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar item com ID: " + id, e);
            return null;
        }
    }

    public void delete(Long id) {
        LOGGER.info("Chamando método delete para remover o item com ID: " + id);
        try {
            if (itemCantinaRepository.existsById(id)) {
                itemCantinaRepository.deleteById(id);
                LOGGER.info("Item deletado com sucesso: " + id);
            } else {
                LOGGER.warning("Item não encontrado com ID: " + id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar item com ID: " + id, e);
        }
    }
}

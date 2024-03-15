package com.techchallenge.restaurant.api.findfood.domain.service;

import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Reserva;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.ReservaRepository;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;

    private final RestauranteRepository restauranteRepository;

    private final ModelMapper modelMapper;

    public ReservaDTO reservarMesa(Long restauranteId, ReservaDTO reservaDTO){
        Optional<Restaurante> optionalRestaurante = restauranteRepository.findById(restauranteId);
        if (optionalRestaurante.isEmpty()) {
            throw new EntityNotFoundException("Restaurante não existe");
        }

        Reserva reserva = modelMapper.map(reservaDTO, Reserva.class);
        reserva.setRestaurante(optionalRestaurante.get());

        if(haMesasDisponiveis(reserva)){
            return modelMapper.map(reservaRepository.save(reserva), ReservaDTO.class);
        } else {
            throw new IllegalArgumentException("Não há lugares disponíveis nesse horário para o Restaurante: " + optionalRestaurante.get().getNome());
        }
    }

    private boolean haMesasDisponiveis(Reserva reserva) {
        final int qtdePessoasPorMesa = 4;
        int qtdeTotalDeMesas = reserva.getRestaurante().getQuantidadeTotalDeMesas();

        List<Reserva> listaDeReservas = reservaRepository.findReservasNoIntervaloDaNovaReservaSolicitada(reserva.getRestaurante(), reserva.getDataHoraInicio(), reserva.getDataHoraFim());
        int totalDeMesasReservadas = listaDeReservas.stream()
                .mapToInt(value -> (int) Math.ceil((double)  value.getQtdPessoas() / qtdePessoasPorMesa))
                .sum();

        int qtdeTotalMesasLivres = qtdeTotalDeMesas - totalDeMesasReservadas;
        int mesasNecesariasParaReserva = (int) Math.ceil((double)  reserva.getQtdPessoas() / qtdePessoasPorMesa);

        return qtdeTotalMesasLivres >= mesasNecesariasParaReserva;
    }

    public Collection<ReservaDTO> findAll(Long restauranteId) {
        Optional<Restaurante> optionalRestaurante = restauranteRepository.findById(restauranteId);

        if (optionalRestaurante.isEmpty()) {
            throw new EntityNotFoundException("Restaurante não existe");
        }

        List<Reserva> reservaList = reservaRepository.findAllByRestaurante(optionalRestaurante.get());
        return reservaList.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .toList();
    }

    public ReservaDTO findById(Long id) {
        var reserva = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada!"));
        return modelMapper.map(reserva, ReservaDTO.class);
    }

    public void delete(Long id) {
        reservaRepository.deleteById(id);
    }

}

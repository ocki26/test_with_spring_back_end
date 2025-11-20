package com.app_language.hoctiengtrung_online.Ticket.mapper;


import com.app_language.hoctiengtrung_online.Ticket.dto.TicketDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper {

    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setShowId(ticket.getShowId());
        dto.setShowName(ticket.getShowName());
        dto.setTicketTypeId(ticket.getTicketTypeId());
        dto.setTicketTypeName(ticket.getTicketTypeName());
        dto.setPrice(ticket.getPrice());
        dto.setBuyerName(ticket.getBuyerName());
        dto.setBuyerEmail(ticket.getBuyerEmail());
        dto.setBuyerPhone(ticket.getBuyerPhone());
        dto.setStatus(ticket.getStatus());
        dto.setPurchaseTime(ticket.getPurchaseTime());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setScannedBy(ticket.getScannedBy());
        dto.setSoldBy(ticket.getSoldBy());
        return dto;
    }

    public List<TicketDTO> toDTOList(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
package com.bolao.v1.api.rest.controller.campeonato.hateoas;

import com.bolao.v1.api.rest.controller.campeonato.CampeonatoController;
import com.bolao.v1.api.rest.controller.campeonato.CampeonatoControllerImpl;
import com.bolao.v1.core.port.in.dto.response.campeonato.CampeonatoResponseDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasResponseBuilder {

    public static CampeonatoResponseDto addLinksHateoas(CampeonatoResponseDto response) {
        if (response == null || response.getId() == null) {
            return response;
        }

        response.add(linkTo(methodOn(CampeonatoControllerImpl.class).getById(Math.toIntExact(response.getId()))).withSelfRel());

        response.add(linkTo(methodOn(CampeonatoControllerImpl.class).getAll()).withRel("todos-campeonatos"));

        return response;
    }
}
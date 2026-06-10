package com.bolao.v1.api.rest.controller.partida.hateoas;

import com.bolao.v1.api.rest.controller.partida.PartidaController;
import com.bolao.v1.core.port.in.dto.response.partida.PartidaResponseDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasResponseBuilder {

    private HateoasResponseBuilder() {
    }

    public static PartidaResponseDto addLinks(PartidaResponseDto response) {
        if (response == null || response.getId() == null) {
            return response;
        }

        Integer id = response.getId();


        response.add(linkTo(methodOn(PartidaController.class).getById(id)).withSelfRel());


        response.add(linkTo(methodOn(PartidaController.class).getAll()).withRel("todas-partidas"));


        response.add(linkTo(methodOn(PartidaController.class).iniciar(id)).withRel("iniciar-partida"));


        response.add(linkTo(methodOn(PartidaController.class).finalizar(id, null)).withRel("finalizar-partida"));

        return response;
    }
}

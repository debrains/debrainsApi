package com.debrains.debrainsApi.util;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PagedModelUtil<T> extends EntityModel<T> {

    public static <T> PagedModel<EntityModel<T>> getEntityModels(PagedResourcesAssembler<T> assembler,
                                                                 Page<T> page,
                                                                 WebMvcLinkBuilder builder,
                                                                 Function<T, ?> selfLinkFunc) {
        return assembler.toModel(page, model -> of(builder, model, selfLinkFunc::apply));
    }

    public static <T> EntityModel<T> of(WebMvcLinkBuilder builder, T model, Function<T, ?> resourceFunc) {
        return EntityModel.of(model, getSelfLink(builder, model, resourceFunc));
    }

    private static <T> Link getSelfLink(WebMvcLinkBuilder builder, T data, Function<T, ?> resourceFunc) {
        WebMvcLinkBuilder slash = builder.slash(resourceFunc.apply(data));
        return slash.withSelfRel();
    }
}

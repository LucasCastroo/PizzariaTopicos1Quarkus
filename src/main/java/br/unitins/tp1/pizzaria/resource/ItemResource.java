package br.unitins.tp1.pizzaria.resource;

import br.unitins.tp1.pizzaria.application.Error;
import br.unitins.tp1.pizzaria.dto.ItemDTO;
import br.unitins.tp1.pizzaria.form.ItemImageForm;
import br.unitins.tp1.pizzaria.model.NivelAcesso;
import br.unitins.tp1.pizzaria.service.ItemFileService;
import br.unitins.tp1.pizzaria.service.ItemService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class ItemResource<S extends ItemService, DTO extends ItemDTO> {
    @Inject
    S service;

    @Inject
    ItemFileService fileService;

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @POST
    @Path("/create/")
    @RolesAllowed({NivelAcesso.Role.GERENTE, NivelAcesso.Role.ADMIN})
    public Response create(DTO dto) {
        LOG.infof("Item %s cadastrado", dto.nome);
        return Response.status(Response.Status.CREATED).entity(service.create(dto)).build();
    }

    @PUT
    @Path("/update/{id}")
    @RolesAllowed({NivelAcesso.Role.GERENTE, NivelAcesso.Role.ADMIN})
    public Response update(DTO dto, @PathParam("id") Long id) {
        LOG.infof("Item %s autualizado", dto.nome);
        return Response.status(Response.Status.ACCEPTED).entity(service.update(id, dto)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok().entity(service.findById(id)).build();
    }

    @GET
    @Path("/image/{nomeImagem}")
    @PermitAll
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        ResponseBuilder response = Response.ok(fileService.obter(nomeImagem));
        response.header("Content-Disposition", "attachment;filename=" + nomeImagem);
        return response.build();
    }

    @PATCH
    @Path("/set-image/{id}")
    @RolesAllowed({NivelAcesso.Role.GERENTE, NivelAcesso.Role.ADMIN})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@MultipartForm ItemImageForm form, @PathParam("id") Long id) {
        String nomeImagem;
        LOG.infof("Imagem do item %d autualizada", id);
        try {
            nomeImagem = fileService.salvar(form.getNomeImagem(), form.getImagem());
        } catch (IOException e) {
            e.printStackTrace();
            Error error = new Error("409", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        return Response.ok(service.updateImage(id, nomeImagem)).build();

    }
}
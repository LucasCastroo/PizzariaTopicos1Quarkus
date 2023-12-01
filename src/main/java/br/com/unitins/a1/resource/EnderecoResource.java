package br.com.unitins.a1.resource;

import br.com.unitins.a1.dto.EnderecoDTO;
import br.com.unitins.a1.dto.EnderecoResponseDTO;
import br.com.unitins.a1.service.EnderecoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {
    @Inject
    EnderecoService service;

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @POST
    @Path("/criar/{idCliente}")
    public Response insert(@Valid EnderecoDTO dto, @PathParam("idCliente") Long idCliente) {
        EnderecoResponseDTO retorno = service.insert(dto, idCliente);
        LOG.info("Novo endereço adicionado!");
        return Response.status(201).entity(retorno).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(EnderecoDTO dto, @PathParam("id") Long idEndereco) {
        service.update(dto, idEndereco);
        LOG.info("Endereço atualizado!");
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        LOG.info("Endereço deletado!");
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/search/id/{id}")
    public Response findById(@PathParam("id") Long id) {
        LOG.info("Busca de endereço por ID!");
        return Response.ok(service.findById(id)).build();
    }

    @GET
    @Path("/search/logradouro/{logradouro}")
    public Response findByNome(@PathParam("logradouro") String logradouro) {
        LOG.info("Busca de endereço por LOGRADOURO!");
        return Response.ok(service.findByLogradouro(logradouro)).build();
    }
}

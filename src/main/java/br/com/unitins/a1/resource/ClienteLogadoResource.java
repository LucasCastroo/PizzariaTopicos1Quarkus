package br.com.unitins.a1.resource;

import br.com.unitins.a1.dto.AlterarSenhaDTO;
import br.com.unitins.a1.model.Cliente;
import br.com.unitins.a1.service.ClienteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/minha-conta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed(Cliente.ROLE)
public class ClienteLogadoResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    ClienteService clienteService;

    @GET
    public Response minhaConta() {
        return Response.ok(clienteService.findById(Long.valueOf(jwt.getSubject()))).build();
    }

    @PATCH
    @Path("/alterar-senha")
    public Response alterarSenha(@Valid AlterarSenhaDTO dto) {
        //TODO validação
        if(clienteService.alterarSenha(dto, Long.valueOf(jwt.getSubject()))){
            return Response.noContent().build();
        }
        return Response.serverError().build();
    }
}

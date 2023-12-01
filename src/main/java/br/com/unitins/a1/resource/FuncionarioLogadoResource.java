package br.com.unitins.a1.resource;

import br.com.unitins.a1.dto.AlterarSenhaDTO;
import br.com.unitins.a1.model.Funcionario;
import br.com.unitins.a1.service.FuncionarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@Path("/meu-perfil")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed(Funcionario.ROLE)
public class FuncionarioLogadoResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    FuncionarioService funcionarioService;

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @GET
    public Response minhaConta() {
        LOG.info("Consulta da conta!");
        return Response.ok(funcionarioService.findById(Long.valueOf(jwt.getSubject()))).build();
    }

    @PATCH
    @Path("/alterar-senha")
    public Response alterarSenha(@Valid AlterarSenhaDTO dto) {
        if(funcionarioService.alterarSenha(dto, Long.valueOf(jwt.getSubject()))){
            LOG.info("Senha alterada!");
            return Response.noContent().build();
        }
        return Response.serverError().build();
    }
}

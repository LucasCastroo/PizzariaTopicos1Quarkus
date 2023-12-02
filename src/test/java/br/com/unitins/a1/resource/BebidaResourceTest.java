package br.com.unitins.a1.resource;

import br.com.unitins.a1.dto.BebidaDTO;
import br.com.unitins.a1.dto.BebidaResponseDTO;
import br.com.unitins.a1.model.Bebida;
import br.com.unitins.a1.service.BebidaServiceImpl;
import br.com.unitins.a1.service.JwtService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
public class BebidaResourceTest {

    @Inject
    BebidaServiceImpl bebidaService;

    @Inject
    JwtService jwtService;

    @Test
    void createBebida() {
        BebidaDTO dto = new BebidaDTO(
                "Coca-Cola",
                "Bebida Gelada",
                4.0,
                100,
                350
        );

        given()
                .contentType(ContentType.JSON)
                .header(TestUtils.authFuncionario)
                .body(dto)
                .when().post("/item/bebida")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "nome", is("Coca-Cola"),
                        "descricao", is("Bebida Gelada"),
                        "preco", is(4.0f),
                        "kCal", is(100),
                        "ml", is(350)
                );
    }

    @Test
    void updateBebida() {
        BebidaDTO dto = new BebidaDTO(
                "Coca-Cola",
                "Bebida Gelada",
                4.0,
                100,
                350
        );

        BebidaResponseDTO bebidaTest = bebidaService.create(dto);
        Long id = bebidaTest.getId();

        BebidaDTO dtoUpdate = new BebidaDTO(
                "Coca-Cola",
                "Bebida Gelada",
                6.0,
                100,
                350
        );

        given()
                .contentType(ContentType.JSON)
                .header(TestUtils.authFuncionario)
                .body(dtoUpdate)
                .when().put("/item/bebida/"+ id)
                .then()
                .statusCode(202);

        BebidaResponseDTO beb = bebidaService.findById(id);
        assertThat(beb.getNome(), is("Coca-Cola"));
        assertThat(beb.getDescricao(), is("Bebida Gelada"));
        assertThat(beb.getPreco(), is(6.0));
        assertThat(beb.getkCal(), is(100));
        assertThat(beb.getMl(), is(350));
    }

    @Test
    void findBebida() {
        BebidaDTO dto = new BebidaDTO(
                "Coca-Cola",
                "Bebida Gelada",
                4.0,
                100,
                350
        );

        BebidaResponseDTO bebidaTest = bebidaService.create(dto);

        given()
                .when()
                .header(TestUtils.authFuncionario)
                .get("/item/bebida/" + bebidaTest.getId())
                .then()
                .statusCode(200);
    }
}

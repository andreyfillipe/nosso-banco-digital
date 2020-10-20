package com.andreyfillipe.nossobancodigital.resource;

import com.andreyfillipe.nossobancodigital.entity.Endereco;
import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.dto.AceitePropostaDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.ClienteDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.EnderecoDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.MensagemDTO;
import com.andreyfillipe.nossobancodigital.entity.enums.StatusProposta;
import com.andreyfillipe.nossobancodigital.service.PropostaArquivoService;
import com.andreyfillipe.nossobancodigital.service.PropostaEnderecoService;
import com.andreyfillipe.nossobancodigital.service.PropostaService;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PropostaResource.class)
@AutoConfigureMockMvc
public class PropostaResourceTest {

    static String API = "/api/propostas";

    PropostaResource propostaResource;

    @Autowired
    MockMvc mvc;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    PropostaService propostaService;

    @MockBean
    PropostaEnderecoService propostaEnderecoService;

    @MockBean
    PropostaArquivoService propostaArquivoService;

    @BeforeEach
    public void setUp() {
        this.propostaResource = new PropostaResource(modelMapper, propostaService, propostaEnderecoService, propostaArquivoService);
    }

    @Test
    @DisplayName("Salvar uma proposta com sucesso")
    public void salvarPropostaTest() throws Exception {
        Integer id = 1;
        ClienteDTO dto = criarCliente();
        Proposta proposta = criarProposta();
        proposta.setId(id);
        Mockito.when(modelMapper.map(dto, Proposta.class)).thenReturn(proposta);
        Mockito.when(propostaService.salvar(Mockito.any(Proposta.class))).thenReturn(proposta);
        String json = new ObjectMapper().writeValueAsString(criarJsonCliente());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(this.API)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
           .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Retornar exceção de validação ao salvar proposta, quando não houver dados suficientes para salvar")
    public void salvarPropostaValidarDadosTest() throws Exception {
        Proposta proposta = criarProposta();
        String json = new ObjectMapper().writeValueAsString(new ClienteDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(this.API)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("erros", hasSize(5)));
        Mockito.verify(propostaService, Mockito.never()).salvar(proposta);
    }

    @Test
    @DisplayName("Salvar o endereço da proposta com sucesso")
    public void salvarPropostaEnderecoTest() throws Exception {
        Integer id = 1;
        Proposta proposta = criarProposta();
        proposta.setId(id);
        Endereco endereco = criarEndereco();
        endereco.setId(id);
        EnderecoDTO dto = criarEnderecoDTO();
        Mockito.when(propostaService.filtrarPorId(Mockito.anyInt())).thenReturn(proposta);
        Mockito.when(modelMapper.map(dto, Endereco.class)).thenReturn(endereco);
        Mockito.when(propostaEnderecoService.salvar(Mockito.any(Endereco.class))).thenReturn(endereco);
        String json = new ObjectMapper().writeValueAsString(dto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(this.API.concat("/" + id + "/enderecos"))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
           .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Retornar exceção de validação ao salvar endereço da proposta, quando não encontrar o id da proposta informada")
    public void salvarPropostaEnderecoValidarPropostaExisteTest() throws Exception {
        Integer id = 1;
        String msgValidacao = "Código de proposta não encontrado";
        EnderecoDTO enderecoDTO = criarEnderecoDTO();
        Endereco endereco = criarEndereco();
        Mockito.when(propostaService.filtrarPorId(Mockito.anyInt())).thenThrow(new NaoEncontradoException(msgValidacao));
        String json = new ObjectMapper().writeValueAsString(enderecoDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(this.API.concat("/" + id + "/enderecos"))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("erros", hasSize(1)))
           .andExpect(jsonPath("erros[0]").value(msgValidacao));
        Mockito.verify(propostaEnderecoService, Mockito.never()).salvar(endereco);
    }

    @Test
    @DisplayName("Retornar exceção de validação ao salvar endereço da proposta, quando não houver dados suficientes para salvar")
    public void salvarPropostaEnderecoValidarDadosTest() throws Exception {
        Integer id = 1;
        Proposta proposta = criarProposta();
        proposta.setId(id);
        Endereco endereco = criarEndereco();
        Mockito.when(propostaService.filtrarPorId(Mockito.anyInt())).thenReturn(proposta);
        String json = new ObjectMapper().writeValueAsString(new EnderecoDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(this.API.concat("/" + id + "/enderecos"))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("erros", hasSize(6)));
        Mockito.verify(propostaEnderecoService, Mockito.never()).salvar(endereco);
    }

    @Test
    @DisplayName("Filtrar proposta com sucesso")
    public void filtrarPropostaTest() throws Exception {
        Integer id = 1;
        Proposta proposta = criarProposta();
        proposta.setId(id);
        Mockito.when(propostaService.filtrarProposta(Mockito.anyInt())).thenReturn(proposta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(this.API.concat("/" + id))
                                                                      .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
           .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retornar exceção ao filtrar por proposta, quando ele não existe na base")
    public void filtrarPropostaNaoEncontradoTest() throws Exception {
        Integer id = 1;
        String msgValidacao = "Código de proposta não encontrado";
        Mockito.when(propostaService.filtrarProposta(Mockito.anyInt())).thenThrow(new NaoEncontradoException(msgValidacao));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(this.API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("erros", hasSize(1)))
           .andExpect(jsonPath("erros[0]").value(msgValidacao));
    }

    @Test
    @DisplayName("Aceite de proposta com sucesso")
    public void aceitePropostaTest() throws Exception {
        Integer id = 1;
        Proposta proposta = criarProposta();
        MensagemDTO mensagemDTO = MensagemDTO.builder().mensagem("OK").build();
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(true).build();
        Mockito.when(propostaService.filtrarProposta(Mockito.anyInt())).thenReturn(proposta);
        Mockito.when(propostaService.aceiteProposta(proposta, aceitePropostaDTO)).thenReturn(mensagemDTO);
        String json = new ObjectMapper().writeValueAsString(aceitePropostaDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(this.API.concat("/" + id))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
           .andExpect(status().isOk())
           .andExpect(jsonPath("mensagem").value("OK"));
    }

    @Test
    @DisplayName("Retornar exceção ao aceite de proposta, quando a proposta não existe na base")
    public void aceitePropostaNaoEncontradoTest() throws Exception {
        Integer id = 1;
        String msgValidacao = "Código de proposta não encontrado";
        Proposta proposta = criarProposta();
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(true).build();
        Mockito.when(propostaService.filtrarProposta(Mockito.anyInt())).thenThrow(new NaoEncontradoException(msgValidacao));
        String json = new ObjectMapper().writeValueAsString(aceitePropostaDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(this.API.concat("/" + id))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("erros", hasSize(1)))
                .andExpect(jsonPath("erros[0]").value(msgValidacao));
        Mockito.verify(propostaService, Mockito.never()).aceiteProposta(proposta ,aceitePropostaDTO);
    }

    private Proposta criarProposta() {
        return Proposta.builder()
                       .nome("nome")
                       .sobrenome("sobrenome")
                       .email("email@email.com.br")
                       .cpf("12345678909")
                       .dataNascimento(LocalDate.of(1990, 10, 20))
                       .status(StatusProposta.LIBERADA)
                       .build();
    }

    private Endereco criarEndereco() {
        return Endereco.builder()
                       .cep("00000-00")
                       .rua("rua")
                       .bairro("bairro")
                       .complemento("Complemento")
                       .cidade("Cidade")
                       .estado("MG")
                       .build();
    }

    private ClienteDTO criarCliente() {
        return ClienteDTO.builder()
                         .nome("nome")
                         .sobrenome("sobrenome")
                         .email("email@email.com.br")
                         .cpf("12345678909")
                         .dataNascimento(LocalDate.of(1990, 10, 20))
                         .build();
    }

    private EnderecoDTO criarEnderecoDTO() {
        return EnderecoDTO.builder()
                          .cep("00000-00")
                          .rua("rua")
                          .bairro("bairro")
                          .complemento("Complemento")
                          .cidade("Cidade")
                          .estado("MG")
                          .build();
    }

    private Map criarJsonCliente() {
        Map<String, String> json = new HashMap<>();
        json.put("nome", "nome");
        json.put("sobrenome", "sobrenome");
        json.put("email", "email@email.com.br");
        json.put("cpf", "12345678909");
        json.put("dataNascimento", "1990-10-20");
        return json;
    }
}

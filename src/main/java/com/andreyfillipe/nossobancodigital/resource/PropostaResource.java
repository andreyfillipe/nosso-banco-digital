package com.andreyfillipe.nossobancodigital.resource;

import com.andreyfillipe.nossobancodigital.entity.Arquivo;
import com.andreyfillipe.nossobancodigital.entity.Endereco;
import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.dto.*;
import com.andreyfillipe.nossobancodigital.service.PropostaArquivoService;
import com.andreyfillipe.nossobancodigital.service.PropostaEnderecoService;
import com.andreyfillipe.nossobancodigital.service.PropostaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/propostas")
public class PropostaResource {

    private ModelMapper modelMapper;
    private PropostaService propostaService;
    private PropostaEnderecoService propostaEnderecoService;
    private PropostaArquivoService propostaArquivoService;

    public PropostaResource(ModelMapper modelMapper, PropostaService propostaService, PropostaEnderecoService propostaEnderecoService,
                            PropostaArquivoService propostaArquivoService) {
        this.modelMapper = modelMapper;
        this.propostaService = propostaService;
        this.propostaEnderecoService = propostaEnderecoService;
        this.propostaArquivoService = propostaArquivoService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarProposta(@RequestBody @Valid ClienteDTO dto) {
        //Converter dto para entity
        Proposta proposta = this.modelMapper.map(dto, Proposta.class);
        //Salvar proposta
        proposta = this.propostaService.salvar(proposta);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/propostas/{id}/enderecos").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("{id}/enderecos")
    public ResponseEntity<Void> salvarEndereco(@PathVariable Integer id, @RequestBody @Valid EnderecoDTO dto) {
        //Filtrar proposta por id
        Proposta proposta = this.propostaService.filtrarPorId(id);
        //Converter dto para entity e vincular proposta
        Endereco endereco = this.modelMapper.map(dto, Endereco.class);
        endereco.setProposta(proposta);
        //Salvar endereço da proposta
        endereco = this.propostaEnderecoService.salvar(endereco);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/propostas/{id}/arquivos").buildAndExpand(endereco.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("{id}/arquivos")
    public ResponseEntity<Void> salvarArquivo(@PathVariable Integer id, @RequestParam MultipartFile arq) {
        //Filtrar proposta por id
        Proposta proposta = this.propostaService.filtrarPorId(id);
        //Criar arquivo e vincular proposta
        Arquivo arquivo = new Arquivo();
        arquivo.setProposta(proposta);
        //Salvar arquivo da proposta
        arquivo = this.propostaArquivoService.salvar(arquivo, arq);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/propostas/{id}").buildAndExpand(arquivo.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<PropostaDTO> filtrarProposta(@PathVariable Integer id) {
        //Filtrar proposta por id e validando passos anteriores
        Proposta proposta = this.propostaService.filtrarProposta(id);
        //Converter entity para dto e retornar dados da proposta
        PropostaDTO dto = this.modelMapper.map(proposta, PropostaDTO.class);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<MensagemDTO> aceiteProposta(@PathVariable Integer id, @RequestBody @Valid AceitePropostaDTO dto) {
        //Filtrar proposta por id e validando passos anteriores
        Proposta proposta = this.propostaService.filtrarProposta(id);
        //Realizar aceite da proposta
        MensagemDTO mensagem = this.propostaService.aceiteProposta(proposta, dto);
        return ResponseEntity.ok().body(mensagem);
    }
}

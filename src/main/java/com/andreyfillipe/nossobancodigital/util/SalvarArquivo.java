package com.andreyfillipe.nossobancodigital.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SalvarArquivo {

    public static String salvar(String raiz, String diretorio, MultipartFile arquivo) {
        //Gerar caminho do diretório onde será salvo o arquivo
        Path caminhoDiretorio = Paths.get(raiz, diretorio);
        //Gerar caminho onde será salvo o arquivo
        Path caminhoArquivo = caminhoDiretorio.resolve(arquivo.getOriginalFilename());

        try {
            //Criar diretório onde será salvo o arquivo
            Files.createDirectories(caminhoDiretorio);
            //salvar arquivo no caminho gerado
            arquivo.transferTo(caminhoArquivo.toFile());
        } catch (IOException ex) {
            throw new RuntimeException("Falha ao salvar o arquivo: " + ex.getMessage());
        }
        //Retornar caminho onde o arquivo foi salvo
        return caminhoArquivo.toFile().toString();
    }
}

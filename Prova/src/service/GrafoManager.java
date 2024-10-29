package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GrafoManager {
    public void processRouteFiles(String configFilePath) {
        FileManager fileManager = new FileManager();

        // Verifica se o arquivo de configuração existe
        if (!Files.exists(Paths.get(configFilePath))) {
            System.out.println("Erro: Arquivo de configuração não encontrado. Criando arquivo...");
            fileManager.createFileRoutes(); 
            return;
        }

        // Verifica se o arquivo está vazio
        if (isConfigFileEmpty(configFilePath)) {
            System.out.println("Erro: Arquivo de configuração encontrado, mas está vazio. Criando rotas...");
            fileManager.createFileRoutes(); 
            return;
        }

        // Verifica se o arquivo contém pelo menos duas linhas
        if (!isConfigFileValid(configFilePath)) {
            System.out.println("Erro: Arquivo de configuração encontrado, mas não contém a linha 2. Criando rotas...");
            fileManager.createFileRoutes(); 
            return;
        }

        System.out.println("Arquivo de configuração encontrado. Processando...");
        fileManager.importConfig(); 
    }

    private boolean isConfigFileEmpty(String configFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFilePath))) {
            String line = br.readLine();
            return line == null || line.trim().isEmpty();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de configuração: " + e.getMessage());
            return true; 
        }
    }

    private boolean isConfigFileValid(String configFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFilePath))) {
            int lineCount = 0;

            // Conta o número de linhas no arquivo
            while (br.readLine() != null) {
                lineCount++;
            }

            // O arquivo é válido se tiver pelo menos 2 linhas
            return lineCount >= 2;
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de configuração: " + e.getMessage());
            return false; // Considera inválido em caso de erro de leitura
        }
    }

    // Método para processar o cabeçalho de um arquivo
    public void processHeader(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();

            if (header == null || !isHeaderValid(header)) {
                System.out.println("Número total de nós inválido. Arquivo deve ser movido para a pasta de Não Processados.");
                // Mover o arquivo para a pasta Não Processados
                br.close();
                new FileManager().moveFileToNaoProcessado(filePath);
                return;
            }

            System.out.println("Cabeçalho válido: " + header);
            // Aqui você pode adicionar a lógica para processar o cabeçalho se ele for válido

        } catch (IOException e) {
            System.out.println("Erro ao processar o cabeçalho: " + e.getMessage());
        }
    }

    private boolean isHeaderValid(String header) {
        // Valida o cabeçalho, por exemplo, verificando se tem o formato correto
        return header.matches("NN=\\d+"); // Exemplo de validação simples
    }
}

package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GrafoManager {
    private FileManager fileManager;

    public GrafoManager() {
        this.fileManager = new FileManager();
    }

    public void processRouteFiles(String configFilePath) {
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
        validateRouteFiles("C:\\Teste"); // Valida os arquivos de rota
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

    public void validateRouteFiles(String directoryPath) {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.startsWith("rota") && name.endsWith(".txt"));
        if (files != null && files.length > 0) {
            for (File file : files) {
            	System.out.println(file);
                boolean isValid = validateRouteFile(file);
                if (isValid) {
                    fileManager.moveToProcessed(file); // Move para Processados se válido
                    System.out.println(file.getName() + " é válido.");
                } else {
                    fileManager.moveToNonProcessed(file); // Move para Não Processados se inválido
                    System.out.println(file.getName() + " é inválido.");
                }
            }
        } else {
            System.out.println("Nenhum arquivo de rota encontrado.");
        }
    }

    private boolean validateRouteFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isValid = true;

            // Lê e valida o cabeçalho
            line = br.readLine();
            if (!isHeaderValid(line)) {
                System.out.println("Cabeçalho inválido: " + line);
                isValid = false;
            }

            // Valida o resumo de conexões
            while ((line = br.readLine()) != null && line.startsWith("01")) {
                if (!isConnectionLineValid(line)) {
                    System.out.println("Linha de conexão inválida: " + line);
                    isValid = false;
                }
            }

            // Valida o resumo de pesos
            while (line != null && line.startsWith("02")) {
                if (!isWeightLineValid(line)) {
                    System.out.println("Linha de peso inválida: " + line);
                    isValid = false;
                }
                line = br.readLine();
            }

            // Valida o trailer
            if (line != null && !line.startsWith("09")) {
                System.out.println("Trailer inválido: " + line);
                isValid = false;
            } else if (line != null) {
                if (!isTrailerValid(line)) {
                    System.out.println("Trailer inválido: " + line);
                    isValid = false;
                }
            }

            return isValid;
        } catch (IOException e) {
            System.out.println("Erro ao validar o arquivo " + file.getName() + ": " + e.getMessage());
            return false;
        }
    }

    private boolean isHeaderValid(String header) {
        return header != null && header.matches("00\\d{2}\\d{5}"); // Exemplo: 00NNSP
    }

    private boolean isConnectionLineValid(String line) {
        return line.matches("01\\d{1,2}=\\d{1,2}"); // Exemplo: 01NO=ND
    }

    private boolean isWeightLineValid(String line) {
        return line.matches("02\\d{1,2};\\d{1,2}=\\d{4}"); // Exemplo: 02NO;ND=P
    }

    private boolean isTrailerValid(String trailer) {
        return trailer.matches("09RC=\\d{2};RP=\\d{2};\\d{4}"); // Exemplo: 09RC=NN;RP=NN;P
    }
}

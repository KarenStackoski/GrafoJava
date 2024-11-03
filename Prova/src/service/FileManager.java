package service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class FileManager {
    private static final String SEPARATOR = "=";
    private static final String FILE_PATH = "C:\\Teste\\Configuracao\\config.txt";

    public void createDirectories() {
        createDirectory("C:\\Teste");
        createDirectory("C:\\Teste\\Configuracao");
        createDirectory("C:\\Teste\\Processado");
        createDirectory("C:\\Teste\\NaoProcessado");
    }

    private void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void createFileRoutes() {
        createDirectories();
        String processado = "Processado" + SEPARATOR + "C:\\Teste\\Processado\n";
        String naoProcessado = "Não Processado" + SEPARATOR + "C:\\Teste\\NaoProcessado";
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(processado);
            bw.write(naoProcessado);
            System.out.println("Arquivo de configuração criado com rota padrão.");
        } catch (IOException e) {
            System.out.println("Erro ao criar rota de arquivo: " + e.getMessage());
        }
    }

    public void importConfig() {
        File configFile = new File(FILE_PATH);
        if (!configFile.exists()) {
            System.out.println("Erro: Arquivo de configuração não encontrado. Criando arquivo...");
            createFileRoutes();
            return;
        }

        ArrayList<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) {
                System.out.println("Arquivo de configuração vazio. Adicionando rotas padrão...");
                createFileRoutes();
                return;
            }

            boolean validConfig = true;
            do {
                linhas.add(line);
                String validationResult = validateLine(line);
                if (!validationResult.equals("Válido")) {
                    System.out.println("Configuração inválida: " + validationResult);
                    validConfig = false;
                }
                line = br.readLine();
            } while (line != null);

            if (validConfig) {
                System.out.println("Configuração válida. Linhas carregadas: " + linhas.size());
            } else {
                System.out.println("Configuração inválida encontrada. Criando rotas padrão...");
                createFileRoutes();
            }
        } catch (IOException e) {
            System.out.println("Erro ao importar arquivo de configuração config.txt: " + e.getMessage());
        }
    }

    private String validateLine(String line) {
        if (line.trim().isEmpty() || !line.contains(SEPARATOR)) {
            return "Linha vazia ou sem separador.";
        }
        String[] parts = line.split(SEPARATOR);
        if (parts.length != 2) {
            return "Número incorreto de partes na linha.";
        }
        String key = parts[0].trim();
        String value = parts[1].trim();
        if (key.equals("Processado") && !value.equals("C:\\Teste\\Processado")) {
            return "Caminho para 'Processado' é inválido.";
        }
        if (key.equals("Não Processado") && !value.equals("C:\\Teste\\NaoProcessado")) {
            return "Caminho para 'Não Processado' é inválido.";
        }
        return "Válido";
    }

    public void processRouteFiles() {
        // Implementar a lógica para processar os arquivos de rota
        File folder = new File("C:\\Teste");
        File[] files = folder.listFiles((dir, name) -> name.matches("rota\\d{2}\\.txt"));
        
        if (files != null) {
            for (File file : files) {
                validateRouteFile(file);
            }
        }
    }

    private void validateRouteFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null || !isValidHeader(headerLine)) {
                System.out.println(file.getName() + " é inválido.");
            } else {
                System.out.println(file.getName() + " é válido.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao processar o arquivo " + file.getName() + ": " + e.getMessage());
        }
    }

    private boolean isValidHeader(String headerLine) {
        return headerLine != null && headerLine.matches("00\\d{2}\\d{5}"); // Exemplo de cabeçalho válido
    }

    // Métodos para mover arquivos
    public void moveToProcessed(File file) {
        moveFile(file, "C:\\Teste\\Processado");
    }

    public void moveToNonProcessed(File file) {
        moveFile(file, "C:\\Teste\\NaoProcessado");
    }

    private void moveFile(File file, String destinationPath) {
        File destDir = new File(destinationPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File newFile = new File(destinationPath + "\\" + file.getName());
        try {
            Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo " + file.getName() + " movido para " + destinationPath);
        } catch (IOException e) {
            System.out.println("Erro ao mover o arquivo " + file.getName() + ": " + e.getMessage());
        }
    }
}

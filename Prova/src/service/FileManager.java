package service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
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
            System.out.println("Arquivo de configuração criado com rotas padrão.");
        } catch (IOException e) {
            System.out.println("Erro ao criar rotas de arquivos: " + e.getMessage());
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
                if (!validateLine(line)) {
                    System.out.println("Configuração inválida: " + line);
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

    private boolean validateLine(String line) {
        if (line.trim().isEmpty() || !line.contains(SEPARATOR)) {
            return false;
        }
        String[] parts = line.split(SEPARATOR);
        if (parts.length != 2) {
            return false;
        }
        String key = parts[0].trim();
        String value = parts[1].trim();
        return (key.equals("Processado") && value.equals("C:\\Teste\\Processado")) ||
               (key.equals("Não Processado") && value.equals("C:\\Teste\\NaoProcessado"));
    }

    public void processHeader(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (!isValidHeader(headerLine)) {
                System.out.println("Número total de nós inválido. Arquivo deve ser movido para a pasta de Não Processados.");
                br.close(); // Fecha o BufferedReader antes de mover o arquivo
                moveFileToNaoProcessado(filePath); // Mova o arquivo para a pasta de não processados
            } else {
                System.out.println("Cabeçalho válido. Processando arquivo...");
            }
        } catch (IOException e) {
            System.out.println("Erro ao processar o cabeçalho: " + e.getMessage());
        }
    }

    private boolean isValidHeader(String headerLine) {
        if (headerLine == null || !headerLine.startsWith("NN=")) {
            return false;
        }
        String[] parts = headerLine.split("=");
        if (parts.length != 2) {
            return false;
        }
        try {
            Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void moveFileToProcessed(String filePath) {
        moveFile(filePath, "C:\\Teste\\Processado");
    }

    public void moveFileToNaoProcessado(String filePath) {
        moveFile(filePath, "C:\\Teste\\NaoProcessado");
    }

    private void moveFile(String filePath, String targetDir) {
        Path sourcePath = Path.of(filePath);
        Path targetPath = Path.of(targetDir + "\\" + new File(filePath).getName());

        for (int attempt = 0; attempt < 10; attempt++) {
            try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
                FileChannel channel = raf.getChannel();
                try (FileLock lock = channel.tryLock()) {
                    if (lock != null) {
                        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Arquivo movido para " + targetDir);
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Tentativa " + (attempt + 1) + " de mover o arquivo falhou: " + e.getMessage());
                try {
                    Thread.sleep(500); // Aguarda 500 ms antes de tentar novamente
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restaura o estado de interrupção
                    System.out.println("Erro ao pausar a thread: " + ie.getMessage());
                }
            }
        }

        // Tenta copiar o arquivo após várias tentativas de mover
        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo copiado para " + targetDir);

            // Tentativas para excluir o arquivo original
            for (int attempt = 0; attempt < 5; attempt++) {
                File originalFile = new File(filePath);
                if (originalFile.delete()) {
                    System.out.println("Arquivo original excluído: " + filePath);
                    return;
                } else {
                    System.out.println("Falha ao excluir o arquivo original: " + filePath + ". Tentativa " + (attempt + 1));
                    try {
                        Thread.sleep(1000); // Aguarda 1 segundo antes da próxima tentativa
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // Restaura o estado de interrupção
                        System.out.println("Erro ao pausar a thread: " + ie.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao copiar o arquivo para " + targetDir + ": " + e.getMessage());
        }

        System.out.println("Erro: não foi possível mover o arquivo para " + targetDir + " após várias tentativas.");
    }

    private boolean isFileInUse(String filePath) {
        File file = new File(filePath);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.writeBytes(""); // Tenta escrever algo no arquivo
            return false; // Se a escrita foi bem-sucedida, o arquivo não está em uso
        } catch (IOException e) {
            return true; // O arquivo está em uso
        }
    }
}

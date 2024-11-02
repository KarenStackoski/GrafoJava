package service;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        GrafoManager grafoManager = new GrafoManager();

        // Criar diretórios e arquivos de configuração se não existirem
        fileManager.createDirectories();

        // Processar o arquivo de configuração e, em seguida, validar os arquivos de rota
        String configFilePath = "C:\\Teste\\Configuracao\\config.txt";
        grafoManager.processRouteFiles(configFilePath);
    }
}

import service.FileManager;
import service.GrafoManager;

public class Main {
    public static void main(String[] args) {
        GrafoManager grafoManager = new GrafoManager();
        FileManager fileManager = new FileManager();
        String configFilePath = "C:\\Teste\\Configuracao\\config.txt"; // Caminho do arquivo de configuração
        
        fileManager.createFileRoutes();
        
        grafoManager.processRouteFiles(configFilePath);
        
        String testFilePath = "C:\\Teste\\Configuracao\\invalid_header.txt"; // Caminho do arquivo inválido
        grafoManager.processHeader(testFilePath); // Chama o método para processar o cabeçalho
    }
}

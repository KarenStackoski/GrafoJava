import service.GrafoManager;

public class Main {
    public static void main(String[] args) {
        GrafoManager grafoManager = new GrafoManager();
        String configFilePath = "C:\\Teste\\Configuracao\\config.txt"; // Caminho do arquivo de configuração
        
        grafoManager.processRouteFiles(configFilePath);
        
        String testFilePath = "C:\\Teste\\Configuracao\\invalid_header.txt"; // Caminho do arquivo inválido
        grafoManager.processHeader(testFilePath); // Chama o método para processar o cabeçalho
    }
}

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CiudadanoMexicanoTest extends Persona{
    private CiudadanoMexicano mexicano;

    @BeforeAll
    @DisplayName("Configura Propiedades Globales Para Pruebas Condicionales")
    static void beforeAll() {
        System.setProperty("Practica 1", "calidad y pruebas");
    }

    @AfterAll
    @DisplayName("Limpia Propiedades Globales Usadas En Pruebas Condicionales")
    static void afterAll() {
        System.clearProperty("Curso");
        System.clearProperty("Entorno");
    }

    @BeforeEach
    @DisplayName("Inicializa Una Cuenta Válida Antes De Cada Test")
    void setUp() {
        mexicano = new CiudadanoMexicano("Raul Hernandez", 32, 1.63, 72.6);
        mexicano = new CiudadanoMexicano("Guerrero","GUE127539GSDT","GUE127539R");
    }

    @AfterEach
    @DisplayName("Manda A Null La Referencia De La Cuenta Después De Cada Prueba")
    void tearDown() {
        mexicano = null;
    }

    /*private static String fileStoreTypeFor(Path path) throws Exception {
        FileStore store = Files.getFileStore(path);
        return store.type() == null ? "" : store.type().toLowerCase();
    }*/

    @Test
    void calcularDiasVividos() {
    }

    @Test
    void clasificacion() {
    }
}
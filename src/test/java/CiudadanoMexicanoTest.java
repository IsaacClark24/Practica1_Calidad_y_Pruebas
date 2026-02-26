import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.condition.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IdentidadMexicana")
@DisplayName("Pruebas Unitarias - Clase CiudadanoMexicano")
class CiudadanoMexicanoTest {

    private CiudadanoMexicano ciudadano;

    @BeforeAll
    static void initAll() {
        System.setProperty("entorno.prueba", "desarrollo");
        System.setProperty("version.app", "1.0");
        System.setProperty("acceso.db", "denegado");
    }

    @BeforeEach
    @DisplayName("Instancia un ciudadano válido antes de cada test")
    void setUp() {
        // Datos que cumplen con los Regex (Nombre con Mayúsculas, CURP y RFC válidos)
        ciudadano = new CiudadanoMexicano("Raul Hernandez", 32, 1.63, 72.6);
        ciudadano.setCurp("HERR980101HDFRNR01");
        ciudadano.setRfc("HERR980101ABC");
    }

    @AfterEach
    void tearDown() {
        ciudadano = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.clearProperty("entorno.prueba");
    }

    //PRUEBAS DE PROPIEDADES DEL SISTEMA
    @Test
    @EnabledIfSystemProperty(named = "entorno.prueba", matches = "desarrollo")
    @DisplayName("Habilitado: Solo en entorno de desarrollo")
    void testSoloEnDesarrollo() {
        assertNotNull(ciudadano.getNombre());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    @DisplayName("Habilitado: Solo si el SO es Windows")
    void testSoloWindows() {
        assertTrue(System.getProperty("os.name").contains("Windows"));
    }

    @Test
    @EnabledOnJre(JRE.JAVA_21) // Ajustar según tu versión de JDK (tu POM dice 25)
    @DisplayName("Habilitado: Solo en versión específica de Java")
    void testVersionJava() {
        assertNotNull(ciudadano);
    }

    @Test
    @DisabledIfSystemProperty(named = "acceso.db", matches = "denegado")
    @DisplayName("Deshabilitado: Si el acceso a DB está denegado")
    void testConexionBaseDatos() {
        fail("Este test no debería ejecutarse si el acceso está denegado");
    }

    @Test
    @DisabledOnOs(OS.LINUX)
    @DisplayName("Deshabilitado: No ejecutar en Linux")
    void testNoLinux() {
        assertFalse(System.getProperty("os.name").contains("Linux"));
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "USER", matches = "root")
    @DisplayName("Deshabilitado: No ejecutar si el usuario es root")
    void testNoRoot() {
        assertNotEquals("root", System.getenv("USER"));
    }

    //PRUEBAS PARAMETRIZADAS
    @ParameterizedTest
    @CsvFileSource(resources = "datos_edades.csv", numLinesToSkip = 1) // Quité el "/"
    @DisplayName("Clasificación desde CSV")
    void testClasificacionCSV(int edad, String esperado) {
        ciudadano.setEdad(edad);
        assertEquals(esperado, ciudadano.clasificacion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"MUCI980101HDFRNR01", "GOGH900505MDFLNS05"})
    @DisplayName("Parametrizado 4: Validación de CURP")
    void testCurpValido(String curp) {
        assertDoesNotThrow(() -> ciudadano.setCurp(curp));
    }

    @ParameterizedTest
    @ValueSource(strings = {"07/12/2000", "01/01/2020"})
    @DisplayName("Parametrizado 5: Días vividos no nulos")
    void testDiasVividos(String fecha) {
        assertTrue(ciudadano.calcularDiasVividos(fecha) >= 0);
    }

    //PRUEBAS REPETIDAS
    @RepeatedTest(2)
    @DisplayName("Repetido 1: Consistencia en el nombre")
    void testNombreConsistencia() {
        assertEquals("Raul Hernandez", ciudadano.getNombre());
    }

    @RepeatedTest(2)
    @DisplayName("Repetido 2: Estabilidad del cálculo de clasificación")
    void testClasificacionEstable() {
        ciudadano.setEdad(70);
        assertEquals("Adulto mayor", ciudadano.clasificacion());
    }

    //PRUEBAS DE TIEMPO
    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    @DisplayName("Timeout 1: Rapidez en cálculo de IMC")
    void testIMCTimeout() {
        ciudadano.calcularIMC();
    }

    @Test
    @DisplayName("Timeout 2: Rapidez en cálculo de días (assertTimeout)")
    void testDiasTimeout() {
        assertTimeout(Duration.ofMillis(500), () -> {
            ciudadano.calcularDiasVividos("01/01/2000");
        });
    }

    //PRUEBAS NORMALES
    @Test
    @DisplayName("Test 15: Error al poner estatura en cero")
    void testEstaturaCero() {
        ciudadano.setEstatura(0);
        assertThrows(ArithmeticException.class, () -> ciudadano.calcularIMC());
    }
}
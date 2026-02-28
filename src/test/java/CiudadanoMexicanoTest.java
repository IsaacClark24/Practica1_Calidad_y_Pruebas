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

    @Nested
    @DisplayName("Contiene Métodos De Pruebas de Tipo Parameterized")
    @Tag("Para")
    class Para{
        //PRUEBAS PARAMETRIZADAS
        @ParameterizedTest
        @CsvFileSource(resources = "datos_edades.csv", numLinesToSkip = 1) // Quité el "/"
        @DisplayName("Clasificación desde CSV")
        void testClasificacionCSV(int edad, String esperado) {
            ciudadano.setEdad(edad);
            assertEquals(esperado, ciudadano.clasificacion());
        }

        @ParameterizedTest
        @ValueSource(strings = {"GARM950315K82GUE", "LOPA801102H35GDL"})
        @DisplayName("Parametrizado 2: Validación de RFC incorrecto")
        void testRfcInvalido(String rfc) {
            assertThrows(IllegalArgumentException.class,() -> ciudadano.setRfc(rfc));
        }

        @ParameterizedTest
        @ValueSource(strings = {"Diana Fernandez Ortiz", "Arturo Guevara Martinez"})
        @DisplayName("Parametrizado 3: Validación de Nombre")
        void testNombreValido(String nombre) {
            assertDoesNotThrow(() -> ciudadano.setNombre(nombre));
        }

        @ParameterizedTest
        @ValueSource(strings = {"MUCI980101HDFRNR01", "GOGH900505MDFLNS05"})
        @DisplayName("Parametrizado 4: Validación de CURP")
        void testCurpValido(String curp) {assertDoesNotThrow(() -> ciudadano.setCurp(curp));
        }

        @ParameterizedTest
        @ValueSource(strings = {"07/12/2000", "01/01/2020"})
        @DisplayName("Parametrizado 5: Días vividos no nulos")
        void testDiasVividos(String fecha) {
            assertTrue(ciudadano.calcularDiasVividos(fecha) >= 0);
        }
    }

    @Nested
    @DisplayName("Contiene Métodos De Pruebas de Tipo Repeated")
    @Tag("Rep")
    class Rep{
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
    }

    @Nested
    @DisplayName("Contiene Métodos De Pruebas de Tipo Timeout")
    @Tag("Time")
    class Time{
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
    }

    @Nested
    @DisplayName("Contiene Métodos De Pruebas Normales")
    @Tag("Normal")
    class Normal{
        //PRUEBAS NORMALES
        @Test
        @DisplayName("Test 15: Error al poner estatura en cero")
        void testEstaturaCero() {
            ciudadano.setEstatura(0);
            assertThrows(ArithmeticException.class, () -> ciudadano.calcularIMC());
        }

        @Test
        @DisplayName("Test 14: Error al poner una fecha futura")
        void testFechaFutura() {
            assertThrows(IllegalArgumentException.class, () -> ciudadano.calcularDiasVividos("23/03/2028"));
        }

        @Test
        @DisplayName("Test 13: El nombre debe iniciar en mayuscula")
        void testNombreMinuscula() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ciudadano.setNombre("ana hernandez"));
            assertEquals("Nombre inválido. Debe emmpezar con Mayúscula.", exception.getMessage());
        }

        @Test
        @DisplayName("Test 12: El nombre no debe ser nulo")
        void testNombreNulo() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ciudadano.setNombre(null));
            assertEquals("Debe proporcionar un nombre.", exception.getMessage());
        }

        @Test
        @DisplayName("Test 11: La edad no puede ser negativa")
        void testEdadNegativa() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ciudadano.setEdad(-12));
            assertEquals("La Edad No Puede Ser Negativa.", exception.getMessage());
        }

        @Test
        @DisplayName("Test 10: El peso no puede ser 0")
        void testPesoCero() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ciudadano.setPeso(0));
            assertEquals("El Peso No Puede Ser Negativo Ni Menor a Cero.", exception.getMessage());
        }

        @Test
        @DisplayName("Test 9: El peso no puede ser negativo")
        void testPesoNegativo() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ciudadano.setPeso(-60));
            assertEquals("El Peso No Puede Ser Negativo Ni Menor a Cero.", exception.getMessage());
        }

        @Test
        @DisplayName("Test 8: La estatura no puede ser negativa")
        void testEstaturaNegativa() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ciudadano.setEstatura(-1.72));
            assertEquals("La Estatura No Puede Ser Menor a Cero.", exception.getMessage());
        }

        @Test
        @DisplayName("Test 7: Menores de edad no pueden tener RFC")
        void testRFCNulo() {
            CiudadanoMexicano ciudadanoMenor = new CiudadanoMexicano("Ana Flores", 13, 1.35, 56.8);
            ciudadanoMenor.setRfc("AFLO980101GDL");
            assertEquals(null, ciudadanoMenor.getRfc());
        }

        @Test
        @DisplayName("Test 6: Prueba de clasificación Adulto Mayor")
        void testClasificacion() {
            CiudadanoMexicano ciudadanoMayor = new CiudadanoMexicano("Martin Gonzales", 76, 1.73, 84.5);
            assertEquals("Adulto mayor", ciudadanoMayor.clasificacion());
        }

        @Test
        @DisplayName("Test 5: Prueba de calcular días vividos")
        void testDiasVividos() {
            assertEquals(4, ciudadano.calcularDiasVividos("23/02/2026"));
        }

        @Test
        @DisplayName("Test 4: Calcular IMC")
        void testIMC() {
            assertEquals(27.3250, ciudadano.calcularIMC(), 0.001);
        }

        @Test
        @DisplayName("Test 3: Prueba de clasificación Adulto")
        void testClasificacionAdulto() {
            assertEquals("Adulto", ciudadano.clasificacion());
        }

        @Test
        @DisplayName("Test 2: Prueba de clasificación Adulto Mayor")
        void testClasificacionMenor() {
            CiudadanoMexicano ciudadanoMenor = new CiudadanoMexicano("Adrian Gonzales", 7, 1.20, 44.9);
            assertEquals("Menor de edad", ciudadanoMenor.clasificacion());
        }

        @Test
        @DisplayName("Test 1: Cambiar RFC con valor valido")
        void testRfcValido() {
            CiudadanoMexicano prueba=new CiudadanoMexicano();
            assertDoesNotThrow(() -> prueba.setRfc("SAVA920820DL1"));
        }
    }
}
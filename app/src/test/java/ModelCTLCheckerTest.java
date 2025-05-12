import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import controller.CTLFormula;
import model.KripkeStructure;
import model.State;

public class ModelCTLCheckerTest {

    private KripkeStructure kripke;

    private void loadKripkeModel(String modelPath) throws Exception {
        System.out.println("Loading Kripke model from: " + modelPath);
        String rawText = new String(Files.readAllBytes(Paths.get(modelPath)));
        String kripkeDefinition = utils.Util.cleanText(rawText);
        kripke = new KripkeStructure(kripkeDefinition);

        // Validate Kripke structure
        assertNotNull(kripke, "The Kripke model should be loaded.");
        assertFalse(kripke.states.isEmpty(), "The Kripke model should contain states.");
        assertFalse(kripke.transitions.isEmpty(), "The Kripke model should contain transitions.");
        System.out.println("Kripke model loaded and validated successfully.");
    }

    private void testModelConditions(String modelPath, String formulasPath) throws Exception {
        // Load the Kripke model
        loadKripkeModel(modelPath);

        System.out.println("Testing conditions from: " + formulasPath);

        // Read the test formulas and remove BOM if it exists
        List<String> testLines = Files.readAllLines(Paths.get(formulasPath))
                .stream()
                .map(line -> line.startsWith("\uFEFF") ? line.substring(1) : line) // Strip BOM
                .toList();

        // Iterate through each test line
        for (String line : testLines) {
            System.out.println("\nProcessing line: " + line); // Log the test case

            String[] parts = line.split(";");
            if (parts.length != 3) {
                System.out.println("Malformed line: " + line); // Handle malformed lines
                continue;
            }

            String stateName = parts[0].trim();
            String condition = parts[1].trim();
            boolean expected = Boolean.parseBoolean(parts[2].trim());

            System.out.println("Testing state: " + stateName + ", Condition: " + condition);

            State testState = new State(stateName, kripke);
            CTLFormula ctlFormula = new CTLFormula(condition, testState, kripke);

            try {
                boolean result = ctlFormula.IsSatisfy();
                System.out.println("Result: " + result + ", Expected: " + expected);
                assertEquals(expected, result, "The formula '" + condition + "' should " +
                        (expected ? "" : "not ") + "hold in state " + stateName + ".");

                System.out.println("\nCompleted Successfully\n");
            } catch (Exception e) {
                System.out.println("Error processing condition: " + condition + " for state: " + stateName);
                e.printStackTrace();
                throw e; // Re-throw to fail the test
            }
        }
    }

    @Test
    void testModel1() throws Exception {
        testModelConditions(
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 1.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 1 - Test Formulas.txt"
        );
    }

    @Test
    void testModel2() throws Exception {
        testModelConditions(
        
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 2.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 2 - Test Formulas.txt"
        );
    }

    @Test
    void testModel3() throws Exception {
        testModelConditions(
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 3.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 3 - Test Formulas.txt"
        );
    }

    @Test
    void testModel4() throws Exception {
        testModelConditions(
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 4.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 4 - Test Formulas.txt"
        );
    }

    @Test
    void testModel5() throws Exception {
        testModelConditions(
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 5.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 5 - Test Formulas.txt"
        );
    }

    @Test
    void testModel6() throws Exception {
        testModelConditions(
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 6.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 6 - Test Formulas.txt"
        );
    }

    @Test
    void testModel7() throws Exception {
        testModelConditions(
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 7.txt",
                "/Users/kamalkiranpolisetty/Desktop/modelCTLChecker/app/src/test/resources/Test Files/Model 7 - Test Formulas.txt"
        );
    }
}

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *  Test class for testing JIMachine class non-GUI portion.
 */
public class JIMachineTest {

  /** The original proportion of display. */
  private final double originalproportion = 1.0;
  /** Increment of proportion of each steps. */
  private final double stepProportion = 0.25;

  /**
   * A test for getProportion function.
   */
  @Test
  public void testGetProportion() {
    JIMachine jiMachine = new JIMachine();
    double actual = jiMachine.getProportion();
    double expected = originalproportion;
    assertEquals(expected, actual);
  }

  /**
   * A test for increaseProportion function.
   */
  @Test
  public void testIncreaseProportion() {
    JIMachine jiMachine = new JIMachine();
    jiMachine.increaseProportion();
    double actual = jiMachine.getProportion();
    double expected = (originalproportion + stepProportion);
    assertEquals(expected, actual);
    jiMachine.increaseProportion();
    actual = jiMachine.getProportion();
    expected = (originalproportion + stepProportion)
      * (originalproportion + stepProportion);
    assertEquals(expected, actual);
  }

  /**
   * A test for decreaseProportion function.
   */
  @Test
  public void testDecreaseProportion() {
    JIMachine jiMachine = new JIMachine();
    jiMachine.decreaseProportion();
    double actual = jiMachine.getProportion();
    double expected = (originalproportion - stepProportion);
    assertEquals(expected, actual);
    jiMachine.decreaseProportion();
    actual = jiMachine.getProportion();
    expected = (originalproportion - stepProportion)
      * (originalproportion - stepProportion);
    assertEquals(expected, actual);
  }

  /**
   * A test for getProportion function.
   */
  @Test
  public void testSetOriginalProportion() {
    JIMachine jiMachine = new JIMachine();
    jiMachine.decreaseProportion();
    jiMachine.decreaseProportion();
    jiMachine.decreaseProportion();
    jiMachine.setOriginalProportion();
    double actual = jiMachine.getProportion();
    double expected = originalproportion;
    assertEquals(expected, actual);
  }
}

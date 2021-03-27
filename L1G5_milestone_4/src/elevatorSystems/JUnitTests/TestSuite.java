
package elevatorSystems.JUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)

@Suite.SuiteClasses(value={
   RequestGroupTest.class,
   RequestTest.class,
   FloorSubsystemTest.class,
   ElevatorTest.class
})
/**
 * @author Matthew Harris 101073502 & Jay McCracken 101066860
 *
 */
public class TestSuite {
}

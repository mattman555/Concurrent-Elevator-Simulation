/**
 * 
 */
package elevatorSystems;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)

@Suite.SuiteClasses(value={
   RequestGroupTest.class,
   SchedulerTest.class,
   FloorSubsystemTest.class,
   ElevatorTest.class
})
/**
 * @author Matthew Harris 101073502
 *
 */
public class TestSuite {
}

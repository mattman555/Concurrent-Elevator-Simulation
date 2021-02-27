
package elevatorSystems.JUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)

@Suite.SuiteClasses(value={
   RequestGroupTest.class,
   RequestTest.class,
   SchedulerTest.class,
   FloorSubsystemTest.class,
   ElevatorTest.class,
   elevatorStates.JUnitTests.ArrivedTest.class,
   elevatorStates.JUnitTests.DoorsClosedTest.class,
   elevatorStates.JUnitTests.DoorsOpenTest.class,
   elevatorStates.JUnitTests.ElevatorSMTest.class,
   elevatorStates.JUnitTests.MovingTest.class,
   elevatorStates.JUnitTests.UpdateLampsTest.class,
   
})
/**
 * @author Matthew Harris 101073502 & Jay McCracken 101066860
 *
 */
public class TestSuite {
}

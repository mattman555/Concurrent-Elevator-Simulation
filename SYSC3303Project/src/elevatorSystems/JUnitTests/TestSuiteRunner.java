/**
 * 
 */
package elevatorSystems.JUnitTests;

/**
 * @author Matthew Harris 101073502
 *
 */
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestSuiteRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(TestSuite.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println("How many tests were ran: " + result.getRunCount());
      System.out.println("How many tests failed: " + result.getFailureCount());
      System.out.println("How many tests were sucessful: " + (result.getRunCount()-result.getFailureCount()-result.getIgnoreCount()));
      System.out.println("How many tests were ignored: " + (result.getIgnoreCount()));
   }
}  	


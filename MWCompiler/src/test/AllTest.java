/**
 * <h1>AllTest</h1>
 * The suite of tests which runs all of the test
 * 
 * 
 * @author Michael Wu
 * @version 1.0
 * @since 2018-03-30
 */
package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ testTest.class
        /*Add test class name here, e.g. testA.class*/
})

// Empty class for the test suite
public class AllTest {

}

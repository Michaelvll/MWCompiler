package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * AllTest.java
 * The suite of tests which runs all of the test
 * 
 * 
 * @author Michael Wu
 * @since 2018-03-30
 */
@RunWith(Suite.class)
@SuiteClasses({  TestPrint.class, TestParser.class
        /*Add test class name here, e.g. testA.class*/
})

// Empty class for the test suite
public class AllTest {

}

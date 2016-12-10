package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import model.ClumpOfOysters;
import model.ConcreteWalls;


public class ConcreteWallsTest {
	
	@Test(expected=UnsupportedOperationException.class)
	public void healthTest(){
		ConcreteWalls tester = new ConcreteWalls(0, 0);
		tester.changeHealth(1);		
	}
	
	@Test
	public void testXandY() {
		ConcreteWalls tester = new ConcreteWalls(0,0);
		assertTrue(tester.getX() == 0);
		assertTrue(tester.getY() == 0);
		
	}
}

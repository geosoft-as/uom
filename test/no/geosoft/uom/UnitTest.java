package no.geosoft.uom;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import no.geosoft.cc.util.Random;

/**
 * Test the Unit class.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class UnitTest
{
  /**
   * Create a random unit.
   *
   * @return A random unit. Never null.
   */
  private Unit newRandomUnit()
  {
    return new Unit(Random.getString(),
                    Random.getString(),
                    Random.getDouble(),
                    Random.getDouble(),
                    Random.getDouble(),
                    Random.getDouble());
  }

  /**
   * Test the Unit constructor.
   */
  @Test
  public void testConstructor()
  {
    //
    // Verify that construction is always successful
    //
    Unit unit = newRandomUnit();
    Assertions.assertNotNull(unit);

    //
    // Illegal arguments
    //
    try {
      new Unit(null,
               Random.getString(),
               Random.getDouble(),
               Random.getDouble(),
               Random.getDouble(),
               Random.getDouble());
      Assertions.fail("Illegal arguments");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      new Unit(Random.getString(),
               null,
               Random.getDouble(),
               Random.getDouble(),
               Random.getDouble(),
               Random.getDouble());
      Assertions.fail("Illegal arguments");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the Unit.getName() method.
   */
  @Test
  public void testGetName()
  {
    for (int i = 0; i < 100; i++) {
      String name = Random.getString();

      Unit unit = new Unit(name,
                           Random.getString(),
                           Random.getDouble(),
                           Random.getDouble(),
                           Random.getDouble(),
                           Random.getDouble());

      Assertions.assertEquals(name, unit.getName());
    }
  }

  /**
   * Test the Unit.getSymbol() method.
   */
  @Test
  public void testGetSymbol()
  {
    for (int i = 0; i < 100; i++) {
      String symbol = Random.getString();

      Unit unit = new Unit(Random.getString(),
                           symbol,
                           Random.getDouble(),
                           Random.getDouble(),
                           Random.getDouble(),
                           Random.getDouble());

      Assertions.assertEquals(symbol, unit.getSymbol());
    }
  }

  /**
   * Test the Unit.toBase() method.
   */
  @Test
  public void testToBase()
  {
    // According to documentation:
    //
    //   base = (a * value + b) / (c * value + d);
    //
    for (int i = 0; i < 1000; i++) {
      double a = Random.getDouble();
      double b = Random.getDouble();
      double c = Random.getDouble();
      double d = Random.getDouble();

      Unit unit = new Unit(Random.getString(), Random.getString(),
                           a, b, c, d);

      double value = Random.getDouble();

      double expected = (a * value + b) / (c * value + d);
      double actual = unit.toBase(value);

      Assertions.assertEquals(expected, actual);
    }
  }

  /**
   * Test the Unit.fromBase() method.
   */
  @Test
  public void testFromBase()
  {
    //
    // TODO: Thnk the test is OK. Need to look at the implementation.
    //
    for (int i = 0; i < 1000; i++) {
      Unit unit = newRandomUnit();
      double value = Random.getDouble(-100000, +100000);

      double base = unit.toBase(value);
      double value1 = unit.fromBase(base);

      Assertions.assertEquals(value, value1, Math.abs(value / 1000.0));
    }
  }

  /**
   * Test the Unit.equals() method.
   */
  @Test
  public void testEquals()
  {
    for (int i = 0; i < 1000; i++) {
      String name1 = Random.getString();
      String symbol1 = Random.getString();
      double a1 = Random.getDouble();
      double b1 = Random.getDouble();
      double c1 = Random.getDouble();
      double d1 = Random.getDouble();

      Unit unit1 = new Unit(name1, symbol1, a1, b1, c1, d1);
      Unit unit2 = new Unit(name1, symbol1, a1, b1, c1, d1);

      Assertions.assertTrue(unit1.equals(unit1));
      Assertions.assertTrue(unit1.equals(unit2));
      Assertions.assertTrue(unit2.equals(unit2));
      Assertions.assertTrue(unit2.equals(unit1));

      String name2 = Random.getString();
      String symbol2 = Random.getString();
      double a2 = Random.getDouble();
      double b2 = Random.getDouble();
      double c2 = Random.getDouble();
      double d2 = Random.getDouble();

      Unit unit3 = new Unit(name2, symbol2, a2, b2, c2, d2);

      Assertions.assertFalse(unit1.equals(unit3));
      Assertions.assertFalse(unit3.equals(unit1));
      Assertions.assertFalse(unit2.equals(unit3));
      Assertions.assertFalse(unit3.equals(unit2));
    }
  }

  /**
   * Test the Unit.toString() method.
   */
  @Test
  public void testToString()
  {
    for (int i = 0; i < 100; i++) {
      Unit unit = newRandomUnit();
      String s = unit.toString();
      Assertions.assertNotNull(s);
      Assertions.assertTrue(s.length() > 0);
    }
  }
}

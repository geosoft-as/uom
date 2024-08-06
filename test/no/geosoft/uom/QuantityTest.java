package no.geosoft.uom;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import no.geosoft.cc.util.Random;

/**
 * Test the Quantity class.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class QuantityTest
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
   * Test the Quantity constructor.
   */
  @Test
  public void testConstructor()
  {
    for (int i = 0; i < 100; i++) {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      Assertions.assertNotNull(quantity);
    }

    try {
      new Quantity(null, Random.getStringOrNull());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the Quantity.getName() method.
   */
  @Test
  public void testGetName()
  {
    for (int i = 0; i < 100; i++) {
      String name = Random.getString();
      Quantity quantity = new Quantity(name, Random.getStringOrNull());

      Assertions.assertEquals(name, quantity.getName());
    }
  }

  /**
   * Test the Quantity.getDescription() method.
   */
  @Test
  public void testGetDescription()
  {
    for (int i = 0; i < 100; i++) {
      String description = Random.getStringOrNull();
      Quantity quantity = new Quantity(Random.getString(), description);

      Assertions.assertEquals(description, quantity.getDescription());
    }
  }

  /**
   * Test the Quantity.getUnits() method.
   */
  @Test
  public void testGetUnits()
  {
    //
    // Verify that a newly created quantity has no units
    //
    for (int i = 0; i < 100; i++) {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      Assertions.assertEquals(0, quantity.getUnits().size());
    }

    //
    // Verify that getUnits() contains exaclty the units added
    //
    for (int i = 0; i < 100; i++) {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());

      int nUnits = Random.getInteger(1, 100);
      List<Unit> units = new ArrayList<>();

      for (int j = 0; j < nUnits; j++) {
        Unit unit = newRandomUnit();
        units.add(unit);

        quantity.addUnit(unit, false);

        Assertions.assertEquals(units.size(), quantity.getUnits().size());

        for (Unit u : quantity.getUnits())
          Assertions.assertTrue(units.contains(u));
      }
    }
  }

  /**
   * Test the Quantity.addUnit() method.
   */
  @Test
  public void testGasBaseUnit()
  {
    //
    // Verify that base unit is unull if there are no units
    //
    {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      Assertions.assertNull(quantity.getBaseUnit());
    }

    //
    // Case 2: Verify that the first unit added act as a base unit
    // if none are added explicitly as base unit.
    //
    {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());

      Unit firstUnit = newRandomUnit();
      quantity.addUnit(firstUnit, false);
      Assertions.assertEquals(firstUnit, quantity.getBaseUnit());

      int nUnits = Random.getInteger(0, 100);
      for (int i = 0; i < nUnits; i++) {
        quantity.addUnit(newRandomUnit(), false);
        Assertions.assertEquals(firstUnit, quantity.getBaseUnit());
      }
    }

    //
    // Case 3: Verify that the specified one act as base unit
    //
    {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());

      // First add a number of non-base units
      int nUnits = Random.getInteger(0, 100);
      for (int i = 0; i < nUnits; i++)
        quantity.addUnit(newRandomUnit(), false);

      // Then add a base unit
      Unit baseUnit = newRandomUnit();
      quantity.addUnit(baseUnit, true);
      Assertions.assertEquals(baseUnit, quantity.getBaseUnit());

      // Add more non-base units
      nUnits = Random.getInteger(0, 100);
      for (int i = 0; i < nUnits; i++) {
        quantity.addUnit(newRandomUnit(), false);
        Assertions.assertEquals(baseUnit, quantity.getBaseUnit());
      }
    }

    //
    // Case 4: More than one unit is added as base unit
    //
    {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      for (int i = 0; i < 1000; i++) {
        boolean isBaseUnit = Random.getBoolean();
        Unit unit = newRandomUnit();

        quantity.addUnit(unit, isBaseUnit);

        if (isBaseUnit)
          Assertions.assertEquals(unit, quantity.getBaseUnit());
      }
    }
  }

  /**
   * Test the Quantity.addUnit() method.
   */
  @Test
  public void testAddUnit()
  {
    //
    // Verify that units added are indeed part of the quantity
    //
    {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      for (int i = 0; i < 100; i++) {
        Unit unit = newRandomUnit();
        quantity.addUnit(unit, Random.getBoolean());

        Assertions.assertTrue(quantity.getUnits().contains(unit));
      }
    }

    //
    // Illegal argument
    //
    try {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      quantity.addUnit(null, Random.getBoolean());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the Quantity.toString() method.
   */
  @Test
  public void testToString()
  {
    for (int i = 0; i < 100; i++) {
      Quantity quantity = new Quantity(Random.getString(), Random.getStringOrNull());
      String s = quantity.toString();
      Assertions.assertNotNull(s);
      Assertions.assertTrue(s.length() > 0);
    }
  }
}

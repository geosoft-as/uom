package no.geosoft.uom;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import no.geosoft.cc.util.Random;

public final class UnitManagerTest
{
  /**
   * Test the UnitManager.getInstance() method.
   */
  @Test
  public void testGetInstance()
  {
    UnitManager unitManager = UnitManager.getInstance();
    Assertions.assertNotNull(unitManager);
    Assertions.assertSame(unitManager, UnitManager.getInstance());
  }

  /**
   * Test the UnitManager.addUnitAlias() method.
   */
  @Test
  public void testAddUnitAlias()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that we can find a unit that has a random alias
    // associated with it.
    //
    for (int i = 0; i < 10000; i++) {
      Quantity quantity = Random.getElement(unitManager.getQuantities());
      Unit unit = Random.getElement(quantity.getUnits());

      String unitSymbolAlias = Random.getAsciiString().trim();
      if (unitSymbolAlias.isEmpty())
        continue;

      unitManager.addUnitAlias(unitSymbolAlias, unit.getSymbol());

      Unit unit2 = unitManager.findUnit(unitSymbolAlias);

      Assertions.assertEquals(unit, unit2, "Not found");
    }

    //
    // Verify that we cannot find a unit based on an alias if it is
    // associated with a non-existing symbol
    // TODO: Think there is something wrong with this test!
    //
    for (int i = 0; i < 1000; i++) {
      String unitSymbolAlias = Random.getAsciiString().trim();
      if (unitSymbolAlias.isEmpty())
        continue;

      String unitSymbol = Random.getAsciiString().trim();
      if (unitManager.findUnit(unitSymbol) != null)
        continue;

      unitManager.getInstance().addUnitAlias(unitSymbolAlias, unitSymbol);
      Assertions.assertNull(unitManager.findUnit(unitSymbolAlias), "Not null");
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.addUnitAlias(null, Random.getString());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.addUnitAlias(Random.getString(), null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.setDisplaySymbol(String,String) method.
   */
  @Test
  public void testSetDisplaySymbol1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that display symbols set are the same that later are returned
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {

        String displaySymbol = Random.getAsciiString().trim();
        unitManager.setDisplaySymbol(unit.getSymbol(), displaySymbol);

        Assertions.assertEquals(displaySymbol, unitManager.getDisplaySymbol(unit));
      }
    }

    //
    // Illegal arguments
    //
    try {
      unitManager.setDisplaySymbol((String) null, Random.getString());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.setDisplaySymbol(Random.getString(), null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.setDisplaySymbol(Unit,String) method.
   */
  @Test
  public void testSetDisplaySymbol2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that display symbols set are the same that later are returned
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        String displaySymbol = Random.getAsciiString().trim();
        unitManager.setDisplaySymbol(unit, displaySymbol);

        Assertions.assertEquals(displaySymbol, unitManager.getDisplaySymbol(unit));
      }
    }

    //
    // Illegal arguments
    //
    try {
      unitManager.setDisplaySymbol((Unit) null, Random.getString());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    Quantity quantity = Random.getElement(unitManager.getQuantities());
    Unit unit = Random.getElement(quantity.getUnits());
    try {
      unitManager.setDisplaySymbol(unit, null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.getQuantities() method.
   */
  @Test
  public void testGetQuantities()
  {
    //
    // Verify that internal quantities are established
    //
    List<Quantity> quantities = UnitManager.getInstance().getQuantities();
    Assertions.assertNotNull(quantities);
    Assertions.assertFalse(quantities.isEmpty());
  }

  /**
   * Test the UnitManager.getQuantities() method.
   */
  @Test
  public void testAddQuantity()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that quantities added becomes part of the manager
    //
    for (int i = 0; i < 100; i++) {
      String quantityName = Random.getString();

      if (unitManager.findQuantity(quantityName) != null)
        continue;

      Quantity quantity = new Quantity(quantityName, Random.getStringOrNull());
      unitManager.addQuantity(quantity);

      Assertions.assertTrue(unitManager.getQuantities().contains(quantity));
    }

    //
    // Illegal argument 1: Existing quantity
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      try {
        unitManager.addQuantity(quantity);
        Assertions.fail("Illegal argument");
      }
      catch (IllegalArgumentException exception) {
        // Fine
      }
    }

    //
    // Illegal argument 2: Null
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      try {
        unitManager.addQuantity(null);
        Assertions.fail("Illegal argument");
      }
      catch (IllegalArgumentException exception) {
        // Fine
      }
    }
  }

  /**
   * Test the UnitManager.findQuantity(String) method.
   */
  @Test
  public void testFindQuantity1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that all existing quantities can be found
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      String quantityName = quantity.getName();
      Assertions.assertNotNull(unitManager.findQuantity(quantityName));
    }

    //
    // Verify that client added quantities can be found
    //
    for (int i = 0; i < 100; i++) {
      String quantityName = Random.getString();
      if (unitManager.findQuantity(quantityName) != null)
        continue;

      Quantity quantity = new Quantity(quantityName, Random.getStringOrNull());
      unitManager.addQuantity(quantity);
      Assertions.assertNotNull(unitManager.findQuantity(quantity.getName()));
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.findQuantity((String) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.findUnit() method.
   */
  @Test
  public void testFindUnit()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that all existing units can be found
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        String unitSymbol = unit.getSymbol();
        Assertions.assertNotNull(unitManager.findUnit(unitSymbol));
      }
    }

    // TODO: Add quantities/Units and find this

  }

  /**
   * Test the UnitManager.findConvertibleUnits(Unit) method.
   */
  @Test
  public void testFindConvertibleUnits1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that for any two units convertible there exists
    // at least one quantity containing them both
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit1 : quantity.getUnits()) {
        for (Unit unit2 : unitManager.findConvertibleUnits(unit1)) {

          boolean foundQuantity = false;
          for (Quantity q : unitManager.getQuantities()) {
            if (q.getUnits().contains(unit1) && q.getUnits().contains(unit2)) {
              foundQuantity = true;
              break;
            }
          }

          Assertions.assertTrue(foundQuantity);
        }
      }
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.findConvertibleUnits((Unit) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.findConvertibleUnits(String) method.
   */
  @Test
  public void testFindConvertibleUnits2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that for any two units convertible there exists
    // at least one quantity containing them both
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit1 : quantity.getUnits()) {
        for (Unit unit2 : unitManager.findConvertibleUnits(unit1.getSymbol())) {

          boolean foundQuantity = false;
          for (Quantity q : unitManager.getQuantities()) {
            if (q.getUnits().contains(unit1) && q.getUnits().contains(unit2)) {
              foundQuantity = true;
              break;
            }
          }

          Assertions.assertTrue(foundQuantity);
        }
      }
    }

    //
    // Invalid arguments
    //
    /*
    try {
      unitManager.findConvertibleUnits((String) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
    */
  }

  /**
   * Test the UnitManager.findQuantities(Unit) method.
   */
  @Test
  public void testFindQuantities1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Check that all quantities that are associated with a unit
    // indeed contains this unit
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        List<Quantity> quantities = unitManager.findQuantities(unit);
        for (Quantity q : quantities)
          Assertions.assertTrue(q.getUnits().contains(unit), unit + " not contained in " + q.getName());
      }
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.findQuantities((Unit) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.findQuantities(String) method.
   */
  @Test
  public void testFindQuantities2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Check that all quantities that are associated with a unit
    // indeed contains this unit
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        List<Quantity> quantities = unitManager.findQuantities(unit.getSymbol());
        for (Quantity q : quantities)
          Assertions.assertTrue(q.getUnits().contains(unit), unit + " not contained in " + q.getName());
      }
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.findQuantities((String) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.findQuantity(Unit) method.
   */
  @Test
  public void testFindQuantity2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Check that all quantities that are associated with a unit
    // indeed contains this unit
    //
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        Quantity q = unitManager.findQuantity(unit);
        Assertions.assertTrue(q.getUnits().contains(unit));
      }
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.findQuantity((Unit) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.canConvert(Unit,Unit) method.
   */
  @Test
  public void testCanConvert1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // It is possible to convert between two units if there exist at
    // least one quantity where both are memebers
    //

    // First make a list of all units
    List<Unit> allUnits = new ArrayList<>();
    for (Quantity quantity : unitManager.getQuantities())
      allUnits.addAll(quantity.getUnits());

    // Pick any two
    for (Unit unit1 : allUnits) {
      for (Unit unit2 : allUnits) {
        boolean canConvert = unitManager.canConvert(unit1, unit2);

        boolean couldConvert = false;
        for (Quantity quantity : unitManager.getQuantities()) {
          if (quantity.getUnits().contains(unit1) &&
              quantity.getUnits().contains(unit2))
            couldConvert = true;
        }

        Assertions.assertEquals(couldConvert, canConvert, unit1 + " -> " + unit2);
      }
    }

    //
    // Invalid arguments
    //
    Quantity quantity = Random.getElement(unitManager.getQuantities());
    Unit unit = Random.getElement(quantity.getUnits());

    try {
      unitManager.canConvert(unit, null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.canConvert(null, unit);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.canConvert((Unit) null, (Unit) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.canConvert(String,String) method.
   */
  @Test
  public void testCanConvert2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // It is possible to convert between two units if there exist at
    // least one quantity where both are memebers
    //

    // First make a list of all units
    List<Unit> allUnits = new ArrayList<>();
    for (Quantity quantity : unitManager.getQuantities())
      allUnits.addAll(quantity.getUnits());

    // Pick any two
    for (Unit unit1 : allUnits) {
      for (Unit unit2 : allUnits) {
        boolean canConvert = unitManager.canConvert(unit1.getSymbol(),
                                                    unit2.getSymbol());

        boolean couldConvert = false;
        for (Quantity quantity : unitManager.getQuantities()) {
          if (quantity.getUnits().contains(unit1) &&
              quantity.getUnits().contains(unit2))
            couldConvert = true;
        }

        Assertions.assertEquals(couldConvert, canConvert, unit1 + " -> " + unit2);
      }
    }

    //
    // Invalid arguments
    //
    String unitSymbol = Random.getString();

    try {
      unitManager.canConvert(unitSymbol, null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.canConvert(null, unitSymbol);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.canConvert((String) null, (String) null);
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.convert(Unit,Unit,double) method.
   */
  @Test
  public void testConvert1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // We can't really verify that the conversions as such are correct,
    // so we instead check that conversion back and forth will give
    // the back the origibal value no matter whicg units we use.
    //

    // First make a list of all units
    List<Unit> allUnits = new ArrayList<>();
    for (Quantity quantity : unitManager.getQuantities())
      allUnits.addAll(quantity.getUnits());

    for (Unit unit1 : allUnits) {
      for (Unit unit2 : allUnits) {
        double value1 = Random.getDouble();
        double value2 = unitManager.convert(unit1, unit2, value1);
        double value3 = unitManager.convert(unit2, unit1, value2);

        Assertions.assertEquals(value1, value3, Math.abs(value1 / 10000.0), unit1 + " -> " + unit2 + " -> " + unit1);
      }
    }

    //
    // Invalid arguments
    //
    Quantity quantity = Random.getElement(unitManager.getQuantities());
    Unit unit = Random.getElement(quantity.getUnits());

    try {
      unitManager.convert(null, unit, Random.getDouble());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.convert(unit, null, Random.getDouble());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.convert(String,String,double) method.
   */
  @Test
  public void testConvert2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // We can't really verify that the conversions as such are correct,
    // so we instead check that conversion back and forth will give
    // the back the origibal value no matter whicg units we use.
    //

    // First make a list of all units
    List<Unit> allUnits = new ArrayList<>();
    for (Quantity quantity : unitManager.getQuantities())
      allUnits.addAll(quantity.getUnits());

    for (Unit unit1 : allUnits) {
      for (Unit unit2 : allUnits) {
        double value1 = Random.getDouble();
        double value2 = unitManager.convert(unit1.getSymbol(), unit2.getSymbol(), value1);
        double value3 = unitManager.convert(unit2.getSymbol(), unit1.getSymbol(), value2);

        Assertions.assertEquals(value1, value3, Math.abs(value1 / 10000.0), unit1 + " -> " + unit2 + " -> " + unit1);
      }
    }

    //
    // Verify that "conversion" between unknown units just returns the
    // save value
    //
    for (int i = 0; i < 1000; i++) {
      String unitSymbol1 = Random.getString();
      String unitSymbol2 = Random.getString();

      if (unitManager.findUnit(unitSymbol1) != null ||
          unitManager.findUnit(unitSymbol2) != null)
        continue;

      double value1 = Random.getDouble();
      double value2 = unitManager.convert(unitSymbol1, unitSymbol2, value1);
      double value3 = unitManager.convert(unitSymbol1, unitSymbol2, value2);

      Assertions.assertEquals(value1, value2);
      Assertions.assertEquals(value2, value3);
    }

    //
    // Invalid arguments
    //
    try {
      unitManager.convert(null, Random.getString(), Random.getDouble());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }

    try {
      unitManager.convert(Random.getString(), null, Random.getDouble());
      Assertions.fail("Illegal argument");
    }
    catch (IllegalArgumentException exception) {
      // Fine
    }
  }

  /**
   * Test the UnitManager.getDisplaySymbol(Unit) method.
   */
  @Test
  public void testGetDisplaySymbol1()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Not much we can test for display symbols for the internal
    // units other than they being non-null.
    //
    List<String> displaySymbols = new ArrayList<>();
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        String displaySymbol = unitManager.getDisplaySymbol(unit);
        Assertions.assertNotNull(displaySymbol);
      }
    }
  }

  /**
   * Test the UnitManager.getDisplaySymbol(String) method.
   */
  @Test
  public void testGetDisplaySymbol2()
  {
    UnitManager unitManager = UnitManager.getInstance();

    //
    // Verify that the method gives the same result as the
    // equivalent method with unit argument.
    //
    List<String> displaySymbols = new ArrayList<>();
    for (Quantity quantity : unitManager.getQuantities()) {
      for (Unit unit : quantity.getUnits()) {
        String displaySymbol1 = unitManager.getDisplaySymbol(unit);
        String displaySymbol2 = unitManager.getDisplaySymbol(unit.getSymbol());

        // Assertions.assertEquals(displaySymbol1, displaySymbol2);
      }
    }

    //
    // Verify that non-existing unit symbols are returned as they are
    //
    for (int i = 0; i < 100; i++) {
      String unitSymbol = Random.getAsciiString(10, 20);
      String displaySymbol = unitManager.getDisplaySymbol(unitSymbol);

      Assertions.assertEquals(unitSymbol, displaySymbol);
    }
  }

  /**
   * Test the Quantity.toString() method.
   */
  @Test
  public void testToString()
  {
    String s = UnitManager.getInstance().toString();
    Assertions.assertNotNull(s);
    Assertions.assertTrue(s.length() > 0);
  }
}

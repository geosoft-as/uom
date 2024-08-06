# UoM - Units of measurement library

![UoM Library](https://geosoft.no/images/UomBox.250.png)

When dealing with scientific data it is essential to know the units of
measurement in order to _understand_ and present the information correctly.
Likewise, in order to do _computations_ with scientific data it is essential
that software is able to convert data into a common unit framework.

The GeoSoft UoM library is a convenient, extensible front-end to the
[OSDU/Energistics Unit of Measure](http://www.energistics.org/asset-data-management/unit-of-measure-standard> database.
It contains more than 250 different quantities with more than
2500 unit definitions.
The API is simple, well documented and easy to use, and the library is trivial
to embed in any scientific software system.

UoM is available for Java (<tt>Uom.jar</tt>) and .Net (<tt>Uom.dll</tt>).
The library is lightweight (&lt; 0.1MB) and self-contained; It embeds the complete
OSDU/Energistics unit database and has no external dependencies.

UoM webpage: https://geosoft.no/uom.html


## Setup

Capture the UoM code to local disk by:

```
$ git clone https://<token>@github.org/github/uom --branch <branch-name>
```

After the initial clone, the directory must be renamed from uom to Uom
in order for the make based tools to work.


## Dependencies

UoM has no external dependenies. The OSDU/Energistics unit database
(`./src/no/geosoft/uom/witsmlUnitDict.xml`) is embedded in the library.


## Examples

The easiest way to get started with UoM is to explore the predefined
OSDU/Energistics quantities and units:

```java
import no.geosoft.uom.Quantity;
import no.geosoft.uom.Unit;
import no.geosoft.uom.UnitManager;

:

//
// Get the unit manager singleton
//
UnitManager unitManager = UnitManager.getInstance();

//
// Get all pre-defined quantities and their units
//
List<Quantity> quantities = uniteManager.getQuantities();
for (Quantity : quantities) {
  System.out.println("Quantity: " + quantity.getName() + " - " + quantity.getDescription();
  for (Unit unit : quantity.getUnits())
    System.out.println("  Unit: " + unit.getSymbol() + " (" + unit.getName() + ")";
}

:
```


### Unit conversion

Basic unit conversion is done through `UnitManager`
using instances of `Unit` or the unit symbols directly:

```java
//
// Convert between known units, using unit symbols directly
//
double milesPerHour = 55.0;
double kilometersPerHour = unitManager.convert("mi/h", "km/h", milesPerHour);

//
// Conversion using Unit instances
//
Unit feet = unitManager.findUnit("ft");

double lengthFt = 8981.0; // Length of the Golden Gate bridge in feet

Quantity quantity = unitManager.findQuantity("length");
for (Unit unit : quantity.getUnits()) {
  double length = unitManager.convert(feet, unit, lengthFt);
  System.out.println("Golden Gate is " + length + " " + unit.getSymbol());
}
```

Making a user interface units aware includes associating
GUI components with quantities and then provide unit conversions,
either per element or as overall preference settings.

It is essential that the application knows the initial unit of measure
of the values involved. A common advice to reduce complexity and risk of errors
is to keep the entire data model in _base_ units (typically SI or similar)
and convert in GUI only on users request. The associated units will then
be _implied_, effectively making the entire business logic _unitless_.
Conversions to and from base units can be performed directly on the `Unit` instances:

```java
//
// Capture pressure display unit from GUI or prefernces
//
Unit diplsayUnit = ...;
String displaySymbol = unitManager.getDisplaySymbol(diplsayUnit.getSymbol());

//
// Populate GUI element
//
double pressure = ...; // From business model, SI implied
pressureText.setText(displayUnit.fromBase(pressure) + " [" + displaySymbol + "]");

:

//
// Capture user input
//
double value = pressureText.getValue(); // In user preferred unit
double pressure = displayUnit.toBase(value); // Converted to business model unit (SI)
```


It may make sense to provide unit conversion even if the quantity of a measure
is unknown. In these cases it is possible to obtain the quantity, but it might
be more convenient to get all convertible units directly:

```java
//
// Given a unit, find the associated quantity
//
String unitSymbol = "degC"; // Degrees Celsius
Quantity quanitity = unitManager.findQuantity(unitSymbol);
List<Unit> units = quantity.getUnits(); // All temperature units

:

//
// Given a unit, find all convertible units
//
String unitSymbol = "degC"; // Degrees Celsius
List<Unit> units = unitManager.findConvertibleUnits(unitSymbol);
```


### Unit aliases

There is no universal accepted standard or convention for unit symbols, and
to make the module more robust when dealing with units from various sources
it is possible to add unit _aliases_. UoM uses the unit symbol defined
by OSDU/Energistics, but have added many aliases for common notations.
In addition, client applications can supply their own:

```
unitManager.addUnitAlias("m/s^2", "m/s2");
unitManager.addUnitAlias("inch", "in");
unitManager.addUnitAlias("api", "gAPI");
unitManager.addUnitAlias("deg", "dega");
:
```


The typical approach would be to read these from a properties file during startup.

### Display symbols

Unit symbols should be regarded as _IDs_, and clients
should never expose these directly in a user interface.
A GUI friendly _display symbol_ may be obtained through
the `UnitManager.getDisplaySymbol()` method.

Many symbols will obviously equal their associated display symbol,
but the table below indicates some of the many that doesn't.
The table shows the connection between _unit name_,
_unit symbol_ and _display symbol_:


| Unit name             | Unit symbol | Display symbol   |
|--------------------------------------------------------|
| microseconds per foot | us/ft       | &#181;s/ft       |
| ohm meter             | ohmm        | &#8486;&middot;m |
| cubic centimeters     | cm3         | cm<sup>3</sup>   |
| degrees Celcius       | degC        | &deg;C           |
| meter/second squared  | m/s2        | m/s<sup>2</sup>  |
| etc.                  |             |                  |


As for unit aliases, it is possible for clients to supply their own
specific display symbols through the `UnitManager.setDisplaySymbol()` method.


### Extensibility

If the predefined set of quantities and units is not sufficient, a client may
easily supply their own:

```java
//
// Define "computer storage" quantity with associated units
//
Quantity q = new Quantity("computer storage");
q.addUnit(new Unit("byte", "byte", 1.0, 0.0, 0.0, 1.0), true);
q.addUnit(new Unit("kilo byte", "kB", 1.0e3, 0.0, 0.0, 1.0), false);
q.addUnit(new Unit("mega byte", "MB", 1.0e6, 0.0, 0.0, 1.0), false);
q.addUnit(new Unit("giga byte", "GB", 1.0e9, 0.0, 0.0, 1.0), false);
:
unitManager.addQuantity(q);

//
// Test the new units
//
long nBytes = 1230000L;
double nMegaBytes = unitManager.convert("byte", "MB", nBytes); // 1.23
```



## Building

UoM can be built from its root folder by

```
$ make clean
$ make
$ make jar
```

The UoM delivery will be the `./lib/Uom.jar` file.

Building with make requires the make module of the tools reprository.



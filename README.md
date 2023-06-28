# youkon
YouKon is a multiplatform mobile app used for unit conversions and projects for scientists engineers.
It allows you to group several related parameters, and convert each to a consistent unit system, regardless of the what each system each was originally defined for.
Examples where this may be useful:
- Mass properties and dimensions of an aerospace vehicle
- Density, elastic, and strength properties of a material
- Measurement quantities for a recipe
- ... and many more

![App Icon](assets/icon.png)

## User Interface

## Shared Architecture

```mermaid
classDiagram
UserData --> Project
Project --> Measurement
class UserData {
+String name
+Array~Project~ projects
}
class Project {
+String name
+String description
+Array~Measurement~ measurement
+Array~String~ images
}
class Measurement {
+String name
+String description
+Double value
+Measurement.Unit unit
} 
```

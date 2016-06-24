---
title: Class Hierarchy
layout: view
blurb: Displays the asserted or inferred class hierarchy for the active ontologies.
menuPath: Class views > Class hierarchy
---
The class hierarchy view displays the asserted and inferred class hierarchies.  The asserted class hierarchy is visible by default.

The asserted class hierarchy view is one of the primary navigation devices in {{site.protege}}.  It is presented as a tree where tree nodes correspond to classes.  A child node represents a class that is a subclass of the class represented by the parent node.

A class will be shown under another class if it is asserted to be a SubClassOf that other class, or if it is asserted to be EquivalentTo a class expression that is an intersection containing that other class as an operand.  For example, if the ontology contains ```A SubClassOf B```, then ```A``` will appear under ```B``` in the tree.  Similarly, if the ontology contains ```E Equivalent to B and hasPart some C``` then ```E``` will also be shown as a chid of ```B``` in the tree.  Any classes that are not asserted to be a subclass of some other class will show up directly under ```owl:Thing``` (the root).

Note that names in the class hierarchy are quoted using single quotes if the name contains spaces.  The quotes, however, are not part of the name.

## Switching between Asserted and Inferred modes

The asserted class hierarchy is shown by default.  To switch to the inferred hierarchy either select "Inferred" from the drop-down or press 'i' when the hierarchy has the focus.  If the inferred hierarchy is shown, the asserted hierarchy can be displayed by selecting "Asserted" from the drop-down or press 'a' when the hierarchy has the focus.

## Icons

The class hierarchy contains two types of icons: Solid yellow icons and yellow icons with three white lines through the middle, representing the equivalence symbol.

The icons containing the equivalence symbol (three white lines) denote classes that are asserted to be equivalent to some other class expression.  These are known as defined classes.

<figure>
  <img src="{{site.baseurl}}/assets/views/class-hierarchy/defined-class-example.png" width="55px"/>
  <figcaption>An example of a defined class</figcaption>
</figure>

The solid yellow icons denote classes that are not defined classes (i.e. aren't asserted to be equivalent to some other class expression).  These classes are known as primitive classes.

<figure>
<img src="{{site.baseurl}}/assets/views/class-hierarchy/primitive-class-example.png" width="55px"/>
<figcaption>An example of a primitive class</figcaption>
</figure>

## Bolding

The class hierarchy view may show some names in a bold font and others in a regular font.  Roughly speaking, classes whose names are shown in a bold font are described using axioms in the active ontology.  This means that the class appears on the left hand side of a SubClassOf axiom, in an EquivalentClasses axiom, in a DisjointUnion axiom or in a DisjointClasses axiom.  Classes whose names are shown in a regular weight font are merely referenced in the imports closure of the active ontology.

<figure>
<img src="{{site.baseurl}}/assets/views/class-hierarchy/class-hierarchy-bolding.png" width="150px"/>
<figcaption>An example the bolding used in the class hierarchy.  Class B is shown in bold because the active ontology contains axioms that describe it.  Class A is not shown in bold because it is merely referenced by the active ontology (in the description of class B).</figcaption>
</figure>

## Drag and Drop

It is possible to edit some of the SubClassOf axioms in the ontology by dragging and dropping tree nodes in the asserted class hierarchy.  Drag and Drop only works for primitive classes - that is, classes that have a solid yellow icon.  Dropping a class on top of an other class will make it a SubClassOf that other class.

## Cycles

Classes that appear in a cycle of SubClassOf axioms, for example ```A SubClassOf B```, ```B SubClassOf C``` and ```C SubClassOf A``` will be collapsed and shown together, with one tree node for each class in the cycle so that each class may be selected.  An example is shown below.  

<figure>
<img src="{{site.baseurl}}/assets/views/class-hierarchy/class-hierarchy-cylces.png" width="200px"/>
<figcaption>An example of a cycle in the class hierarchy.  Classes in the cycle are displayed as equivalences, with a node for each class.</figcaption>
</figure>

The reason for this notation is that a cycle in the class hierarchy between two or more classes states that the classes in the cycle are equivalent.  This is because ```A EquivalentTo B``` is an abbreviation for the two axioms ```A SubClassOf B``` and ```B SubClassOf A```.

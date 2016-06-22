---
title: Annotations
layout: view
blurb: Displays annotation assertions for the selected entity.
---
The annotations view displays annotation assertions for the selected entity.  The view lists annotations sorted by their annotation property.  Certain well known properties, for example ```rdfs:label``` have a higher priority in the sorting over other annotation properties and will always appear at the top of the view.

For each annotation displayed in the view, the rendering includes the annotation property (shown in blue), the annotation value datatype of language tag (shown in light gray) and the actual annotation value.

Annotations on annotations are shown inline by default.  In the image below, the definition annotation has two other annotations on it. These can be hidden by toggling [Display axiom annotations inline]({{site.baseurl}}/menus/display-axiom-annotations-inline).



## Annotation editor

To edit an annotation, or add a new annotation, you should use the annotation editor, which is displayed when you click the (+) button or the edit button (o) or when you double click on an annotation.

<figure>
  <img src="{{site.baseurl}}/assets/views/annotations/annotations-editor.png" alt="The annotation editor">
  <figcaption>The annotation editor.  The annotation property is specified by selecting it from the tree on the left hand side.  The value is entered into one of the tabs on the right hand side.</figcaption>
</figure>

The annotation editor is split into a left hand side pane and a right hand side pane.

Select an annotation property from the left (or add your own on in the top section if you want).

On the right hand side you specify a value for the annotation.  You can specify:

* A **Literal**, which is a string or number.  The literal may have a language tag or it may be typed with a datatype.  You can specify a type with the Type dropdown, or you can specify a language tag in the language tag editor.
* An **Entity IRI** that points to an entity in the ontology or imports closure.
* An **Arbitrary IRI** that may point at anything.
* An **Anonymous Individual** and associated property values.

## Ontology Annotations

The annotations on the active ontology are shown in the Ontology Annotations view.  This view also shows the Ontology IRI and Ontology Version IRI pair for the active ontology.  Both of these fields will be empty in the case of an anonymous ontology.  If an ontology has an Ontology IRI then the Ontology Version IRI may be empty.

In the example below, which is taken from the UBERON ontology, the Ontology IRI is ```http://purl.obolibrary.org/obo/uberon.owl``` and the Ontology version IRI is ```http://purl.obolibrary.org/obo/uberon/releases/2016-06-12/uberon.owl```.

<figure>
  <img src="{{site.baseurl}}/assets/views/annotations/ontology-annotations.png" alt="The ontology annotation editor"/>
  <figcaption>The ontology annotations view</figcaption>
</figure>

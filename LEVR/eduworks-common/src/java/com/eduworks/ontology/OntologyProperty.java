package com.eduworks.ontology;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLProperty;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

@SuppressWarnings("rawtypes")
public class OntologyProperty extends OntologyWrapper {

	public static OntologyProperty createProperty(Ontology ont, String identifier, JSONObject vals){
		// TODO: look through vals for things that indicate the type (range types, specific object property flags)
		throw new RuntimeException("Not implemented yet");
	}
	
	// TODO: All Value Keys should be case-insensitive
	public static OntologyProperty createDataProperty(Ontology ont, String identifier, JSONObject vals){
		IRI iri = IRI.create("#"+ identifier);
		String newPropIri = ont.getBaseIRI() + "#" + identifier;
		
		DatatypeProperty newJenaProp;
		
		// TODO: Check Existence for Jena
		
		try{
			if(vals.has("functional") && vals.getBoolean("functional")){
				newJenaProp = ont.getJenaModel().createDatatypeProperty(newPropIri, true);
			}else{
				newJenaProp = ont.getJenaModel().createDatatypeProperty(newPropIri, false);
			}
			
			if(vals.has("domain")){
				JSONArray domainIds = new JSONArray();
				
				String domainIdString = vals.getString("domain");
				if(domainIdString.startsWith("[") && domainIdString.endsWith("]")){
					domainIds = vals.getJSONArray("domain");
				}else{
					domainIds.put(domainIdString);
				}
				
				getDataDomainAxioms(ont, newJenaProp, domainIds);
			}
			
			if(vals.has("range")){
				getDataRangeAxiom(ont, newJenaProp, vals.getString("range"));
			}	
			
			
			if(vals.has("subpropertyOf")){
				JSONArray superPropIds = new JSONArray();
				
				String domainIdString = vals.getString("subpropertyOf");
				if(domainIdString.startsWith("[") && domainIdString.endsWith("]")){
					superPropIds = vals.getJSONArray("subpropertyOf");
				}else{
					superPropIds.put(domainIdString);
				}
				
				getDataSubPropertyAxioms(ont, newJenaProp, superPropIds);
			}
		}catch(JSONException e){
			throw new RuntimeException("Shouldn't happen, we check that the key exists");
		}
		
		
		return new OntologyProperty(ont, newJenaProp);
	}
	
	// TODO: All Value Keys should be case-insensitive
	public static OntologyProperty createObjectProperty(Ontology ont, String identifier, JSONObject vals){
		
		IRI iri = IRI.create("#"+ identifier);
		String newPropIri = ont.getBaseIRI() + "#" + identifier;
		
		ObjectProperty newJenaProp;
		
		// TODO: Check existence in Jena
		
		try{			
			if(vals.has("functional") && vals.getBoolean("functional")){
				newJenaProp = ont.getJenaModel().createObjectProperty(newPropIri, true); 
			}else{
				newJenaProp = ont.getJenaModel().createObjectProperty(newPropIri, false);
			}
			
			
			if(vals.has("domain")){
				JSONArray domainIds = new JSONArray();
				
				String domainIdString = vals.getString("domain");
				if(domainIdString.startsWith("[") && domainIdString.endsWith("]")){
					domainIds = vals.getJSONArray("domain");
				}else{
					domainIds.put(domainIdString);
				}
				
				getObjectDomainAxioms(ont, newJenaProp, domainIds);
			}
			
			if(vals.has("range")){
				JSONArray rangeIds = new JSONArray();
				
				String rangeIdString = vals.getString("range");
				if(rangeIdString.startsWith("[") && rangeIdString.endsWith("]")){
					rangeIds = vals.getJSONArray("range");
				}else{
					rangeIds.put(rangeIdString);
				}
				
				getObjectRangeAxioms(ont, newJenaProp, rangeIds);
			}
			
			if(vals.has("subpropertyOf")){
				JSONArray superPropIds = new JSONArray();
				
				String domainIdString = vals.getString("subpropertyOf");
				if(domainIdString.startsWith("[") && domainIdString.endsWith("]")){
					superPropIds = vals.getJSONArray("subpropertyOf");
				}else{
					superPropIds.put(domainIdString);
				}
				
				getObjectSubPropertyAxioms(ont, newJenaProp, superPropIds);
			}
			
			if(vals.has("inverses")){
				JSONArray inversePropIds = new JSONArray();
				
				// TODO: Probably shouldn't be an array.
				String invPropIdString = vals.getString("inverses");
				if(invPropIdString.startsWith("[") && invPropIdString.endsWith("]")){
					inversePropIds = vals.getJSONArray("inverses");
				}else{
					inversePropIds.put(invPropIdString);
				}
				getInversePropertyAxioms(ont, newJenaProp, inversePropIds);
			}
			
			if(vals.has("symmetric") && vals.getBoolean("symmetric")){
				newJenaProp.addProperty(RDF.type, OWL.SymmetricProperty);
			}
			
			if(vals.has("inverse-functional") && vals.getBoolean("inverse-functional")){
				newJenaProp.addProperty(RDF.type, OWL.InverseFunctionalProperty);
			}
			
			if(vals.has("transitive") && vals.getBoolean("transitive")){
				newJenaProp.addProperty(RDF.type, OWL.TransitiveProperty);
			}
			if(vals.has("asymmetric") && vals.getBoolean("asymmetric")){
				newJenaProp.addProperty(RDF.type, OWL2.AsymmetricProperty);
			}
			if(vals.has("reflexive") && vals.getBoolean("reflexive")){
				newJenaProp.addProperty(RDF.type, OWL2.ReflexiveProperty);
			}
			if(vals.has("irreflexive") && vals.getBoolean("irreflexive")){
				newJenaProp.addProperty(RDF.type, OWL2.IrreflexiveProperty);
			}
			
		}catch(JSONException e){
			throw new RuntimeException("Shouldn't happen, we check that the key exists");
		}
		
		return new OntologyProperty(ont, newJenaProp);
	}
	
	private static void getDataDomainAxioms(Ontology ont, DatatypeProperty jenaProp, JSONArray domainIds){
		for(int i = 0; i < domainIds.length(); i++){
			try{
				OntologyClass cls = ont.getClass(domainIds.getString(i));
				
				OntClass jenaCls = cls.getJenaClass();
				jenaProp.addDomain(jenaCls);
			}catch(JSONException e){
				throw new RuntimeException("Shouldn't happen, we loop only through the size of the array");
			}
		}
	}
	
	private static void getObjectDomainAxioms(Ontology ont, ObjectProperty jenaProp, JSONArray domainIds){
		for(int i = 0; i < domainIds.length(); i++){
			try{
				OntologyClass cls = ont.getClass(domainIds.getString(i));
				
				OntClass jenaCls = ont.getJenaModel().getOntClass(cls.getFullId());
				jenaProp.addDomain(jenaCls);
			}catch(JSONException e){
				throw new RuntimeException("Shouldn't happen, we loop only through the size of the array");
			}
		}
	}
	
	private static void getDataRangeAxiom(Ontology ont, DatatypeProperty jenaProp, String dataRange){
		
		if(dataRange.startsWith("xsd:")){
			if(dataRange.equals("xsd:string")){
				jenaProp.addRange(XSD.xstring);
			}else if(dataRange.equals("xsd:integer")){
				jenaProp.addRange(XSD.xint);
			}else if(dataRange.equals("xsd:double")){
				jenaProp.addRange(XSD.xdouble);
			}else{
				throw new RuntimeException("unknown data property range: " + dataRange);
			}
		
		}else{
			throw new RuntimeException("unknown data property range: " + dataRange);
		}
	}
	
	private static void getObjectRangeAxioms(Ontology ont, ObjectProperty jenaProp, JSONArray rangeIds){
		for(int i = 0; i < rangeIds.length(); i++){
			try{
				OntologyClass cls = ont.getClass(rangeIds.getString(i));
				
				OntClass jenaCls = ont.getJenaModel().getOntClass(cls.getFullId());
				jenaProp.addDomain(jenaCls);
			}catch(JSONException e){
				throw new RuntimeException("Shouldn't happen, we loop only through the size of the array");
			}
		}
	}
	
	private static void getDataSubPropertyAxioms(Ontology ont, DatatypeProperty jenaProp, JSONArray superPropIds){
		try{
			for(int i = 0; i < superPropIds.length(); i++){
				OntologyProperty superProp = ont.getProperty(superPropIds.getString(i));
				
				if(superProp.getJenaProperty().isDatatypeProperty()){
					jenaProp.addSuperProperty(superProp.getJenaProperty());
				}else{
					throw new RuntimeException("Data Property <"+getIdentifier(jenaProp.getURI())+"> cannot be a subProperty of non-data property <"+superPropIds.getString(i)+">");
				}
			}
		}catch(JSONException e){
			throw new RuntimeException("Error getting super-property Id");
		}
		
	}
	
	private static void getObjectSubPropertyAxioms(Ontology ont, ObjectProperty jenaProp, JSONArray superPropIds){
		try{
			for(int i = 0; i < superPropIds.length(); i++){
				OntologyProperty superProp = ont.getProperty(superPropIds.getString(i));
				
				if( superProp.isObjectProperty()){
					jenaProp.addSuperProperty(superProp.getJenaProperty());
				}else{
					throw new RuntimeException("Object Property <"+getIdentifier(jenaProp.getURI())+"> cannot be a subProperty of non-object property <"+superPropIds.getString(i)+">");
				}
			}
		}catch(JSONException e){
			throw new RuntimeException("Error getting super-property Id");
		}
	}
	
	private static void getInversePropertyAxioms(Ontology ont, ObjectProperty jenaProp, JSONArray inversePropIds){
		for(int i = 0; i < inversePropIds.length(); i++){
			try{
				String invPropId = inversePropIds.getString(i);
				
				OntologyProperty invP = ont.getProperty(invPropId);
				
				if(invP.isObjectProperty()){
					jenaProp.addInverseOf(invP.getJenaProperty());
				}else{
					throw new RuntimeException("Property ("+invPropId+") is not an Object Property and cannot be an inverse property");
				}
				
			}catch(JSONException e){
				throw new RuntimeException("This shouldn't occur, we test how many items before looping");
			}
		}
	}
	
	
	
	Ontology ont;
	OntProperty jenaProp;
	
	/**
	 * (DEPRECATED)
	 * Creates an OntologyProperty Wrapper, by looking through ontology given for the property with the specified IRI. There is no guarantee
	 * that the property returned is not a new property
	 * @param o
	 * @param iri
	 */
	public OntologyProperty(Ontology o, IRI iri){
		ont = o;
		
		String s = idCharacter+iri.getFragment();
		
//		if(o.getDataPropertyIdList().contains(s)){
//			_prop = ont.getOWLDataFactory().getOWLDataProperty(iri);
//		}else if(o.getObjectPropertyIdList().contains(s)){
//			_prop = ont.getOWLDataFactory().getOWLObjectProperty(iri);
//		}else{
//			throw new RuntimeException("Could not find Property with id: "+s);
//		}
	}
	
	/**
	 * Creates an OntologyProperty Wrapper to wrap the OWLAPI OWLProperty passed in, assumes that the ontology passed in is the 
	 * owner of the property
	 * @param prop
	 * @param o
	 */
	public OntologyProperty(Ontology o, OWLProperty prop, OntProperty jena){
		// TODO: May want to check whether ontology contains the property
		o.getJenaModel().enterCriticalSection(true);
		if(!o.getJenaModel().containsResource(jena)){
			throw new RuntimeException("Property ("+jena.getId()+") is not contained in the ontology at: "+o.getTDBDir());
		}
		o.getJenaModel().leaveCriticalSection();
		
		ont = o;
		jenaProp = jena;
	}
	
	public OntologyProperty(Ontology o, OntProperty prop){
		this(o, null, prop);
	}
	
	public void update(JSONObject newVals){
		JSONObject oldValues = this.getJSONRepresentation();
		JSONObject currentValues = new JSONObject();
		
		Iterator<String> k = oldValues.keys();
		
		while(k.hasNext()){
			String key = k.next();
			try {
				currentValues.put(key, oldValues.get(key));
			} catch (JSONException e) {
				throw new RuntimeException("error copying current values to old values");
			}
		}
		
		try{
			// Data Property 		
			if(isDataProperty()){
				
				
				if(newVals.has("domain")){
					for(OntResource domain : jenaProp.listDomain().toSet()){
						if( !domain.equals(OWL2.Thing) && !domain.equals(RDFS.Resource)){
							jenaProp.removeDomain(domain);
						}
					}
					
					JSONArray domainIds = new JSONArray();
					
					String domainIdString = newVals.getString("domain");
					if(domainIdString.startsWith("[") && domainIdString.endsWith("]")){
						domainIds = newVals.getJSONArray("domain");
					}else if(!domainIdString.isEmpty()){
						domainIds.put(domainIdString);
					}
					
					if(domainIds.length() != 0 && !domainIds.getString(0).isEmpty()){
						getDataDomainAxioms(ont, jenaProp.asDatatypeProperty(), domainIds);
					}
				}
				
				
				if(newVals.has("range")){
					for(OntResource range : jenaProp.listRange().toSet()){
						jenaProp.removeRange(range);
					}
					
					if(!newVals.getString("range").isEmpty()){
						getDataRangeAxiom(ont, jenaProp.asDatatypeProperty(), newVals.getString("range"));
					}
				}
				
				if(newVals.has("functional")){
					if(newVals.getBoolean("functional") && !oldValues.getBoolean("functional")){
						jenaProp.addProperty(RDF.type, OWL.FunctionalProperty);
					}else if(!newVals.getBoolean("functional") && oldValues.getBoolean("functional")){
						jenaProp.removeProperty(RDF.type, OWL.FunctionalProperty);
					}
				}
			
				if(newVals.has("subpropertyOf")){
					for(OntProperty superProp : jenaProp.listSuperProperties().toSet()){
						jenaProp.removeSuperProperty(superProp);
					}
					
					JSONArray superPropIds = new JSONArray();
					
					String superPropIdString = newVals.getString("subpropertyOf");
					if(superPropIdString.startsWith("[") && superPropIdString.endsWith("]")){
						superPropIds = newVals.getJSONArray("subpropertyOf");
					}else if(!superPropIdString.isEmpty()){
						superPropIds.put(superPropIdString);
					}
					
					if(superPropIds.length() != 0 && !superPropIds.getString(0).isEmpty()){
						getDataSubPropertyAxioms(ont, jenaProp.asDatatypeProperty(), superPropIds);
					}
				}
			
			// Object Property
			}else{
				if(newVals.has("domain")){					
					for(OntResource domain : jenaProp.listDomain().toSet()){
						jenaProp.removeDomain(domain);
					}
					
					JSONArray domainIds = new JSONArray();
					
					String domainIdString = newVals.getString("domain");
					if(domainIdString.startsWith("[") && domainIdString.endsWith("]")){
						domainIds = newVals.getJSONArray("domain");
					}else if(!domainIdString.isEmpty()){
						domainIds.put(domainIdString);
					}
					
					if(domainIds.length() != 0 && !domainIds.getString(0).isEmpty()){
						getObjectDomainAxioms(ont, jenaProp.asObjectProperty(), domainIds);
					}
				}
				
				
				if(newVals.has("range")){
					for(OntResource range : jenaProp.listRange().toSet()){
						jenaProp.removeRange(range);
					}
					
					JSONArray rangeIds = new JSONArray();
					
					String rangeIdString = newVals.getString("range");
					if(rangeIdString.startsWith("[") && rangeIdString.endsWith("]")){
						rangeIds = newVals.getJSONArray("range");
					}else if(!rangeIdString.isEmpty()){
						rangeIds.put(rangeIdString);
					}
					
					if(rangeIds.length() != 0 && !rangeIds.getString(0).isEmpty()){
						getObjectRangeAxioms(ont, jenaProp.asObjectProperty(), rangeIds);
					}
				}
				
				if(newVals.has("inverse")){
					if(jenaProp.getInverse() != null){
						jenaProp.removeInverseProperty(jenaProp.getInverse());
					}
					
					JSONArray inversePropIds = new JSONArray();
					
					String invPropIdString = newVals.getString("inverse");
					if(invPropIdString.startsWith("[") && invPropIdString.endsWith("]")){
						inversePropIds = newVals.getJSONArray("inverse");
					}else if(!invPropIdString.isEmpty()){
						inversePropIds.put(invPropIdString);
					}
					
					if(inversePropIds.length() != 0 && !inversePropIds.getString(0).isEmpty()){
						getInversePropertyAxioms(ont, jenaProp.asObjectProperty(), inversePropIds);
					}
				}
				
				if(newVals.has("subpropertyOf")){
					
					for(OntProperty superProp : jenaProp.listSuperProperties().toSet()){
						jenaProp.removeSuperProperty(superProp);
					}
					
					JSONArray superPropIds = new JSONArray();
					
					String superPropIdString = newVals.getString("subpropertyOf");
					if(superPropIdString.startsWith("[") && superPropIdString.endsWith("]")){
						superPropIds = newVals.getJSONArray("subpropertyOf");
					}else if(!superPropIdString.isEmpty()){
						superPropIds.put(superPropIdString);
					}
					
					if(superPropIds.length() != 0 && !superPropIds.getString(0).isEmpty()){
						getObjectSubPropertyAxioms(ont, jenaProp.asObjectProperty(), superPropIds);
					}
				}
				
				if(newVals.has("functional")){
					if(newVals.getBoolean("functional") && !oldValues.getBoolean("functional")){
						jenaProp.addProperty(RDF.type, OWL.FunctionalProperty);
					}else if(!newVals.getBoolean("functional") && oldValues.getBoolean("functional")){
						jenaProp.removeProperty(RDF.type, OWL.FunctionalProperty);
					}
				}
				
				if(newVals.has("symmetric")){
					if(newVals.getBoolean("symmetric") && !oldValues.getBoolean("symmetric")){
						jenaProp.addProperty(RDF.type, OWL.SymmetricProperty);
					}else if(!newVals.getBoolean("symmetric") && oldValues.getBoolean("symmetric")){
						jenaProp.removeProperty(RDF.type, OWL.SymmetricProperty);
					}
				}
				
				if(newVals.has("inverse-functional")){
					if(newVals.getBoolean("inverse-functional") && !oldValues.getBoolean("inverse-functional")){
						jenaProp.addProperty(RDF.type, OWL.InverseFunctionalProperty);
					}else if(!newVals.getBoolean("inverse-functional") && oldValues.getBoolean("inverse-functional")){
						jenaProp.removeProperty(RDF.type, OWL.InverseFunctionalProperty);
					}
				}
				
				if(newVals.has("transitive")){
					if(newVals.getBoolean("transitive") && !oldValues.getBoolean("transitive")){
						jenaProp.addProperty(RDF.type, OWL.TransitiveProperty);
					}else if(!newVals.getBoolean("transitive") && oldValues.getBoolean("transitive")){
						jenaProp.removeProperty(RDF.type, OWL.TransitiveProperty);
					}
				}
				
				if(newVals.has("asymmetric")){
					if(newVals.getBoolean("asymmetric") && !oldValues.getBoolean("asymmetric")){
						jenaProp.addProperty(RDF.type, OWL2.AsymmetricProperty);
					}else if(!newVals.getBoolean("asymmetric") && oldValues.getBoolean("asymmetric")){
						jenaProp.removeProperty(RDF.type, OWL2.AsymmetricProperty);
					}
				}
				
				if(newVals.has("reflexive")){
					if(newVals.getBoolean("reflexive") && !oldValues.getBoolean("reflexive")){
						jenaProp.addProperty(RDF.type, OWL2.ReflexiveProperty);
					}else if(!newVals.getBoolean("reflexive") && oldValues.getBoolean("reflexive")){
						jenaProp.removeProperty(RDF.type, OWL2.ReflexiveProperty);
					}
				}
				
				if(newVals.has("irreflexive")){
					if(newVals.getBoolean("irreflexive") && !oldValues.getBoolean("irreflexive")){
						jenaProp.addProperty(RDF.type, OWL2.IrreflexiveProperty);
					}else if(!newVals.getBoolean("irreflexive") && oldValues.getBoolean("irreflexive")){
						jenaProp.removeProperty(RDF.type, OWL2.IrreflexiveProperty);
					}
				}
			}
		}catch(JSONException e){
			throw new RuntimeException("Error Reading New Values: "+e.getMessage());
		}
		
	}
	
	/**
	 * Delete's the  property and removes any references to it from the ontology 
	 */
	public void delete(){
		jenaProp.remove();
	}
	
	
	/**
	 * Returns a JSONObject representing this property of the form:
	 * {
	 * 		domain: [@<classId>, ...]
	 * 		range: [<type>, ...]
	 * 		functional: <true/false>	
	 * }
	 * If the property is an object property it will also have the following true/false fields:
	 * symmetric, inverse-functional, transitive, asymmetric, reflexive, and irreflexive. It will
	 * also have the following 'inverses' @<propertyId> field
	 */
	public JSONObject getJSONRepresentation() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("domain", getDomain());
			obj.put("range", getRange());
			
			//obj.put("functional", _prop.isFunctional(ont.getOWLOntology()));
			obj.put("functional", jenaProp.isFunctionalProperty());
			
			JSONObject subProperties = new JSONObject();
			for(OntologyProperty subProp : getSubProperties()){
				subProperties.put(subProp.getId(), subProp.getJSONRepresentation());
			}
			obj.put("subproperties", subProperties);
			
			JSONArray superProperties = new JSONArray();
			for(OntologyProperty superProp : getSuperProperties()){
				superProperties.put(superProp.getId());
			}
			obj.put("superproperties", superProperties);
			
			if(isObjectProperty()){
				//OWLObjectProperty p = _prop.asOWLObjectProperty();
				
				
				
//				JSONArray inverses = new JSONArray();
//				for (OWLObjectPropertyExpression invP : p.getInverses(ont.getOWLOntology())){
//					if(!invP.isAnonymous()){
//						inverses.put(new OntologyProperty(ont, invP.asOWLObjectProperty()).getId());
//					}
//				}
								
				OntProperty inverse = jenaProp.getInverse();
				if(inverse == null){
					obj.put("inverse", "");
				}else{
					obj.put("inverse", new OntologyProperty(ont, null, inverse).getId());
				}
				
				
				
				
				//obj.put("symmetric", p.isSymmetric(ont.getOWLOntology()));
				obj.put("symmetric", jenaProp.isSymmetricProperty());
				
				//obj.put("inverse-functional", p.isInverseFunctional(ont.getOWLOntology()));
				obj.put("inverse-functional", jenaProp.isInverseFunctionalProperty());
				
				//obj.put("transitive", p.isTransitive(ont.getOWLOntology()));
				obj.put("transitive", jenaProp.isTransitiveProperty());
				
				//obj.put("asymmetric", p.isAsymmetric(ont.getOWLOntology()));
				obj.put("asymmetric", jenaProp.hasProperty(RDF.type, OWL2.AsymmetricProperty));
				
				//obj.put("reflexive", p.isReflexive(ont.getOWLOntology()));
				obj.put("reflexive", jenaProp.hasProperty(RDF.type, OWL2.ReflexiveProperty));
				
				//obj.put("irreflexive", p.isIrreflexive(ont.getOWLOntology()));
				obj.put("irreflexive", jenaProp.hasProperty(RDF.type, OWL2.IrreflexiveProperty));
				
			}
			
		} catch (JSONException e1) {
			
		}
		
		
		return obj;
	}
	
	
	public boolean isDataProperty(){
		return jenaProp.isDatatypeProperty();
	}
	
	public boolean isObjectProperty(){
		return jenaProp.isObjectProperty();
	}
	
	public Set<OntologyProperty> getSubProperties(){
		Set<OntologyProperty> subPropertyObjects = new HashSet<OntologyProperty>();
		
		// TODO: MAKE SURE NULL IS OKAY UNTIL DELETE
		for(OntProperty p : jenaProp.listSubProperties().toSet()){
			if(p.getURI() != jenaProp.getURI()){
				subPropertyObjects.add(new OntologyProperty(ont, null, p));
			}
		}
//		Set<OWLObjectPropertyExpression> subProps = _prop.getSubProperties(ont.getOWLOntology());
//		for(OWLObjectPropertyExpression expr : subProps){
//			
//			//subPropertyObjects.add(new OntologyProperty(ont, expr.getNamedProperty()));
//		}
		
		return subPropertyObjects;
	}
	
	public Set<OntologyProperty> getSuperProperties(){
		Set<OntologyProperty> superPropertyObjects = new HashSet<OntologyProperty>();

		// TODO: MAKE SURE NULL IS OKAY UNTIL DELETE
		for(OntProperty p : jenaProp.listSuperProperties().toSet()){
			superPropertyObjects.add(new OntologyProperty(ont, null, p));
		}
		
//		Set<OWLObjectPropertyExpression> superProps = _prop.getSuperProperties(ont.getOWLOntology());
//		for(OWLObjectPropertyExpression expr : superProps){ 
//			//superPropertyObjects.add(new OntologyProperty(ont, expr.getNamedProperty()));
//		}
		
		return superPropertyObjects;
	}
	
	public JSONArray getDomain(){
		JSONArray arr = new JSONArray();
		
		for(OntResource r : jenaProp.listDomain().toSet()){
			if(r.isClass() && !r.isAnon()){
				// TODO: REMOVE IRI REFERENCE
				arr.put(new OntologyClass(ont, r.asClass()).getId());
			}
		}
		
//		OntologyReasoner reasoner = ont.getReasoner();
//		
//		Set<OntologyClass> set = reasoner.getPropertyDomains(this);
//		
//		// TODO: Going to need to use a reasoner
//		//Set<OWLClassExpression> set = _prop.getDomains(ont.getOWLOntology());
//		for(OntologyClass cls : set){
//			arr.put(cls.getId());
//		}
//		
//		reasoner.dispose();
		
		return arr;
	}
	
	public JSONArray getRange(){
		JSONArray arr = new JSONArray();
	
		
		//if(_prop instanceof OWLDataProperty){
		if(jenaProp.isDatatypeProperty()){	
			for(OntResource r : jenaProp.listRange().toSet()){
				// TODO: ALL OTHER DATA RANGES
				if(r.equals(XSD.xstring)){
					arr.put("xsd:string");
				}else if(r.equals(XSD.xint)){
					arr.put("xsd:integer");
				}else if(r.equals(XSD.xdouble)){
					arr.put("xsd:double");
				}else if(r.equals(XSD.dateTime)){
					arr.put("xsd:dateTime");
				}
			}

		}else{
			for(OntResource r : jenaProp.listRange().toSet()){
				if(r.isClass() && !r.isAnon()){
					arr.put(new OntologyClass(ont, null, r.asClass()).getId());
				}
			}
		}
		
		return arr;
	}
	
	public OntProperty getJenaProperty(){
		return jenaProp;
	}
	
	@Override
	public String getId() {
		return getIdentifier(getFullId());
	}

	@Override
	public String getFullId() {
		return jenaProp.getURI();
	}

}

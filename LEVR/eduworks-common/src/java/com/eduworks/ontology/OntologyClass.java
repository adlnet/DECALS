package com.eduworks.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDatatype;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.XSD;


public class OntologyClass extends OntologyWrapper {
	
	
	public static OntologyClass createClass(Ontology ont, String classId, JSONObject vals){
		String newClassIri = ont.getBaseIRI() + "#" + classId;
		
		OntClass jenaClass = ont.getJenaModel().createClass(newClassIri);
		
		try{
			JSONArray superClassIds = new JSONArray();
			
			// Add Super Classes
			boolean hasThingSuper = false;
			if(vals.has("subclassOf")){
				String superclassIdString = vals.getString("subclassOf");
				
				if(superclassIdString.contains(idCharacter+"Thing")){
					hasThingSuper = true;
				}
					
				if(superclassIdString.startsWith("[") && superclassIdString.endsWith("]")){
					superClassIds = vals.getJSONArray("subclassOf");
				}else{
					superClassIds.put(superclassIdString);
				}
			}	
			
			// Class always has superclass of Thing!
			if(!hasThingSuper){
				superClassIds.put(idCharacter + "Thing");
			}
			
			getSubclassAxioms(ont, jenaClass, superClassIds);
			
			// Class Requirements
			if(vals.has("requirements")){
				try{
					JSONObject req = vals.getJSONObject("requirements");
					
					getRestrictionAxioms(ont, jenaClass, superClassIds, req, true);
				}catch(JSONException e){
					throw new RuntimeException("Requirements must be in the form of a JSONObject {"+idCharacter+"<propertyId>: [intersect-value1, intersect-value2, ...]}");
				}
			}
			
			// Class Restrictions
			if(vals.has("restrictions")){
				try{
					JSONObject res = vals.getJSONObject("restrictions");
					
					getRestrictionAxioms(ont, jenaClass, superClassIds, res, false);
				}catch(JSONException e){
					throw new RuntimeException("Requirements must be in the form of a JSONObject {"+idCharacter+"<propertyId>: [intersect-value1, intersect-value2, ...]}");
				}
			}
		}catch(JSONException e){
			throw new RuntimeException("Not sure why this happened, error accessing Values JSONObject: "+ e.getMessage());
		}
	
		
		// Make Disjoint from sibling classes, why should we have two classes with the same hierarchy who are the same?
		for(OntClass sup : jenaClass.listSuperClasses(true).toSet()){
			for(OntClass sibling : sup.listSubClasses(true).toSet()){
				if(!jenaClass.equals(sibling)){
					jenaClass.addDisjointWith(sibling);
				}
			}
		}
		
		
		return new OntologyClass(ont, jenaClass);
	}
	
	private static void getRestrictionAxioms(Ontology ont, OntClass jenaClass, JSONArray superClassIds, JSONObject requirements, boolean required){
		Iterator<String> k = requirements.keys();
		
		while(k.hasNext()){
			String key = k.next();
			
			if(!key.startsWith(idCharacter)){
				throw new RuntimeException("requirements Property Id does is not an '"+idCharacter+"'-formatted ID");
			}else{
				OntologyProperty prop = ont.getProperty(key);
				
				JSONArray restrictionValues = new JSONArray();
				
				try{
					String restrictionValueString = requirements.getString(key);
					if(restrictionValueString.startsWith("[") && restrictionValueString.endsWith("]")){
						restrictionValues = requirements.getJSONArray(key);
					}else{
						restrictionValues.put(restrictionValueString);
					}
				}catch(JSONException e){
					throw new RuntimeException("error reading from requirements object: "+requirements);
				}
					
				getRestrictionAxiom(ont, jenaClass, prop, superClassIds, restrictionValues, required);
			}	
		}
	}
	
	private static void getRestrictionAxiom(Ontology ont, OntClass jenaClass, OntologyProperty prop, JSONArray superClasses, JSONArray values, boolean required){
		JSONArray propDomains = prop.getDomain();
		JSONArray propRanges = prop.getRange();
		
		if(propDomains.length() > 0){
			for(int i = 0; i < propDomains.length(); i++){
				boolean inDomain = false;
				for(int j = 0; j < superClasses.length(); j++){
					try{
						if(superClasses.getString(j).equals(propDomains.get(i))){
							inDomain = true;
							break;
						}
					}catch(JSONException e){
						throw new RuntimeException("Error reading index "+j+" from superClasses "+superClasses);
					}
				}
				
				if(inDomain){
					break;
				}else if(i == (propDomains.length() - 1)){
					throw new RuntimeException("Property<"+prop.getId()+"> has domain "+propDomains+" that the new class (superClasses: "+superClasses+") does not satisfy");
				}
			}
		}

		if(propRanges.length() > 0){
			for(int i = 0; i < values.length(); i++){
				try{
					String valString = values.getString(i);
					
					boolean inRange = false;
					
					if(valString.startsWith("xsd:")){
						if(prop.isDataProperty()){
							try{
								if(values.length() > 1){
									throw new RuntimeException("Data Properties cannot have more than one DataRange requirement");
								}else if(propRanges.length() == 0 || valString.equals(propRanges.getString(0))){
									inRange = true;
								}
							}catch(JSONException e){
								throw new RuntimeException("Error Reading first value from property ranges: "+propRanges);
							}
						}else{
							throw new RuntimeException("Cannot set Object Property Requirement to a dataType");
						}
					}else if(valString.startsWith(idCharacter)){
						if(prop.isObjectProperty()){
							if(propRanges.length() == 0){
								inRange = true;
							}else{
								for(int j = 0; j < propRanges.length(); j++){
									try{
										Set<String> superIds = new HashSet<String>();
										for(OntologyClass cls : ont.getClass(valString).getSuperClasses()){
											superIds.add(cls.getId());
										}
										if(superIds.contains(propRanges.getString(j))){
											inRange = true;
										}else if(!inRange && j == propRanges.length() - 1){
											throw new RuntimeException("Error property ("+prop.getId()+")has range ("+propRanges+"), but restriction value class ("+valString+") doesn't have super ("+superIds+") in range");
										}
									}catch(JSONException e){
										throw new RuntimeException("Error reading index "+j+" from property ranges: "+propRanges);
									}
								}
							}
						}else{
							// TODO: Probably not true in the case of defined datatypes....
							throw new RuntimeException("Cannot set Data Property Requirement to an identifier");
						}
					}else{
						throw new RuntimeException("Unknown value<"+valString+"> for Property<"+prop.getId()+">");
					}
					
					if(inRange){
						break;
					}else if(i == (values.length() - 1)){
						throw new RuntimeException("Property<"+prop.getId()+"> has range "+propRanges+" that the value<"+valString+"> does not satisfy");
					}
					
				}catch(JSONException e){
					throw new RuntimeException("Error Reading index "+i+" from values: "+values);
				}
			
				
			}
		}
		
		if(prop.isDataProperty()){
			OWLDatatype type;
			Resource dataType;
			
			try{
				String valString = values.getString(0);
				// TODO: Add more Data Types
				if(valString.equalsIgnoreCase("xsd:string")){
					dataType = XSD.xstring;
				}else if(valString.equalsIgnoreCase("xsd:integer")){
					dataType = XSD.xint;
				}else if(valString.equalsIgnoreCase("xsd:double")){
					dataType = XSD.xdouble;
				}else{
					throw new RuntimeException("Unrecognized Data Type<"+valString+"> for Property<"+prop.getId()+">");
				}
			}catch(JSONException e){
				throw new RuntimeException("Error Reading first value from value array: "+values);
			}
			
			if(required){
				jenaClass.addSuperClass( ont.getJenaModel().createSomeValuesFromRestriction(null, prop.getJenaProperty(), dataType));	
			}else{
				jenaClass.addSuperClass(ont.getJenaModel().createAllValuesFromRestriction(null, prop.getJenaProperty(), dataType));
			}	
		}else{
			OntClass restrictedIntersection = null;
			
			for(int i = 0; i < values.length(); i++){
				try{
					OntologyClass cls = ont.getClass(values.getString(i));
					
					//TODO: THIS NEEDS TO BE CHECKED!
					if(restrictedIntersection == null){
						restrictedIntersection = cls.getJenaClass();
					}else{
						RDFList list = ont.getJenaModel().createList(restrictedIntersection.listRDFTypes(false));
						restrictedIntersection = ont.getJenaModel().createIntersectionClass(null, list.append(cls.getJenaClass().listRDFTypes(false)));
					}
					
				}catch(JSONException e){
					throw new RuntimeException("Error Reading index "+i+" from Values :"+values);
				}
			}
			
			if(required){
				Set<OntClass> supers = jenaClass.listSuperClasses().toSet();
				Resource restriction = ont.getJenaModel().createSomeValuesFromRestriction(null, prop.getJenaProperty(), restrictedIntersection);
				jenaClass.addSuperClass(restriction);
				
				supers = jenaClass.listSuperClasses().toSet();
				
			}else{
				jenaClass.addSuperClass(ont.getJenaModel().createAllValuesFromRestriction(null, prop.getJenaProperty(), restrictedIntersection));
				
			}
		}
	}
	
	private static void getSubclassAxioms(Ontology ont, OntClass jenaClass, JSONArray superClassIds){
		
		// Iterate through superclassIds adding them as superclasses
		for(int i = 0; i < superClassIds.length(); i++){
			try{
				String superclassIdString = superClassIds.getString(i);
				if(!superclassIdString.equals(idCharacter+"Thing")){
					jenaClass.addSuperClass(ont.getClass(superclassIdString).getJenaClass());
				}	
			}catch(JSONException e){
				throw new RuntimeException("Error Extracting SuperClass Id from List of Ids");
			}
		}
	}
	
	private Ontology ont;
	private OntClass jenaClass;
	
	/**
	 * Creates a new Ontology Class Object, saving the necessary data for future manipulation/querying of the class.
	 * There is no guarantee that the class already exists in the ontology, this could be creating a new class right now 
	 * @param o - Ontology that the class belongs to
	 * @param identifier - IRI of the Class that will be returned
	 */
	public OntologyClass(Ontology o, IRI identifier){
		// TODO Check that class exists already somehow? (referencing axioms trick?)
		
		ont = o;
		//_class = ont.getOWLDataFactory().getOWLClass(identifier);
	}
	
	public OntologyClass(Ontology o, OntClass cls){
		ont = o;
		if(cls == null){
			throw new RuntimeException("Jena Class cannot be null");
		}
		jenaClass = cls;
	}
	
	public OntologyClass(Ontology o, OWLClass cls, OntClass jenaCls){
		this(o, jenaCls);
	}
	
	/**
	 * NOT IMPLEMENTED YET
	 * @return
	 */
	public JSONObject update(JSONObject newVals){
		JSONObject oldValues = this.getJSONRepresentation();
		oldValues.remove("instanceId");
		JSONObject currentValues = new JSONObject();

		Iterator<String> k = oldValues.keys();
		
		// TODO: look for empty values (to be deleted props)
		while(k.hasNext()){
			String key = k.next();
			try {
				currentValues.put(key, oldValues.get(key));
			} catch (JSONException e) {
				throw new RuntimeException("error copying current values to old values");
			}
		}
		
		JSONArray superclassIds = new JSONArray();
		
		jenaClass.removeProperties();
		jenaClass.addSuperClass(OWL.Thing);
		
		try{
			if(newVals.has("subclassOf")){
				
				String superclassIdString = newVals.getString("subclassOf");
				if(superclassIdString.startsWith("[") && superclassIdString.endsWith("]")){
					superclassIds = newVals.getJSONArray("subclassOf");
				}else{
					superclassIds.put(superclassIdString);
				}
				
				getSubclassAxioms(ont, jenaClass, superclassIds);
			}else{
				for(OntologyClass cls : getSuperClasses()){
					superclassIds.put(cls.getId());
				}
			}
			
			if(newVals.has("requirements")){
				getRestrictionAxioms(ont, jenaClass, superclassIds, newVals.getJSONObject("requirements"), true);
			}
			
			if(newVals.has("restrictions")){
				getRestrictionAxioms(ont, jenaClass, superclassIds, newVals.getJSONObject("restrictions"), false);
			}
		}catch(JSONException e){
			throw new RuntimeException("Error getting values from newvalue object:"+newVals);
		}
		
		return this.getJSONRepresentation();
	}
	
	/**
	 * 
	 */
	public void delete(){
		jenaClass.remove();
		
	}

	/**
	 * Returns all of the Ontology Instances of this class in a map
	 * @return a Map of String (InstanceId) to the OntologyInstance it identifies
	 */
	public Map<String, OntologyInstance> getAllInstances(){
		return getAllInstances(true);
	}
	
	public Map<String, OntologyInstance> getAllInstances(boolean local){
		Map<String, OntologyInstance> instances = new HashMap<String, OntologyInstance>();
		
		String classId = getIdentifierString(jenaClass.getURI());
		
		OntologyClass cls = ont.getClass(classId);

		// TODO: Make sure this finds inferred instances
		for(OntResource ind : jenaClass.listInstances().toSet()){
			if(ind.isIndividual() && (!local || ind.getURI().startsWith(ont.getBaseIRI()))){
				OntologyInstance instance = new OntologyInstance(ind.asIndividual(), ont);
				instances.put(instance.getId(), instance);
			}
		}
			
		return instances;
	}


	/**
	 * Returns a JSONObject that contains the information that represents this class of the form:
	 * {
	 * 		classId: <classId>,
	 * 		requirements: {
	 * 			(idChar)<propId>: [(idChar)<valueType>, ...]
	 * 		},
	 * 		restrictions: {
	 * 			(idChar)<propId>: [(idChar)<valueType>, ...]
	 * 		}
	 * }
	 * Where propId is an (idChar)-formatted Id of a property
	 */
	public JSONObject getJSONRepresentation(){
		JSONObject obj = new JSONObject();
		
		JSONObject req = new JSONObject(), rest = new JSONObject();
		
		// TODO: NEED TO USE REASONER TO GET PARENT CLASS PROPERTY RESTRICTIONS
		
		// Class's Necessary and Sufficient conditions are stored in superclass assertions
		
		List<OntClass> supers = jenaClass.listSuperClasses().toList();
		
		for(int index = 0; index < supers.size(); index++){
			OntClass cls = supers.get(index);
			
			if(cls.isRestriction()){
				Restriction r = cls.asRestriction();
				String valueId = "";
				
				OntProperty prop = r.getOnProperty();
				Resource restrictionValue;
				 
				if(r.isAllValuesFromRestriction()){
					restrictionValue = r.asAllValuesFromRestriction().getAllValuesFrom();
				}else if(r.isSomeValuesFromRestriction()){
					restrictionValue = r.asSomeValuesFromRestriction().getSomeValuesFrom();
				}else{
					// TODO: Implement Cardinality Restrictions
					throw new RuntimeException("Unexpected Restriction Type");
				}
				
				
				if(!prop.isObjectProperty() && prop.isDatatypeProperty()){
					// TODO: More DataTypes
					if(restrictionValue.equals(XSD.xint) || restrictionValue.equals(XSD.integer)){
						valueId = "xsd:integer";
					}else if(restrictionValue.equals(XSD.xstring)){
						valueId = "xsd:string";
					}else if(restrictionValue.equals(XSD.xdouble)){
						valueId = "xsd:double";
					}else if(restrictionValue.equals(XSD.dateTime)){
						valueId = "xsd:dateTime";
					}else{
						throw new RuntimeException("Unrecognized DataType: "+restrictionValue.toString());
					}
				}else{
					valueId = getIdentifier(restrictionValue.getURI());
				}
				
				try{
					if(r.isAllValuesFromRestriction()){
						JSONArray restriction = rest.optJSONArray(getIdentifier(prop.getURI()));
						if(restriction == null || !restriction.toString().contains(valueId)){
							rest.append(getIdentifier(prop.getURI()), valueId);
						}
					}else if(r.isSomeValuesFromRestriction()){
						JSONArray requirement = req.optJSONArray(getIdentifier(prop.getURI()));
						// TODO: HACK (For some reason during update I am getting duplicate of the some value restrictions)
						if(requirement == null || !requirement.toString().contains(valueId)){
							req.append(getIdentifier(prop.getURI()), valueId);
						}
					}else{
						// TODO: Implement Cardinality Restrictions
						throw new RuntimeException("Unexpected Restriction Type");
					}
				}catch(JSONException e){
					throw new RuntimeException("Error Creating JSON Object for Property " + prop);
				}
			}
		}

		JSONObject subclasses = new JSONObject();
		for(OntologyClass cls : getSubClasses()){
			try{
				subclasses.put(cls.getId(), cls.getJSONRepresentation());
			}catch(JSONException e){
				throw new RuntimeException("Error getting subclasses of: "+getId());
			}
		}
		
		JSONArray superclasses = new JSONArray();
		for(OntologyClass cls : getSuperClasses()){
			superclasses.put(cls.getId());
		}
		
		JSONArray disjointClasses = new JSONArray();
		for(OntClass cls : jenaClass.listDisjointWith().toSet()){
			disjointClasses.put(new OntologyClass(ont, null, cls).getId());
		}
		
		try{
			obj.put("subclasses", subclasses);
			obj.put("superclasses", superclasses);
			obj.put("requirements", req);
			obj.put("restrictions", rest);
			obj.put("disjoints", disjointClasses);
		}catch(JSONException e){
			throw new RuntimeException("Error adding class details to JSONObject for class "+ getIdentifierString(jenaClass.getURI()));
		}
		
		return obj;
	}
	
	
	public Set<OntologyProperty> getPropertiesWithDomain(boolean direct){
		Set<OntologyProperty> props = new HashSet<OntologyProperty>();
		
		for(OntProperty prop : jenaClass.listDeclaredProperties(direct).toSet()){
			props.add(new OntologyProperty(ont, prop));
		}
		
		return props;
	}
	
	public Set<OntologyProperty> getPropertiesWithRange(){
		
		throw new RuntimeException("Not Implemented Yet");
	}
	
	
	public Set<OntologyClass> getSuperClasses(){
		// TODO: Needs to Include @Thing, all object properties require 
		Set<OntologyClass> superclassObjects = new HashSet<OntologyClass>();
		
		for(OntClass cls : jenaClass.listSuperClasses().toSet()){
			if(!cls.isRestriction() && !cls.isAnon()){
				superclassObjects.add(new OntologyClass(ont, cls));
			}
		}
		
		return superclassObjects;
	}
	
	public Set<OntologyClass> getSubClasses(){
		Set<OntologyClass> subclassObjects = new HashSet<OntologyClass>();
		
		for(OntClass cls : jenaClass.listSubClasses().toSet()){
			if(!cls.equals(OWL2.Nothing) && !cls.isAnon()){
				subclassObjects.add(new OntologyClass(ont, cls));
			}
		}
		
		return subclassObjects;
	}
	
	/**
	 * Returns the Ontology that this class is a part of
	 * @return Ontology object that this class is described in
	 */
	public Ontology getOntology(){
		return ont;
	}

	
	public OntClass getJenaClass(){
		return jenaClass;
	}
	
	@Override
	public String getId() {
		return getIdentifier(getFullId());
	}

	@Override
	public String getFullId() {
		return jenaClass.getURI();//.toStringID();
	}
}

package com.eduworks.ontology;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticweb.owlapi.model.IRI;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

public class OntologyInstance extends OntologyWrapper {
	
	/**
	 * Creates a new Instance of the Class that is passed in with the values of properties being those that are passed in
	 * @param cls - Class that we want to create a new instance of
	 * @param values - Values of the new Instance Properties
	 * @return OntologyInstance of the newly created instance
	 * @throws JSONException
	 */
	public static OntologyInstance createInstance(OntologyClass cls, JSONObject values){
		Ontology ont = cls.getOntology();
		JSONObject classTemplate = cls.getJSONRepresentation();
		
		
		// Get all of the superclasses, so we can use for property domain coherency check
		Map<String, OntologyClass> assertedClasses = new HashMap<String, OntologyClass>();
		for(OntClass expr : cls.getJenaClass().listSuperClasses().toSet()){
			if(!expr.isAnon()){
				OntologyClass c = new OntologyClass(ont, expr);
				assertedClasses.put(c.getId(), c);
			}
		}
		assertedClasses.put(cls.getId(), cls);
		
		// Check the Instance Values against class restrictions/requirements and any other property specific restrictions (domain/range)
		JSONObject requiredVals = checkClassRestrictions(ont, values, classTemplate.optJSONObject("requirements"), true);
		JSONObject restrictedVals = checkClassRestrictions(ont, values, classTemplate.optJSONObject("restrictions"), false);
		JSONObject unrestrictedVals = checkUnrestrictedProperties(ont, values, assertedClasses);
				
		// Prepare to Create the New Instance
		String ontPrefix = ont.getPrefix() ;
	
		UUID id;
		Individual jenaInd;
		boolean unique = false;
		
		// Make sure it's unique
		do{
			id = UUID.randomUUID();
			IRI iri = IRI.create("#" + id);
			
			// TODO: NOT ALWAYS THIS MAYBE
			String stringIri = ont.getBaseIRI() + "#" + id;

			// Test whether individual with this Id already exists (creating an instance with an Iri that exists doesn't cause an error)
			jenaInd = ont.getJenaModel().getIndividual(stringIri);
			if(jenaInd == null){
				jenaInd = ont.getJenaModel().createIndividual(stringIri, cls.getJenaClass());	
			}else{
				// if already exists, restart ID generation
				continue;
			}
			
			unique = true;
		}while(!unique);
		
		
		// Add all Properties
		addInstanceProperties(ont, jenaInd, requiredVals);
		
		addInstanceProperties(ont, jenaInd, restrictedVals);
		
		addInstanceProperties(ont, jenaInd, unrestrictedVals);
		
		
		// Add Class Assertion Axiom
		jenaInd.addOntClass(cls.getJenaClass());
		jenaInd.addRDFType(RDFS.Resource);
		
		return new OntologyInstance(jenaInd, ont);
	}
	
	private static void addInstanceProperties(Ontology ont, Individual jenaInd, JSONObject properties){
		Iterator<String> keys = properties.keys();
		while(keys.hasNext()){
			String key = keys.next();
				
			JSONArray vals = properties.optJSONArray(key);
			for(int i = 0; i < vals.length(); i++){
				String val = vals.optString(i);
				
				if(val.startsWith(idCharacter)){
					// Object Property
					OntologyProperty p = ont.getProperty(key);
					
					OntologyInstance pVal = ont.getInstance(val);
					
					jenaInd.addProperty(p.getJenaProperty(), pVal.getJenaIndividual());
					
				}else{
					// Data Property
					OntologyProperty p = ont.getProperty(key);
					
					try{
						String datatype = p.getRange().getString(0);
						
						// TODO: ADD ALL DATATYPES
						// TODO: Check that value is of correct type
						if(datatype.equals("xsd:dateTime")){ 
							jenaInd.addProperty(p.getJenaProperty(), val, XSDDatatype.XSDdateTime);
						}else if(datatype.equals("xsd:double")){
							jenaInd.addProperty(p.getJenaProperty(), val, XSDDatatype.XSDdouble);
						}else if(datatype.equals("xsd:integer")){
							jenaInd.addProperty(p.getJenaProperty(), val, XSDDatatype.XSDinteger);
						}else{
							jenaInd.addProperty(p.getJenaProperty(), val);
						}
						
						
					}catch(JSONException e){
						jenaInd.addProperty(p.getJenaProperty(), val);
					}
					
				}
			}
			
		}
	}
	

	private static JSONObject checkUnrestrictedProperties(Ontology ont, JSONObject newValues, Map<String, OntologyClass> assertedClasses){
		JSONObject properties = new JSONObject();
		
		// Check that remaining values are for properties that exist in ontology and have the correct domain (and range?)
		Iterator<String> k = newValues.keys();
		while(k.hasNext()){
			String propId = k.next();
			
			JSONArray domains = ont.getProperty(propId).getDomain();
			
			try{
				// TODO: Test this better
				if(domains.length() == 0 || assertedClasses.size() == 0){
					 // No domain specified, or no classes asserted for the instance so this property will be fine
					properties.put(propId, checkPropertyValuesAgainstTypes(ont, propId, newValues.get(propId), null, false, false));
				
				}else if(domains.length() == 1){ 
					
					if(assertedClasses.containsKey(domains.get(0))){
						properties.put(propId, checkPropertyValuesAgainstTypes(ont, propId, newValues.get(propId), null, false, false));
					}else{
						throw new RuntimeException("Domain of property <"+propId+"> ("+domains+") does not match asserted classes: "+assertedClasses.keySet());
					}
				}else{
					
					for(int i = 0; i < domains.length(); i++){
						if(!assertedClasses.containsKey(domains.get(i))){
							throw new RuntimeException("Domain intersection of property <"+propId+"> ("+domains+") does not match asserted classes: "+assertedClasses.keySet());
						}
					}
					
					properties.put(propId,  checkPropertyValuesAgainstTypes(ont, propId, newValues.get(propId), null, false, false));
				}
			}catch(JSONException e){
				throw new RuntimeException("this shouldn't be happening");
			}
		}
		
		return properties;
	}
	
	private static JSONObject checkClassRestrictions(Ontology ont, JSONObject newValues, JSONObject restrictions, boolean required){
		JSONObject requiredVals = new JSONObject();
		JSONObject newVals = new JSONObject();
		
		try 
		{
			if(newValues.length() > 0)
			{
				newVals = new JSONObject(newValues, JSONObject.getNames(newValues));
			}
			else
			{
				newVals = new JSONObject();
			}
		} 
		catch (JSONException e1) 
		{
			e1.printStackTrace();
		}
		
		if(restrictions != null){
			Iterator<String> k = restrictions.keys();
			
			// Iterate through required properties
			while(k.hasNext()){
				String propertyId = k.next();
				
				try{	
					// Make sure that Instance Values has the required property and it is not an empty value
					if(newVals.has(propertyId) &&  
									(newVals.optJSONArray(propertyId) != null ||
									(newVals.optString(propertyId) != null && !newVals.getString(propertyId).isEmpty()))
						){
							
							// Get the instance value for the key
							Object value = newVals.get(propertyId);
							
							// Get the Required Type(s) of the property
							JSONArray valTypes = restrictions.getJSONArray(propertyId);
							
							// Check that the Value matches the required Types
							value = checkPropertyValuesAgainstTypes(ont, propertyId, value, valTypes, required, false);
							
							requiredVals.put(propertyId, value);
							newVals.remove(propertyId);
					}else if(required){
						throw new RuntimeException("Missing Required Property: " + propertyId);
					}
				}catch(JSONException e){
					throw new RuntimeException("This shouldn't happen: (on "+ propertyId+")", e);
				}
			}
		}
		
		return requiredVals;
	}
	
	private static JSONArray checkPropertyValuesAgainstTypes(Ontology ont, String propId, Object values, JSONArray types, boolean required, boolean restricted){
		JSONArray checkedValues = new JSONArray();
		
		if(values instanceof JSONArray){
			if(((JSONArray)values).length() == 0 && required){
				throw new RuntimeException("Missing Required Property: " + propId);
			}
			
			for(int i = 0; i < ((JSONArray)values).length(); i++){
				
				// TODO: Might want to catch exception here if we're looking at requirements (not restrictions, restrictions 
				// (could require a friend who's of type A, but restrict friends to type A and type B,
				// 		so type B is okay as long as we have a friend of type A as well)
				try{
					String value = ((JSONArray)values).getString(i);
							
					checkedValues.put(checkPropertyValueAgainstTypes(ont, propId, value, types));
				}catch(JSONException e){
					throw new RuntimeException("Error pulling value out of value list");
				}
				
			}
		}else{
			checkedValues.put(checkPropertyValueAgainstTypes(ont, propId, values.toString(), types));
		}
		
		return checkedValues;
	}
	
	private static String checkPropertyValueAgainstTypes(Ontology ont, String propertyId, String value, JSONArray types){
		// Iterate through restricted types, ensuring that the value is in the intersection of all of the restricted types
		if(types != null && types.length() > 0){
			String type = "";
			try {
				type = types.getString(0);
			} catch (JSONException e) {
				throw new RuntimeException("Error getting first type from type list");
			}
			
			if(type.startsWith("xsd:")){
				return checkDataPropertyValue(propertyId, value, types);
			}else if(type.startsWith(idCharacter)){
				return checkObjectPropertyValue(ont, propertyId, value, types);
			}else{
				throw new RuntimeException("Unexpected value type of <"+ type +"> for property: " + propertyId);
			}
		}
		
		return value;
		
	}
	
	private static String checkDataPropertyValue(String propertyId, String value, JSONArray types){
		if(types.length() == 1){
			return value;
		}else{
			throw new RuntimeException("Data Property <"+propertyId+"> has more than one DataType");
		}
	}
	
	private static String checkObjectPropertyValue(Ontology ont, String propertyId, String value, JSONArray types){
		if(!value.startsWith(idCharacter)){
			throw new RuntimeException("Expected "+idCharacter+"-formatted identifier for property <" + propertyId +">, instead received '" + value +"'");
		}
		
		for(int i = 0; i < types.length(); i++){
			String type;
			
			try {
				type = types.getString(i);
			} catch (JSONException e) {
				throw new RuntimeException("Error retrieving class from restriction list");
			}
			
			OntologyClass c = ont.getClass(type.substring(1));
			
			// Get the instances of the class type requirement
			Map<String, OntologyInstance> instances = c.getAllInstances(false);
			
			// Make sure the ID points to an existing instance
			if(!instances.containsKey(value)){
				throw new RuntimeException("Identifier '"+value+"' does not represent instance of class <"+type+">");
			}
			
			// Make sure all ClassType Restrictions have been met before adding to instance values
			if(i == types.length() - 1){
				return value;
			}
		}
		
		return value;
	}
	
	
	/* Instance Properties */
	
	private Ontology ont;
	private Individual jenaInstance;
	
	private Map<String, OntologyClass> assertedClasses;
	
	/* Instance Methods */
	
	/**
	 * (DANGER) Creates a new OntologyInstance, wrapping the OWLAPI object passed in, assumes ontology passed in is the ontology in
	 * which the instance is within
	 * @param instance - OWLAPI Individual to be wrapped 
	 * @param o - Ontology that this Individual is a part of
	 */
	public OntologyInstance(Individual jInstance, Ontology o){
		if(jInstance == null){
			throw new RuntimeException("Jena Instance cannot be null");
		}
		ont = o;
		
		jenaInstance = jInstance;
		
		assertedClasses = new HashMap<String, OntologyClass>();
		
		for(OntClass cls : jenaInstance.listOntClasses(false).toSet()){
			if(!cls.isAnon()){
				OntologyClass c = new OntologyClass(ont, cls);
				assertedClasses.put(c.getId(), c);
			}
		}
		
	}
	
	/**
	 * Creates a new OntologyInstance by looking through ontology passed in and finding the Instance that is referenced by the IRI passed in.
	 * There is no guarantee that the instance exists, this could be creating the instance
	 * @param o - Ontology to look through for the instance specified
	 * @param iri - IRI of the instance we want to wrap
	 */
//	public OntologyInstance(Ontology o, IRI iri){
//		// TODO: Ensure that the instance exists somehow
//		ont = o;
//		
//		assertedClasses = new HashMap<String, OntologyClass>();
//		
//		for(OWLClassExpression expr : _instance.getTypes(ont.getOWLOntology())){
//			if(expr.isClassExpressionLiteral()){
//				OntologyClass cls = new OntologyClass(ont, expr.asOWLClass().getIRI());
//				assertedClasses.put(cls.getId(), cls);	
//			}
//		}
//		
//		
//	}
	
	/**
	 * Updates the OntologyInstance with the values passed in
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void update(JSONObject values){
		JSONObject infValues = this.getInferredProperties();
		
		values.remove("uri");
		
		Iterator<String> k = infValues.keys();
		while(k.hasNext()){
			String key = k.next();
			try{
				JSONArray actualNewVals = new JSONArray();
				JSONArray oldInfVals = new JSONArray();
				
				if(values.has(key)){
					EwJsonArray infProps = new EwJsonArray(infValues.getJSONArray(key));
					EwJsonArray newProps = new EwJsonArray(values.getJSONArray(key));
					
					// Cycle through new Props, only adding ones that aren't in the inferred model
					for(int i = 0; i < newProps.length(); i++){
						String val = newProps.getString(i);
						if(!infProps.contains(val)){
							actualNewVals.put(val);
						}
					}
					
					// Cycle through old ones, finding those to be deleted
					for(int i = 0; i < infProps.length(); i++){
						String val = infProps.getString(i);
						if(!newProps.contains(val)){
							oldInfVals.put(val);
						}
					}
					
					values.put(key, actualNewVals);
					infValues.put(key, oldInfVals);
				}
			}catch(JSONException e){
				throw new RuntimeException("Error updating instance: " + this.getId()+ "["+e.getMessage()+"]");
			}
		}
		
		
		JSONObject oldValues = this.getJSONRepresentation(true);
		JSONObject currentValues = new JSONObject();
		 
		oldValues.remove("uri");
		
		k = oldValues.keys();
		while(k.hasNext()){
			String key = k.next();
			try {
				currentValues.put(key, oldValues.get(key));
			} catch (JSONException e) {
				throw new RuntimeException("error copying current values to old values");
			}
		}
		
		
		JSONObject requiredVals = new JSONObject();
		JSONObject restrictedVals = new JSONObject();
		try{
			// TODO: look for empty values (to be deleted props)
			k = values.keys();
			while(k.hasNext()){
				String key = k.next();
				currentValues.put(key, values.get(key));
			}
			
			k = oldValues.keys();
			while(k.hasNext()){
				String key = k.next();
				if(values.optJSONArray(key) == null ){
					currentValues.remove(key);
				}
			}
			
			
			for(String key : assertedClasses.keySet()){
				if(!key.equals(":Thing") && !key.equals(":Resource")){
					JSONObject temp = checkClassRestrictions(ont, currentValues, assertedClasses.get(key).getJSONRepresentation().getJSONObject("requirements"), true);
					
					k = temp.keys();
					while(k.hasNext()){
						key = k.next();
						
						requiredVals.put(key, temp.get(key));
					}
				}
			}
			
			
			for(String key : assertedClasses.keySet()){
				if(!key.equals(":Thing")  && !key.equals(":Resource")){
					JSONObject temp = checkClassRestrictions(ont, currentValues, assertedClasses.get(key).getJSONRepresentation().getJSONObject("restrictions"), false);
					
					k = temp.keys();
					while(k.hasNext()){
						key = k.next();
						
						restrictedVals.put(key, temp.get(key));
					}
				}
			}
		}catch(JSONException e){
			throw new RuntimeException("Error Checking Updated Values for instance<"+getId()+">");
		}
		
		JSONObject unrestrictedVals = checkUnrestrictedProperties(ont, currentValues, assertedClasses);

		updateProperties(ont, jenaInstance, oldValues, requiredVals, infValues);
		updateProperties(ont, jenaInstance, oldValues, restrictedVals, infValues);
		updateProperties(ont,  jenaInstance, oldValues, unrestrictedVals, infValues);
		
		
	}
	
	private void updateProperties(Ontology ont, Individual jenaInd, JSONObject oldProps, JSONObject newProps, JSONObject inferredProps){
		Iterator<String> k = oldProps.keys();
		
		while(k.hasNext()){
			String key = k.next();
			
			OntologyProperty changedProp = ont.getProperty(key);
				
				if(jenaInd.hasProperty(changedProp.getJenaProperty())){
					jenaInd.removeAll(changedProp.getJenaProperty());
				}
				
				if(inferredProps.has(key)){
					try{

						JSONArray vals = inferredProps.getJSONArray(key);
						
						OntProperty invProp = changedProp.getJenaProperty().getInverse();
						
						if(changedProp.getJenaProperty().getURI().equals(OWL.sameAs.getURI())){
							invProp = ont.getJenaModel().getOntProperty(OWL.sameAs.getURI());
						}
						
						if(invProp != null){
							String invPropKey = getIdentifier(invProp.getURI());
							
							for(int i = 0; i < vals.length(); i++){
								OntologyInstance related = ont.getInstance(vals.getString(i));
								
								if(related.getJSONRepresentation(true).has(invPropKey)){
									related.getJenaIndividual().removeProperty(invProp, jenaInd);
								}
							}
						}
						inferredProps.remove(key);
					}catch(JSONException e){
						
					}
				}	
		}
		
		k = inferredProps.keys();
		while(k.hasNext()){
			String key = k.next();
			
			try{
				JSONArray vals = inferredProps.getJSONArray(key);
				
				OntologyProperty changedProp = ont.getProperty(key);
				OntProperty invProp = changedProp.getJenaProperty().getInverse();
				
				if(invProp != null){
					String invPropKey = getIdentifier(invProp.getURI());
					
					for(int i = 0; i < vals.length(); i++){
						OntologyInstance related = ont.getInstance(vals.getString(i));
						
						if(related.getJSONRepresentation(true).has(invPropKey)){
							related.getJenaIndividual().removeProperty(invProp, jenaInd);
						}
					}
				}
			}catch(JSONException e){
				
			}
		}
		
		addInstanceProperties(ont, jenaInd, newProps);
		
	}

	
	/**
	 * Delete's the instance from it's ontology
	 */
	public void delete(){
		jenaInstance.removeProperties();
		jenaInstance.remove();
		
	}

	/**
	 * Returns a JSONObject that represents this instance in the form:
	 * {
	 * 		instanceId: <instanceId>,
	 * 		@<propId>: [<valueType>, ...],
	 * }
	 */
	public JSONObject getJSONRepresentation(){
		return getJSONRepresentation(false);
	}
	public JSONObject getJSONRepresentation(boolean local) {
		JSONObject instanceObj = new JSONObject();

		// TODO: Change to Reasoner!
		// Get Object Properties and Values associated with and add to instance Object
		
		OntModel explicitModel = null;
		if(local){
			explicitModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, ont.getJenaModel().getBaseModel());
		}
		
		Set<String> objectPropIdList = ont.getObjectPropertyIdList();
		Set<String> dataPropIdList = ont.getDataPropertyIdList();
		
		for(Statement stmt : jenaInstance.listProperties().toSet()){
			if(!local || explicitModel.contains(stmt)){
				
			
				Property p = stmt.getPredicate();
				
				if(p.getURI() != null && !p.equals(OWL.differentFrom)){
					String propId = getIdentifier(p.getURI());
					
					try{
						if(objectPropIdList.contains(propId)){
							Resource val = stmt.getResource();
							if(val != null){
								instanceObj.append(propId, getIdentifier(val.getURI()));
							}
						}else if(dataPropIdList.contains(propId)){
							Literal val = stmt.getLiteral();
							if(val != null){
								instanceObj.append(propId, val.getLexicalForm());
							}
						}
					}catch(JSONException e){
						throw new RuntimeException("Error Adding Property Values to JSON Representation");
					}
				}
			}
		}
		
		try{
			instanceObj.put("uri", jenaInstance.getURI());
		}catch(JSONException e){
			throw new RuntimeException("Error Adding URI to JSON Representation");
		}
		
		
		// TODO: Annotations?
		
					
		return instanceObj;
	}
	
	public JSONObject getInferredProperties() {
		JSONObject instanceObj = new JSONObject();

		OntModel explicitModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, ont.getJenaModel().getBaseModel());
		// TODO: Change to Reasoner!
		// Get Object Properties and Values associated with and add to instance Object
		
		for(Statement stmt : jenaInstance.listProperties().toSet()){
			if(!explicitModel.contains(stmt)){
				Property p = stmt.getPredicate();
				
				if(p.getURI() != null && !p.equals(OWL.differentFrom)){
					String propId = getIdentifier(p.getURI());
					
					try{
						if(ont.getObjectPropertyIdList().contains(propId)){
							Resource val = stmt.getResource();
							if(val != null){
								instanceObj.append(propId, getIdentifier(val.getURI()));
							}
						}else if(ont.getDataPropertyIdList().contains(propId)){
							Literal val = stmt.getLiteral();
							if(val != null){
								instanceObj.append(propId, val.getLexicalForm());
							}
						}
					}catch(JSONException e){
						throw new RuntimeException("Error Adding Property Values to JSON Representation");
					}
				}
			}
		}
		
		// TODO: Annotations?
		
					
		return instanceObj;
	}
	
	public Individual getJenaIndividual(){
		return jenaInstance;
	}
	
	@Override
	public String getId() {
		return getIdentifier(getFullId());
	}

	@Override
	public String getFullId() {
		return jenaInstance.getURI();
	}
	
}

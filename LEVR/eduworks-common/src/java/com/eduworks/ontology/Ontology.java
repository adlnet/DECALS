package com.eduworks.ontology;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntTools;
import com.hp.hpl.jena.ontology.OntTools.Path;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.TDBLoader;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

//Wrapper for JENA.
public class Ontology extends OntologyWrapper
{

	public static boolean debug = false;
	
	public static final String extension = ".owl";

	private static String defaultURI = "http://www.eduworks.com/";

	private static OntModelSpec reasonerSpec = OntModelSpec.OWL_MEM_MICRO_RULE_INF;

	// JENA Manager Things
	private static OntDocumentManager jenaManager = OntDocumentManager.getInstance();


	public static Object lock = new Object();

	public static List<String> listModelIdentifiers(Dataset tdbDataSet)
	{
		List<String> nameList = new EwList<String>();

		Iterator<String> names = tdbDataSet.listNames();

		if (names != null)
		{
			while (names.hasNext())
			{
				String s = names.next();
				if (s != null)
				{
					nameList.add(s);
				}
			}
		}

		return nameList;
	}

	public static void importToTDB(Dataset tdbDataSet,String inputPath, String identifier)
	{
		Ontology o = Ontology.createOntology(tdbDataSet,identifier);

		TDBLoader.loadModel(o.jenaModel, inputPath);
	}

	public static void exportFromTDB(Dataset tdbDataSet,String outputPath, String identifier, String extension)
	{
		Ontology o = Ontology.loadOntology(tdbDataSet,identifier);

		String fullOutputLocation = outputPath + (outputPath.endsWith(File.separator) ? "" : File.separator) + identifier + extension;

		try
		{
			FileWriter writer = new FileWriter(fullOutputLocation);
			o.getJenaModel().write(writer);
			writer.close();
			o.close(true);
		}
		catch (IOException e)
		{
			o.close(true);
			throw new RuntimeException("Failed to Export Ontology to Path: " + fullOutputLocation);
		}
	}

	public static synchronized Dataset setTDBLocation(String directory)
	{
		return setTDBLocation(directory, false);
	}

	public static synchronized Dataset setTDBLocation(String directory, boolean hard)
	{
		Dataset tdbDataSet = null;

//		TDBFactory.reset();
		
		tdbDataSet = TDBFactory.createDataset(directory);

		// STILL HACKY, BUT PREVENTS THE ALREADY CLOSED EXCEPTION IF WE ARE
		// RESETTING THE TDB LOCATION
//		if (hard)
//		{
//			tdbDataSet.begin(ReadWrite.WRITE);
//			tdbDataSet.commit();
//			tdbDataSet.end();
//		}
		return tdbDataSet;
	}

	public static void setDefaultURI(String uri)
	{
		defaultURI = uri;
	}

	public static String getDefaultURI()
	{
		return defaultURI;
	}

	// TODO: Need to have method to load ontology from IRI (and eventually load
	// from an online owl-ontology to disk to be used)
	/**
	 * Loads an existing ontology from the local directory and wraps it in an
	 * Ontology object to be manipulated
	 * 
	 * @param identifier
	 *            - Identifier of the ontology (right now this is the filename
	 *            without the directory or extension)
	 * 
	 * @return Ontology Object wrapping the OWLOntology that was loaded
	 */
	public static Ontology loadOntology(Dataset tdbDataSet,String identifier)
	{
		long dt = System.currentTimeMillis();
		if (identifier == null)
		{
			throw new NullPointerException();
		}
		else if (identifier.isEmpty())
		{
			throw new RuntimeException("Ontology Identifier cannot be empty");
		}

		// TODO: Probably want to use a real URL Validator here
		if(!identifier.startsWith("http")){
			if (defaultURI.endsWith("/"))
			{
				identifier = defaultURI + identifier;
			}
			else
			{
				identifier = defaultURI + "/" + identifier;
			}
		}

		Ontology o = null;

//		o = (Ontology) EwCache.getCache("OntologyCache", 20).get(identifier);
//		if (o != null && o.jenaModel != null && o.jenaModel.isClosed())
//			o = null;
//		if (o != null)
//		{
//			return o;
//		}

		if (!tdbDataSet.containsNamedModel(identifier))
		{
			throw new RuntimeException("Model Doesn't Exist with Identifier: " + identifier);
		}
		
		Model base = tdbDataSet.getNamedModel(identifier);

		OntModelSpec tdbSpec = new OntModelSpec(reasonerSpec);
		tdbSpec.setImportModelGetter(new OntologyTDBModelGetter(tdbDataSet));
		
		OntModel _o = ModelFactory.createOntologyModel(tdbSpec, base);

		o = new Ontology(_o, identifier,tdbDataSet);

//		EwCache.getCache("OntologyCache", 20).put(identifier, o);

		if (debug) System.out.println("Load Ontology took: " + (System.currentTimeMillis()-dt) + " millis.");
		
		return o;
	}

	/**
	 * Creates a new ontology file in the local directory and then loads and
	 * wraps it in an Ontology object
	 * 
	 * @param identifier
	 *            - The identifier of the new ontology (right now, just the name
	 *            of the file without the path or extension)
	 * 
	 * @return Ontology Object wrapping the newly created JenaOntology
	 */
	public static Ontology createOntology(Dataset tdbDataSet,String identifier)
	{
		long dt = System.currentTimeMillis();
		if (identifier.isEmpty())
		{
			throw new RuntimeException("Ontology Identifier cannot be empty");
		}

		if (defaultURI.endsWith("/"))
		{
			identifier = defaultURI + identifier;
		}
		else
		{
			identifier = defaultURI + "/" + identifier;
		}

		if (tdbDataSet.containsNamedModel(identifier))
		{
			throw new RuntimeException("Ontology already exists with that identifier");
		}

		OntModelSpec tdbSpec = new OntModelSpec(reasonerSpec);
		tdbSpec.setImportModelGetter(new OntologyTDBModelGetter(tdbDataSet));
		
		OntModel o = ModelFactory.createOntologyModel(tdbSpec);

		tdbDataSet.addNamedModel(identifier, o);

		o = ModelFactory.createOntologyModel(tdbSpec, tdbDataSet.getNamedModel(identifier));
		com.hp.hpl.jena.ontology.Ontology ont = o.createOntology(identifier);

		if (debug) System.out.println("Create Ontology took: " + (System.currentTimeMillis()-dt) + " millis.");
		return new Ontology(o, identifier,tdbDataSet);
	}

	/* Ontology Instance Properties */

	private OntModel jenaModel;
	private Dataset dataSet;

	private String baseIRI;
	private String identifier;

	private String tdbDir;

	/* Instance Methods */

	/**
	 * Constructor for creating/loading a new ontology, wraps the ontology given
	 * and saves the
	 * 
	 * @param o
	 *            - OntModel to be wrapped
	 * @param dataSet 
	 */
	private Ontology(OntModel o, String identifier, Dataset dataSet)
	{
		this.jenaModel = o;
		this.identifier = identifier;
		this.baseIRI = identifier;
		this.dataSet = dataSet;
	}

	/**
	 * Saves the underlying ontology after modifications to the ontology that
	 * need to be propogated for queries
	 */
	public void save(Dataset tdbDataSet)
	{
		// TODO: Check Coherency/Consistency with a Reasoner
		TDB.sync(tdbDataSet);
	}

	public void delete(Dataset tdbDataSet)
	{
		tdbDataSet.removeNamedModel(identifier);
	}

	public void addOntology(Dataset tdbDataSet,String identifier)
	{

		if (identifier == null)
		{
			throw new NullPointerException();
		}
		else if (identifier.isEmpty())
		{
			throw new RuntimeException("Ontology Identifier cannot be empty");
		}

		Ontology ont = loadOntology(tdbDataSet,identifier);

		if (!jenaModel.hasLoadedImport(ont.getBaseIRI()))
		{
			jenaModel.addSubModel(ont.getJenaModel());
			jenaModel.add(ResourceFactory.createResource(getBaseIRI()), OWL.imports, ResourceFactory.createResource(ont.getBaseIRI()));
		}

	}

	public Set<String> listImportedOntologies()
	{
		Set<String> ids = new HashSet<String>();

		Set<String> uris = jenaModel.listImportedOntologyURIs();

		for (String uri : uris)
		{
			ids.add(getIdentifier(uri));
		}

		return ids;
	}

	/* Class */

	/**
	 * 
	 * @param id
	 * @param values
	 * @return
	 */
	public OntologyClass createClass(String id, JSONObject values)
	{
		String tempUri = baseIRI + "#" + id;

		if (jenaModel.contains(ResourceFactory.createResource(tempUri), RDF.type, OWL.Class))
		{
			throw new RuntimeException("Class (" + id + ") Already Exists");
		}

		OntologyClass cls = OntologyClass.createClass(this, id, values);

		return cls;
	}

	/**
	 * Returns the OntologyClass object for the classId specified
	 * 
	 * @param classId
	 *            - Id of the class that will be retrieved
	 * @return OntologyClass wrapper for the class specified
	 */
	public OntologyClass getClass(String classId)
	{
		if (classId.startsWith(idCharacter))
		{
			classId = classId.substring(1);
		}

		Set<String> testedIris = new HashSet<String>();

		OntClass jenaClass = null;

		// Check if Class Exists by appending all uri's with identifier and
		// checking statement existence
		String uri = null;
		for (com.hp.hpl.jena.ontology.Ontology o : jenaModel.listOntologies().toSet())
		{
			String tempUri;
			if(o.getURI().endsWith("#")){
				tempUri  = o.getURI() + classId;
			}else{
				tempUri = o.getURI() + "#" + classId;
			}
			

			if (jenaModel.contains(ResourceFactory.createResource(tempUri), RDF.type, OWL.Class))
			{
				uri = tempUri;
				break;
			}
			else
			{
				testedIris.add(tempUri);
			}
		}

		if (uri == null)
		{
			throw new RuntimeException("Could not find IRI for Class Id (" + classId + ") testedIRIs:" + testedIris);
		}

		jenaClass = jenaModel.getOntClass(uri);

		if (jenaClass == null)
		{
			throw new RuntimeException("Class Doesnt Exist with Id: " + classId);
		}

		return new OntologyClass(this, null, jenaClass);
	}

	/**
	 * Returns a map from classId to OntologyClass of all of the classes in this
	 * ontology
	 * 
	 * @return map from classId to OntologyClass of all of the classes in this
	 *         ontology
	 */
	public Map<String, OntologyClass> getAllClasses()
	{
		Map<String, OntologyClass> classes = new HashMap<String, OntologyClass>();

		OntologyClass thing = new OntologyClass(this, jenaModel.getOntClass(OWL2.Thing.getURI()));

		classes.put(thing.getId(), thing);

		return classes;
	}

	public Set<String> getClassIdList()
	{
		Set<String> classes = new HashSet<String>();

		for (OntClass cls : jenaModel.listClasses().toSet())
		{
			if (!cls.isAnon() && !cls.isOntLanguageTerm())
			{
				classes.add(new OntologyClass(this, cls).getId());
			}
		}

		return classes;
	}

	/* Instance */

	/**
	 * Creates a new instance of a class with the property values given
	 * 
	 * @param classId
	 *            - Id of the class that is to be instantiated
	 * @param values
	 *            - values of the properties for the new instance
	 * @return OntologyInstance wrapper for the new instance created
	 * @throws JSONException
	 */
	public OntologyInstance createInstance(String classId, JSONObject values)
	{
		OntologyClass cls = getClass(classId);

		// Add Import if the class is from a different ontology
		if (!cls.getFullId().contains(baseIRI))
		{
			jenaModel.getOntology(baseIRI).addImport(ResourceFactory.createResource(cls.getFullId().substring(0, cls.getFullId().indexOf("#"))));
		}

		OntologyInstance ins = OntologyInstance.createInstance(cls, values);

		return ins;
	}

	/**
	 * Returns the OntologyInstance object for the instanceId specified
	 * 
	 * @param instanceId
	 *            - Id of the instance to be retrieved
	 * @return OntologyInstance wrapper for the instance specified
	 */
	public OntologyInstance getInstance(String instanceId)
	{
		return getInstance(instanceId, false);
	}

	public OntologyInstance getInstance(String instanceId, boolean local)
	{
		long dt = System.currentTimeMillis();
		String iri = null;

		Set<String> testedIris = new HashSet<String>();

		if (instanceId.startsWith("http"))
		{
			iri = instanceId;
			if (!jenaModel.contains(ResourceFactory.createResource(iri), RDF.type, RDFS.Resource))
			{
				throw new RuntimeException("Could Not find Instance with IRI:" + iri);
			}
		}
		else
		{
			String id;

			if (instanceId.startsWith(idCharacter))
			{
				id = instanceId.substring(idCharacter.length());
			}
			else
			{
				id = instanceId;
			}

			for (com.hp.hpl.jena.ontology.Ontology o : jenaModel.listOntologies().toSet())
			{
				long dt2 = System.currentTimeMillis();
				String tempUri = o.getURI() + "#" + id;
				Resource createdResource = ResourceFactory.createResource(tempUri);

				if (jenaModel.contains(createdResource, RDF.type, RDFS.Resource))
				{
					iri = tempUri;
					break;
				}
				else
				{
					testedIris.add(tempUri);
				}
				if (debug) System.out.println("Get Instance (CreateResource) took: " + (System.currentTimeMillis()-dt2) + " millis.");
			}
		}

		if (iri == null)
		{
			throw new RuntimeException("Could not find IRI for Instance Id (" + instanceId + ") testedIRIs:" + testedIris);
		}

		Individual jInstance = jenaModel.getIndividual(iri);

		if (local && !jenaModel.isInBaseModel(jInstance))
		{
			throw new RuntimeException("Instance (id: " + instanceId + ") is not defined in this model (" + this.identifier + ")");
		}

		if (jInstance == null)
		{
			throw new RuntimeException("Individual (id: " + instanceId + ") Doesn't Exist");
		}

		OntologyInstance ontologyInstance = new OntologyInstance(jInstance, this);

		if (debug) System.out.println("Get Instance took: " + (System.currentTimeMillis()-dt) + " millis.");
		return ontologyInstance;
	}

	public boolean propertyExists(String propertyId)
	{
		if (propertyId.startsWith(idCharacter))
		{
			propertyId = propertyId.substring(idCharacter.length());
		}

		for (com.hp.hpl.jena.ontology.Ontology o : jenaModel.listOntologies().toSet())
		{
			String iri = o.getURI() + "#" + propertyId;
			if (jenaModel.contains(ResourceFactory.createResource(iri), RDF.type, OWL.DatatypeProperty)
					|| jenaModel.contains(ResourceFactory.createResource(iri), RDF.type, OWL.ObjectProperty))
			{
				return true;
			}

		}

		return false;
	}

	public boolean classExists(String classId)
	{
		if (classId.startsWith(idCharacter))
		{
			classId = classId.substring(idCharacter.length());
		}

		for (com.hp.hpl.jena.ontology.Ontology o : jenaModel.listOntologies().toSet())
		{
			String iri = o.getURI() + "#" + classId;
			if (jenaModel.contains(ResourceFactory.createResource(iri), RDF.type, OWL.Class))
			{
				return true;
			}
		}

		return false;
	}

	/* Property */

	/**
	 * 
	 * @param propertyId
	 * @return
	 */
	public OntologyProperty createDataProperty(String propertyId, JSONObject values)
	{
		if (propertyExists(propertyId))
		{
			throw new RuntimeException("Property already exists with id (" + propertyId + ")");
		}
		return OntologyProperty.createDataProperty(this, propertyId, values);
	}

	public OntologyProperty createObjectProperty(String propertyId, JSONObject values)
	{
		if (propertyExists(propertyId))
		{
			throw new RuntimeException("Property already exists with id (" + propertyId + ")");
		}
		return OntologyProperty.createObjectProperty(this, propertyId, values);
	}

	/**
	 * Returns the OntologyProperty object for the propertyId specified
	 * 
	 * @param propertyId
	 *            - identifier of the property that we want to retrieve
	 * @return OntologyProperty object wrapping the property specified
	 */
	public OntologyProperty getProperty(String propertyId)
	{
		if (propertyId.startsWith(idCharacter))
		{
			propertyId = propertyId.substring(idCharacter.length());
		}

		Set<String> testedIris = new HashSet<String>();

		Set<String> namespaceSet = new HashSet<String>();
		// Check if Class Exists by appending all uri's with identifier and
		// checking statement existence
		String iri = null;
		for (com.hp.hpl.jena.ontology.Ontology o : jenaModel.listOntologies().toSet())
		{
			namespaceSet.add(o.getURI() + "#");
		}

		namespaceSet.add(OWL.NS);

		for (String ns : namespaceSet)
		{
			String tempUri = ns + propertyId;

			if (jenaModel.contains(ResourceFactory.createResource(tempUri), RDF.type, OWL.DatatypeProperty)
					|| jenaModel.contains(ResourceFactory.createResource(tempUri), RDF.type, OWL.ObjectProperty))
			{
				iri = tempUri;
				break;
			}
			else
			{
				testedIris.add(tempUri);
			}
		}

		if (iri == null)
		{
			throw new RuntimeException("Could not find IRI for Property Id (" + propertyId + ") testedIris: " + testedIris);
		}

		OntProperty jenaProp;
		if ((jenaProp = jenaModel.getOntProperty(iri)) != null)
		{
			return new OntologyProperty(this, null, jenaProp);
		}
		else
		{
			throw new RuntimeException("Property with IRI<" + iri + "> Doesn't Exist");
		}
	}

	/**
	 * Returns a Map from propertyId to OntologyProperty object of all of the
	 * properties in this ontology
	 * 
	 * @return Map from propertyId to OntologyProperty of all properties in the
	 *         ontology
	 */
	public Map<String, OntologyProperty> getAllProperties()
	{
		Map<String, OntologyProperty> ret = getDataProperties();

		ret.putAll(getObjectProperties());

		return ret;
	}

	public void close(boolean soft)
	{
		// if (soft)
		// try
		// {
		// jenaModel.reset();
		// }
		// catch (NullPointerException ex)
		// {
		// System.out.println("Catching NPE from reset()");
		// ex.printStackTrace();
		// cache.remove(identifier);
		// jenaModel.close();
		// }
		// else
		// {
		// cache.remove(identifier);
		// jenaModel.close();
		// }
	}

	public Set<String> getDataPropertyIdList()
	{
		Set<String> ret = new HashSet<String>();

		for (DatatypeProperty p : jenaModel.listDatatypeProperties().toSet())
		{
			ret.add(new OntologyProperty(this, null, p).getId());
		}

		return ret;
	}

	public Set<String> getObjectPropertyIdList()
	{
		Set<String> ret = new HashSet<String>();

		for (ObjectProperty p : jenaModel.listObjectProperties().toSet())
		{
			ret.add(new OntologyProperty(this, null, p).getId());
		}

		return ret;
	}

	/**
	 * Returns a Map from propertyId to OntologyProperty object of all of the
	 * data properties in this ontology
	 * 
	 * @return Map from propertyId to OntologyProperty of all data properties in
	 *         the ontology
	 */
	public Map<String, OntologyProperty> getDataProperties()
	{
		Map<String, OntologyProperty> ret = new HashMap<String, OntologyProperty>();

		for (DatatypeProperty p : jenaModel.listDatatypeProperties().toSet())
		{
			OntologyProperty prop = new OntologyProperty(this, null, p);

			boolean isSubProp = false;
			for (OntologyProperty superProp : prop.getSuperProperties())
			{
				if (!superProp.getJenaProperty().hasProperty(OWL2.topDataProperty))
				{
					isSubProp = true;
				}
			}

			if (!isSubProp)
			{
				ret.put(prop.getId(), prop);
			}
		}

		return ret;
	}

	/**
	 * Returns a Map from propertyId to OntologyProperty object of all of the
	 * object properties in this ontology
	 * 
	 * @return Map from propertyId to OntologyProperty of all object properties
	 *         in the ontology
	 */
	public Map<String, OntologyProperty> getObjectProperties()
	{
		Map<String, OntologyProperty> ret = new HashMap<String, OntologyProperty>();

		for (OntProperty p : jenaModel.listObjectProperties().toSet())
		{
			OntologyProperty prop = new OntologyProperty(this, null, p);

			boolean isSubProp = false;
			for (OntologyProperty superProp : prop.getSuperProperties())
			{
				if (!superProp.getJenaProperty().hasProperty(OWL2.topObjectProperty))
				{
					isSubProp = true;
				}
			}

			if (!isSubProp)
			{
				ret.put(prop.getId(), prop);
			}

		}

		return ret;
	}

	public JSONObject query(String sparqlQueryString, boolean local)
	{

		JSONObject ret = new JSONObject();

		Query q = QueryFactory.create(sparqlQueryString);

		QueryExecution qExec = QueryExecutionFactory.create(q, jenaModel);

		try
		{
			if (q.isAskType())
			{
				if (qExec.execAsk())
				{
					ret.put("result", true);
				}
				else
				{
					ret.put("result", false);
				}
			}
			else
			{
				ResultSet results = qExec.execSelect();

				while (results.hasNext())
				{
					QuerySolution sol = results.nextSolution();

					Iterator<String> vars = sol.varNames();

					JSONObject result = new JSONObject();

					while (vars.hasNext())
					{
						String var = vars.next();
						RDFNode val = sol.get(var);

						// TODO: NEED TO MAKE SURE THIS WORKS FOR BOTH
						// INDIVIDUAL IDs AND LITERALS
						if (val.isResource())
						{ // ID
							if (!local || val.asResource().getURI().startsWith(baseIRI))
							{
								result.put(var, getIdentifier(val.asResource().getURI()));
							}
						}
						else
						{ // Literal
							result.put(var, val.asLiteral().getLexicalForm());
						}

					}

					ret.append("result", result);
				}
			}

		}
		catch (JSONException e)
		{

		}
		finally
		{
			qExec.close();
		}

		return ret;
	}

	public JSONArray findShortestPath(String startId, String endId, JSONArray pathRelationships)
	{
		JSONArray result = new JSONArray();

		Resource start = findResource(startId);
		;
		RDFNode end = findRDFNode(endId);

		Collection<Property> props = new ArrayList<Property>();
		for (int i = 0; i < pathRelationships.length(); i++)
		{
			String propId;
			try
			{
				propId = pathRelationships.getString(i);
			}
			catch (JSONException e)
			{
				throw new RuntimeException("Error Finding Shortest Path");
			}

			props.add(getProperty(propId).getJenaProperty());
		}

		Filter<Statement> filter = new OntTools.PredicatesFilter(props);

		List<Path> bfs = new LinkedList<Path>();
		Set<Resource> seen = new HashSet<Resource>();

		for (Iterator<Statement> it = jenaModel.listStatements(start, null, (RDFNode) null).filterKeep(filter); it.hasNext();)
		{
			bfs.add(new Path().append(it.next()));
		}

		Path solution = null;
		while (solution == null && !bfs.isEmpty())
		{
			Path candidate = bfs.remove(0);

			if (candidate.hasTerminus(end))
			{
				solution = candidate;
			}
			else
			{
				Resource terminus = candidate.getTerminalResource();
				if (terminus != null)
				{
					seen.add(terminus);

					// breadth-first expansion
					for (Iterator<Statement> i = terminus.listProperties().filterKeep(filter); i.hasNext();)
					{
						Statement link = i.next();

						// no looping allowed, so we skip this link if it takes
						// us to a node we've seen
						if (!seen.contains(link.getObject()))
						{
							bfs.add(candidate.append(link));
						}
					}
				}
			}
		}

		if (solution != null)
		{
			for (Statement stmt : solution)
			{
				JSONObject pathStep = new JSONObject();
				OntologyInstance sub = getInstance(getIdentifier(stmt.getSubject().getURI()));
				OntologyProperty prop = getProperty(getIdentifier(stmt.getPredicate().getURI()));
				OntologyInstance obj = getInstance(getIdentifier(stmt.getObject().asResource().getURI()));

				try
				{
					pathStep.put("subject", sub.getId());
					pathStep.put("property", prop.getId());
					pathStep.put("value", obj.getId());
					result.put(pathStep);
				}
				catch (JSONException e)
				{
					throw new RuntimeException("Error Parsing Path");
				}

			}
		}

		return result;
	}

	private Resource findResource(String resourceId)
	{
		return getInstance(resourceId).getJenaIndividual().asResource();
	}

	private RDFNode findRDFNode(String nodeId)
	{
		return getInstance(nodeId).getJenaIndividual();
	}

	public String getBaseIRI()
	{
		return baseIRI;
	}

	/* Jena Ontology Getter Functions */

	public OntDocumentManager getJenaManager()
	{
		return jenaManager;
	}

	public OntModel getJenaModel()
	{
		return jenaModel;
	}

	public String getTDBDir()
	{
		return tdbDir;
	}

	/* Ontology Wrapper Functions */

	@Override
	public JSONObject getJSONRepresentation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId()
	{
		return getBaseIRI();
	}

	@Override
	public String getFullId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getPrefix()
	{
		return jenaModel.listOntologies().toList().get(0).getURI();
	}
}

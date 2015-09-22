package com.eduworks.resolver;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwJson;
import com.eduworks.lang.util.EwUri;
import com.eduworks.resolver.enumeration.ResolverMatchOption;

/**
 * A class to iterate over matching items in an array. If elements in the array are equal they are returned. If they are
 * JSON values, they are returned depending on which of the following options is specified by the "match" key:
 * <ul>
 * <li><b>all</b> return elements if the key values in the item are equal to the key values in the element</li>
 * <li><b>any</b> return elements if any of the key values in an item match any key value in an element (default)</li>
 * <li><b>inArray</b> return elements if all the key values in an element match key values present in the item</li>
 * <li><b>inItem</b> return elements if all the key values in an item match key values present in an element</li>
 * </ul>
 * The "strict" key may also be present with a boolean value to indicate how the absence of any "match" key is
 * interpreted. If "true" then "match" defaults to "all"; otherwise it defaults to "any".
 * <br/><br/>
 * @author dharvey
 * @since 11/2011
 */
@Deprecated
public abstract class ResolverMatcher extends ResolverGetter
{
	protected boolean matchAll;
	protected boolean matchAllInCollection;
	protected boolean matchAllInItem;
	protected boolean matchAny;

	private boolean criteriaSet;

	/** @return true if item matches any of the values in the collection; false otherwise */
	protected boolean matches(EwJsonCollection coll, Object item) throws JSONException
	{
		if (coll == null || item == null)
			return false;

		if (EwJson.contains(coll, item))
			return true;

		return (matchAll(coll, item).length() > 0);
	}

	/**
	 * Iterate over collection for values that equal item, or for json elements that match item if it is json.
	 * @param coll a json collection of values to compare with item
	 * @param item the item to match against each collection element
	 * @return an {@link EwJsonCollection} of the same type as coll containing equal or matching values
	 * 		    from the collection if it is not null; otherwise, an empty {@link EwJsonObject}
	 * @throws JSONException
	 */
	protected EwJsonCollection matchAll(EwJsonCollection coll, Object item) throws JSONException
	{
		if (!criteriaSet) throw new RuntimeException("Matching criteria have not been set!");

		final EwJsonCollection matched = matchAll(EwJson.getEmptyInstance(coll), coll, item);

		return (matched != null) ? matched : new EwJsonObject();
	}

	protected EwJsonCollection matchAll(EwJsonCollection results, EwJsonCollection json, Object item) throws JSONException
	{
		if (item == null || json == null) return results;

		final boolean asArray = (results instanceof EwJsonArray);

		for (String key : json.keySet())
		{
			final Object element = tryParseJson(json.get(key));
			final Object ref = (asArray) ? results.length() : key;

			if (valuesMatch(element, item, true))
				results.put(ref, element);

			else if (EwJson.isJson(item) && EwJson.isJson(element))
			{
				final EwJsonCollection jsonItem = EwJson.tryConvert(item);
				final EwJsonCollection jsonElement = EwJson.tryConvert(element);

				if (matchJson(jsonItem, jsonElement)) results.put(ref, element);
			}
		}

		return results;
	}

	/**
	 * Perform a match between a json item and element. Matching criteria must be set beforehand.
	 * @param item the item to match against the array element
	 * @param element an element from a json array to compare with item
	 * @return true if the json element matches the json item according to match criteria; false otherwise
	 * @throws JSONException
	 */
	protected boolean matchJson(EwJsonCollection item, EwJsonCollection element) throws JSONException
	{
		if (!criteriaSet) throw new RuntimeException("Matching criteria have not been set!");

		// Null may mean invalid collection
		if (item == null || element == null) return false;

		// No comparisons between different collection types
		if (!(element.getClass().isInstance(item) || item.getClass().isInstance(element)))
			return false;

		final int elemLen = element.length();
		final int itemLen = item.length();

		// Array keys are indices: value matching is not index-specific
		final boolean compareAsArrays = (
				element instanceof EwJsonArray &&
				item instanceof EwJsonArray
			);

		// Difference in sizes means they don't match: go to next element
		if (matchAll && (elemLen != itemLen)) return false;

		// Element cannot match all the item's keys: go to next element
		if (matchAllInItem && (elemLen < itemLen)) return false;

		// Item cannot match all the element's keys: go to next element
		if (matchAllInCollection && (elemLen > itemLen)) return false;

		String key;

		if (matchAny || matchAllInItem)
		{
			final Iterator<String> itemKeys = item.keys();

			/* Try matching item values */

			ITEMS:
			for (int i = 0; i < itemLen; i++)
			{
				key = itemKeys.next();

				if (compareAsArrays)
				{
					// Compare all element values to this item
					final Object itemValue = item.get(key);

					for (int j = 0; j < elemLen; j++)
						if (valuesMatch(itemValue, element.get(j), false))
							if (matchAny || (!matchAllInCollection && i == (itemLen - 1)))
							{
								// At least one element value matched
								// or this was the last item to compare

								return true;
							}
							else continue ITEMS; // Item matched, try next

					if (matchAllInItem) return false; // Item matched no element
				}
				else if (element.hasComplex(key) && valuesMatch(element.get(key), item.get(key), false))
				{
					if (matchAny || (!matchAllInCollection && i == (itemLen - 1)))
					{
						// At least one item value matched
						// or we are done with this item:
						// element values will be skipped

						return true;
					}
				}
				else if (matchAllInItem)
				{
					// One item value didn't match
					// No need to test element values

					return false;
				}
			}
		}

		if (matchAllInCollection)
		{
			final Iterator<String> elemKeys = element.keys();

			/* Try matching all element values */

			ELEMENTS:
			for (int i = 0; i < elemLen; i++)
			{
				key = elemKeys.next();

				if (compareAsArrays)
				{
					// Compare all item values to this element
					final Object elemValue = element.get(key);

					for (int j = 0; j < itemLen; j++)
						if (valuesMatch(elemValue, item.get(j), false))
							if (i == (elemLen - 1))
								return true; // All element values matched
							else
								continue ELEMENTS; // Element matched, try next

					return false; // This element matched no item
				}
				else if (item.hasComplex(key) && valuesMatch(item.get(key), element.get(key), false))
				{
					if (i == (elemLen - 1))
						return true; // All element values matched
				}
				else if (compareAsArrays)
				{
					// Compare all element values to this item

					for (int j = 0; j < itemLen; j++)
						if (valuesMatch(item.get(key), element.get(j), false))
							if (matchAny || (!matchAllInCollection && i == (itemLen - 1)))
								return true;

					if (matchAllInItem) return false; // An item value didn't match
				}
				else break; // At least one didn't match
			}
		}

		return false;
	}

	protected EwJsonCollection removeFrom(EwJsonCollection coll, Object item) throws JSONException
	{
		if (!criteriaSet) throw new RuntimeException("Matching criteria have not been set!");

		return removeItem(EwJson.getEmptyInstance(coll), coll, item);
	}

	private EwJsonCollection removeItem(EwJsonCollection result, EwJsonCollection removeFrom, Object item) throws JSONException
	{
		final EwJsonCollection collection = EwJson.tryConvert(removeFrom);

		if (collection == null) return null;

		final boolean asArray = (result instanceof EwJsonArray);

		for (String key : collection.keySet())
		{
			final Object element = tryParseJson(collection.get(key));
			final Object ref = (asArray) ? result.length() : key;

			/* Compare as json with match criteria before comparing as objects */

			if (EwJson.isJson(item) && EwJson.isJson(element))
			{
				final EwJsonCollection jsonItem = EwJson.tryConvert(item);
				final EwJsonCollection jsonElement = EwJson.tryConvert(element);

				// Criteria based match as specified in setCriteria()
				if (!matchJson(jsonItem, jsonElement)) result.put(ref, element);
			}

			else if (!valuesMatch(element, item, true))
				result.put(ref, element);
		}

		return result;
	}

	/** Used for each item comparison. Override to customize item comparison in other methods. */
	protected boolean valuesMatch(Object item, Object element, boolean tryEncoding) throws JSONException
	{
		if (item == null || element == null)
			return false;

		if (item.equals(element))
			return true;

		if (tryEncoding && item instanceof String)
			return valuesMatch(EwUri.encodeValue((String)item), element, false);

		return false;
	}

	/** Sets matching criteria to the default if no other options are set. */
	protected void setMatchCriteria(Map<String,String[]> parameters, ResolverMatchOption defaultOption) throws JSONException
	{
		if (criteriaSet) return;

		ResolverMatchOption matchOption = optMatchOption(parameters);

		if (matchOption == null && defaultOption != null)
			matchOption = defaultOption;

		setMatchOption(matchOption, false);
	}

	/** Sets matching criteria to favor least constraining options, unless the "strict" option is specified. */
	protected void setMatchCriteria(Map<String,String[]> parameters, boolean defaultToStrict) throws JSONException
	{
		if (criteriaSet) return;

		setMatchOption(
				optMatchOption(parameters),
				optAsBoolean("_strict", defaultToStrict, parameters)
			);

		remove("_strict");
	}

	private ResolverMatchOption optMatchOption(Map<String,String[]> parameters) throws JSONException
	{
		final String matchOption = optAsString(ResolverMatchOption.DEFAULT_KEY, parameters);

		remove(ResolverMatchOption.DEFAULT_KEY);

		return ResolverMatchOption.optionForKeyValue(matchOption);
	}

	private void setMatchOption(ResolverMatchOption matchOption, boolean strict)
	{
		if (criteriaSet) return;

		if (matchOption == null)
			matchOption = (strict)
				? ResolverMatchOption.ALL
				: ResolverMatchOption.ANY;

		switch (matchOption)
		{
			case ALL:
				this.matchAll = true;
				this.matchAllInCollection = true;
				this.matchAllInItem = true;
				break;

			case ANY:
				this.matchAny = true;
				break;

			case IN_ARRAY:
			case IN_OBJECT:
				this.matchAllInCollection = true;
				break;

			case IN_ITEM:
				this.matchAllInItem = true;
				break;

		}

		this.criteriaSet = true;
	}
}

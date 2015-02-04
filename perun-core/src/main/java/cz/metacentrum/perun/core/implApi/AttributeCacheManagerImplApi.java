/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.metacentrum.perun.core.implApi;

import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.AttributeHolders;
import cz.metacentrum.perun.core.api.PerunBean;
import cz.metacentrum.perun.core.api.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Katarína Hrabovská <katarina.hrabovska1992@gmail.com>
 */
public interface AttributeCacheManagerImplApi {


	/**
	 * Get map with all objects and their attributes stored in cache.
	 *
	 * @author Katarina Hrabovska
	 * @return map of Objects(attributeHolders, String) and attributes
	 */
	Map<Object,Map<String,AttributeDefinition>> getApplicationCache();


	/**
	 * Remove all objects and attributes from cache.
	 *
	 * @author Katarina Hrabovska
	 */
	void flushCache();


	/**
	 * Add attribute to attributeHolders in cache. If attributeHolders is not in cache, it will be created.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeHolders couple of entities to add attribute to
	 * @param attribute attribute of attributeHolders
	 */
	void addAttributeToCache(AttributeHolders attributeHolders, Attribute attribute);

	/**
	 * Add attribute of attributeHolders to transaction.
	 * Retrieve actions for an actual transaction and store their to map. New attribute is added there.
	 * If an actual transaction is not active, it will be called method addAttributeToCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param secondaryHolder secondary entity of object AttributeHolders, can be null
	 * @param attribute attribute of object AttributeHolders
	 */
	void addAttributeToCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, Attribute attribute);

	/**
	 * Add attribute of attributeHolders to transaction.
	 * Call method addAttributeToCacheInTransaction for AttributeHolders, which secondaryHolder is null in.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param attribute attribute of object AttributeHolders
	 */
	void addAttributeToCacheInTransaction(PerunBean primaryHolder, Attribute attribute);

	/**
	 * Add attribute to cache for object String key.
	 *
	 * @author Katarina Hrabovska
	 * @param key key, that attribute is stored in cache for
	 * @param attribute
	 */
	void addAttributeToCacheForString(String key, Attribute attribute);

	/**
	 * Add attribute to transaction for object String key.
	 *
	 * @author Katarina Hrabovska
	 * @param key key, that attribute is stored in cache for
	 * @param attribute
	 */
	void addAttributeToCacheForStringInTransaction(String key, Attribute attribute);

	/**
	 * Add attributeDefinition to cache for key String Perun.
	 *
	 * @author Katarina Hrabovska
	 * @param attribute
	 */
	void addAttributeToCacheForAttributes(AttributeDefinition attribute);

	/**
	 * Add attributeDefinition to transaction.
	 * Retrieve actions for an actual transaction and store their to map. New attribute is added there.
	 * If an actual transaction is not active, it will be called method addAttributeToCacheForAttributes.
	 *
	 * @author Katarina Hrabovska
	 * @param attribute
	 */
	void addAttributeToCacheForAttributesInTransaction(AttributeDefinition attribute);

	/**
	 * Remove attribute of attributeHolders from cache. If attribute or attributeHolders are not in cache, nothing happen.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeHolders couple of entities to remove attribute of,
	 * @param attribute attribute of attributeHolders
	 */
	void removeAttributeFromCache(AttributeHolders attributeHolders, AttributeDefinition attribute);

	/**
	 * Add attribute of attributeHolders to transaction, until removing from DB.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is added there.
	 * If an actual transaction is not active, it will be called method removeAttributeFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param secondaryHolder secondary entity of object AttributeHolders, can be null
	 * @param attribute attribute of object AttributeHolders
	 */
	void removeAttributeFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, AttributeDefinition attribute);

	/**
	 * Add attribute of attributeHolders to transaction, until removing from DB.
	 * Call method removeAttributeFromCacheInTransaction for AttributeHolders, which secondaryHolder is null in.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param attribute attribute of object AttributeHolders
	 */
	void removeAttributeFromCacheInTransaction(PerunBean primaryHolder, AttributeDefinition attribute);

	/**
	 * Remove attribute for object String key from cache. If attribute or key are not in cache, nothing happen.
	 *
	 * @author Katarina Hrabovska
	 * @param key
	 * @param attribute
	 */
	void removeAttributeFromCacheForString(String key, AttributeDefinition attribute);

	/**
	 * Add attribute to transaction for object String key, until removing from DB.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is added there.
	 * If an actual transaction is not active, it will be called method removeAttributeFromCacheForString.
	 *
	 * @author Katarina Hrabovska
	 * @param key
	 * @param attribute
	 */
	void removeAttributeFromCacheForStringInTransaction(String key, AttributeDefinition attribute);

	/**
	 * Remove all attributes of attributeHolders from cache. If attributeHolders are not in cache or dont have any attributes, nothing happen.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeHolders couple of entities to remove attribute of
	 */
	void removeAllAttributesFromCache(AttributeHolders attributeHolders);

	/**
	 * Add all attributes of attributeHolders to transaction, until removing from DB.
	 * Retrieve actions for an actual transaction and store their to map. Attributes are added there.
	 * If an actual transaction is not active, it will be called method removeAllAtributesFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param secondaryHolder secondary entity of object AttributeHolders, can be null
	 */
	void removeAllAttributesFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder);

	/**
	 * Add all attributes of attributeHolders to transaction, until removing from DB.
	 * Call method removeAllAtributesFromCacheInTransaction for AttributeHolders, which secondaryHolder is null in.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 */
	void removeAllAttributesFromCacheInTransaction(PerunBean primaryHolder);

	/**
	 * Remove all attributes of couple User-Facility for any User from cache. If some couple dont have any attributes, nothing happen.
	 *
	 * @author Katarina Hrabovska
	 * @param secondaryHolder secondary entity of object AttributeHolders, specifically object of class Facility
	 */
	void removeAllUserFacilityAttributesForAnyUserFromCache(PerunBean secondaryHolder);

	/**
	 * Add all attributes of couple User-Facility for any User to transaction, until removing from DB.
	 * Retrieve actions for an actual transaction and store their to map. Attributes are added there.
	 * If an actual transaction is not active, it will be called method removeAllUserFacilityAttributesForAnyUserFromCache.
	 *
	 * @param secondaryHolder secondary entity of object AttributeHolders, specifically object of class Facility
	 */
	void removeAllUserFacilityAttributesForAnyUserFromCacheInTransaction(PerunBean secondaryHolder);


	/**
	 * Remove all attributes of couple User-Facility for the User and its all facilities from cache.
	 * If some couple dont have any attributes, nothing happen.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders, specifically object of class User
	 */
	void removeAllUserFacilityAttributesFromCache(PerunBean primaryHolder);


	/**
	 * Add all attributes of couple User-Facility for the User and its all facilities to transaction, until removing from DB.
	 * Retrieve actions for an actual transaction and store their to map. Attributes are added there.
	 * If an actual transaction is not active, it will be called method removeAllUserFacilityAttributesFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders, specifically object of class User
	 */
	void removeAllUserFacilityAttributesFromCacheInTransaction(PerunBean primaryHolder);

	/**
	 * Remove attribute from cache. If attribute is not in cache, nothing happen.
	 *
	 * @author Katarina Hrabovska
	 * @param attribute
	 */
	void removeAttributeFromCacheForAttributes(AttributeDefinition attribute);

	/**
	 * Add attribute to transaction, until removing from DB.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is added there.
	 * If an actual transaction is not active, it will be called method removeAttributeFromCacheForAttributes.
	 *
	 * @author Katarina Hrabovska
	 * @param attribute
	 */
	void removeAttributeFromCacheForAttributesInTransaction(AttributeDefinition attribute);

	/**
	 * Get attribute of attributeHolders from cache.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeHolders couple of entities to get attribute of,
	 * @param attributeName name of attribute
	 * @return attribute, null if attributeHolders or attribute is not in cache
	 */
	Attribute getAttributeFromCache(AttributeHolders attributeHolders, String attributeName);

	/**
	 * Get attribute of attributeHolders from transaction.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is looked for there.
	 * If an actual transaction is not active or attribute is not in, it will be called method getAttributeFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param secondaryHolder secondary entity of object AttributeHolders, can be null
	 * @param attributeName name of attribute
	 * @return attribute, null if attributeHolders or attribute is not in transaction or cache
	 */
	Attribute getAttributeFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, String attributeName);


	/**
	 * Get attribute of attributeHolder from transaction.
	 * Call method getAttributeFromCacheInTransaction for AttributeHolders, which secondaryHolder is null in.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder attributeHolder to get attribute of
	 * @param attributeName name of attribute
	 * @return attribute, null if attributeHolder or attribute is not in transaction or cache
	 */
	Attribute getAttributeFromCacheInTransaction(PerunBean primaryHolder, String attributeName);

	/**
	 * Get attribute for object String key from cache.
	 *
	 * @author Katarina Hrabovska
	 * @param key key, that attribute is got for
	 * @param attributeName
	 * @return attribute, null if key or attribute is not in cache
	 */
	Attribute getAttributeFromCacheForString(String key, String attributeName);

	/**
	 * Get attribute for object String key from transaction.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is looked for there.
	 * If an actual transaction is not active or attribute is not in, it will be called method getAttributeFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param key key, that attribute is got for
	 * @param attributeName
	 * @return attribute, null if key or attribute is not in transaction or cache
	 */
	Attribute getAttributeFromCacheForStringInTransaction(String key, String attributeName);

	/**
	 * Get all attributes of attributeHolders from cache.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeHolders couple of entities to get attributes of,
	 * @return list of attributes, can be empty, if attributeHolder is not in cache or it doesnt have any attributes.
	 */
	List<AttributeDefinition> getAllAttributesFromCache(AttributeHolders attributeHolders);


	/**
	 * Get all attributes of attributeHolders from transaction.
	 * Retrieve actions for an actual transaction and store their to map. Attributes are looked for there.
	 * Then it will be called method getAllAttributesFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param secondaryHolder secondary entity of object AttributeHolders, can be null
	 * @return list of attributes, can be empty, if attributeHolder is not in cache or it doesnt have any attributes.
	 */
	List<AttributeDefinition> getAllAttributesFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder);


	/**
	 * Get all attributes of attributeHolders from transaction.
	 * Call method getAllAttributesFromCacheInTransaction for AttributeHolders, which secondaryHolder is null in.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @return list of attributes, can be empty, if attributeHolder is not in cache or it doesnt have any attributes.
	 */
	List<AttributeDefinition> getAllAttributesFromCacheInTransaction(PerunBean primaryHolder);

	/**
	 * Get attribute of attributeHolders by Id from Cache.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeHolders couple of entities to get attribute of,
	 * @param id of attribute, which we want to get
	 * @return attribute, null if attributeHolders or attribute is not in cache
	 */
	Attribute getAttributeByIdFromCache(AttributeHolders attributeHolders, int id);

	/**
	 * Get attribute of attributeHolders by Id from transaction.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is looked for there.
	 * If an actual transaction is not active or attribute is not in, it will be called method getAttributeByIdFromCache.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param secondaryHolder secondary entity of object AttributeHolders, can be null
	 * @param id of attribute, which we want to get
	 * @return attribute, null if attributeHolders or attribute is not in transaction
	 */
	Attribute getAttributeByIdFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, int id);

	/**
	 * Get attribute of attributeHolders by Id from transaction.
	 * Call method getAttributeByIdFromCacheInTransaction for AttributeHolders, which secondaryHolder is null in.
	 *
	 * @author Katarina Hrabovska
	 * @param primaryHolder primary entity of object AttributeHolders
	 * @param id of attribute, which we want to get
	 * @return attribute, null if attributeHolders or attribute is not in transaction
	 */
	Attribute getAttributeByIdFromCacheInTransaction(PerunBean primaryHolder, int id);

	/**
	 * Get attributeDefinition from Cache.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeName
	 * @return attributeDefinition, null if attribute is not in cache
	 */
	AttributeDefinition getAttributeFromCacheForAttributes(String attributeName);

	/**
	 * Get attributeDefinition from transaction.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is looked for there.
	 * If an actual transaction is not active or attribute is not in, it will be called method getAttributeFromCacheForAttributes.
	 *
	 * @author Katarina Hrabovska
	 * @param attributeName
	 * @return attributeDefinition, null if attribute is not in transaction
	 */
	AttributeDefinition getAttributeFromCacheForAttributesInTransaction(String attributeName);

	/**
	 * Get attributeDefinition by Id from Cache.
	 *
	 * @author Katarina Hrabovska
	 * @param id of attribute, which we want to get
	 * @return attributeDefinition, null if attribute is not in transaction
	 */
	AttributeDefinition getAttributeByIdFromCacheForAttributes(int id);

	/**
	 * Get attributeDefinition by Id from transaction.
	 * Retrieve actions for an actual transaction and store their to map. Attribute is looked for there.
	 * If an actual transaction is not active or attribute is not in, it will be called method getAttributeByIdFromCacheForAttributes.
	 *
	 * @author Katarina Hrabovska
	 * @param id of attribute, which we want to get
	 * @return attributeDefinition, null if attribute is not in transaction
	 */
	AttributeDefinition getAttributeByIdFromCacheForAttributesInTransaction(int id);

	/**
	 * Unbind transaction resource.
	 */
	void clean();

	/**
	 * Commit actions from transaction to cache. For attributes of AttributeHolders with null value is called method removeAttributeFromCache and for others method addAttributeToCache.
	 * For attributes with String key and null value is called method removeAttributeFromCacheForString and for others method addAttributeToCacheForString.
	 * For attributeDefinition without key is called method removeAttributeFromCacheForAttributes and for others method addAttributeToCacheForAttributes.
	 * Flush transaction's map.
	 */
	void flush();
}

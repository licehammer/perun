package cz.metacentrum.perun.core.impl.modules.attributes;

import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.AttributesManager;
import cz.metacentrum.perun.core.api.User;
import cz.metacentrum.perun.core.api.exceptions.AttributeNotExistsException;
import cz.metacentrum.perun.core.api.exceptions.InternalErrorException;
import cz.metacentrum.perun.core.api.exceptions.WrongAttributeAssignmentException;
import cz.metacentrum.perun.core.api.exceptions.WrongReferenceAttributeValueException;
import cz.metacentrum.perun.core.impl.PerunSessionImpl;
import cz.metacentrum.perun.core.implApi.modules.attributes.UserVirtualAttributesModuleAbstract;
import cz.metacentrum.perun.core.implApi.modules.attributes.UserVirtualAttributesModuleImplApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This module determines if user is a researcher. If so,
 * it provides URL: 'http://www.ga4gh.org/beacon/bonafide/ver1.0'.
 *
 * The decision depends on attribute 'elixirBonaFideStatusREMS', if it
 * is not empty then the user is a researcher. If it is empty, then this module
 * searches in user's affiliations. If any of those affiliations starts
 * with 'faculty@' then the user is a researcher as well.
 * If none of above succeed, module looks into user:def:publications and if its value (map)
 * contains key "ELIXIR" and value of such key is > 0 then is researcher.
 * Otherwise, null value is set.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class urn_perun_user_attribute_def_virt_elixirBonaFideStatus extends UserVirtualAttributesModuleAbstract implements UserVirtualAttributesModuleImplApi {

	private final static Logger log = LoggerFactory.getLogger(urn_perun_user_attribute_def_virt_elixirBonaFideStatus.class);

	private final Pattern userRemsSetPattern = Pattern.compile("Attribute:\\[(.*)friendlyName=<elixirBonaFideStatusREMS>(.*)] set for User:\\[(.*)]");
	private final Pattern userRemsRemovePattern = Pattern.compile("AttributeDefinition:\\[(.*)friendlyName=<elixirBonaFideStatusREMS>(.*)] removed for User:\\[(.*)]");
	private final Pattern userPublicationsSetPattern = Pattern.compile("Attribute:\\[(.*)friendlyName=<publications>(.*)] set for User:\\[(.*)]");
	private final Pattern userPublicationsRemovePattern = Pattern.compile("AttributeDefinition:\\[(.*)friendlyName=<publications>(.*)] removed for User:\\[(.*)]");
	private final Pattern userAllAttrsRemovedPattern = Pattern.compile("All attributes removed for User:\\[(.*)]");
	private final Pattern uesAllAttrsRemovedPattern = Pattern.compile("All attributes removed for UserExtSource:\\[(.*)]");
	private final Pattern uesSetAffiliationAttributePattern = Pattern.compile("Attribute:\\[(.*)friendlyName=<affiliation>(.*)] set for UserExtSource:\\[(.*)]");
	private final Pattern uesRemoveAffiliationAttributePattern = Pattern.compile("AttributeDefinition:\\[(.*)friendlyName=<affiliation>(.*)] removed for UserExtSource:\\[(.*)]");

	private static final String FRIENDLY_NAME = "elixirBonaFideStatus";
	private static final String URL = "http://www.ga4gh.org/beacon/bonafide/ver1.0";

	private static final String USER_REMS_ATTR_NAME = "elixirBonaFideStatusREMS";
	private static final String USER_AFFILIATIONS_ATTR_NAME = "eduPersonScopedAffiliations";
	private static final String USER_PUBLICATIONS_ATTR_NAME = "publications";

	@Override
	public Attribute getAttributeValue(PerunSessionImpl sess, User user, AttributeDefinition attributeDefinition) throws InternalErrorException {
		Attribute attribute = new Attribute(attributeDefinition);

		// get value from 'elixirBonaFideStatusREMS'
		try {
			Attribute statusAttr = sess.getPerunBl().getAttributesManagerBl().getAttribute(sess, user, AttributesManager.NS_USER_ATTR_DEF + ":" + USER_REMS_ATTR_NAME);
			String statusAttrValue = (String) statusAttr.getValue();
			if (statusAttrValue != null && !statusAttrValue.isEmpty()) {
				attribute.setValue(URL);
			}
		} catch (WrongAttributeAssignmentException | AttributeNotExistsException e) {
			log.error("Cannot read {} from user {}", USER_REMS_ATTR_NAME, user, e);
		}

		// fallback on users affiliations
		if (attribute.getValue() == null) {
			try {
				Attribute userAttribute = sess.getPerunBl().getAttributesManagerBl().getAttribute(sess, user, AttributesManager.NS_USER_ATTR_VIRT + ":" + USER_AFFILIATIONS_ATTR_NAME);
				if (userAttribute.getValue() != null) {
					List<String> affiliations = (List<String>) userAttribute.getValue();
					for (String affiliation : affiliations) {
						if (affiliation != null && affiliation.startsWith("faculty@")) {
							attribute.setValue(URL);
							break;
						}
					}
				}
			} catch (WrongAttributeAssignmentException | AttributeNotExistsException e) {
				log.error("Cannot read {} from user {}", USER_AFFILIATIONS_ATTR_NAME, user, e);
			}
		}

		// fallback on reported publications with thanks to "ELIXIR".
		if (attribute.getValue() == null) {
			try {
				Attribute userAttribute = null;
				userAttribute = sess.getPerunBl().getAttributesManagerBl().getAttribute(sess, user, AttributesManager.NS_USER_ATTR_DEF + ":" + USER_PUBLICATIONS_ATTR_NAME);

				if (userAttribute.getValue() != null) {
					LinkedHashMap<String,String> publications = (LinkedHashMap<String,String>) userAttribute.getValue();
					if (publications.containsKey("ELIXIR")) {
						String value = publications.get("ELIXIR");
						try {
							int count = Integer.parseInt(value);
							if (count > 0) {
								attribute.setValue(URL);
							}
						} catch (NumberFormatException ex) {
							log.error("Attribute user:def:publications has wrong value for key ELIXIR", ex);
						}
					}
				}
			} catch (WrongAttributeAssignmentException | AttributeNotExistsException e) {
				log.error("Cannot read {} from user {}", USER_PUBLICATIONS_ATTR_NAME, user, e);
			}

		}

		return attribute;
	}

	@Override
	public List<String> resolveVirtualAttributeValueChange(PerunSessionImpl sess, String message) throws InternalErrorException, WrongReferenceAttributeValueException, AttributeNotExistsException, WrongAttributeAssignmentException {
		List<String> resolvingMessages = new ArrayList<>();
		if (message == null) return resolvingMessages;

		Matcher userRemsSetMatcher = userRemsSetPattern.matcher(message);
		Matcher userRemsRemoveMatcher = userRemsRemovePattern.matcher(message);
		Matcher userPublicationsSetMatcher = userPublicationsSetPattern.matcher(message);
		Matcher userPublicationsRemoveMatcher = userPublicationsRemovePattern.matcher(message);
		Matcher userAllAttrsRemovedMatcher = userAllAttrsRemovedPattern.matcher(message);
		Matcher uesAllAttrsRemovedMatcher = uesAllAttrsRemovedPattern.matcher(message);
		Matcher uesSetAffiliationAttributeMatcher = uesSetAffiliationAttributePattern.matcher(message);
		Matcher uesRemoveAffiliationAttributeMatcher = uesRemoveAffiliationAttributePattern.matcher(message);

		User user;
		Attribute attribute;

		if (uesSetAffiliationAttributeMatcher.find() ||	uesRemoveAffiliationAttributeMatcher.find() ||	uesAllAttrsRemovedMatcher.find() ||
				userRemsSetMatcher.find() || userRemsRemoveMatcher.find() || userAllAttrsRemovedMatcher.find() || userPublicationsSetMatcher.find() ||
				userPublicationsRemoveMatcher.find()) {

			user = sess.getPerunBl().getModulesUtilsBl().getUserFromMessage(sess, message);
			if (user != null) {
				String messageAttributeSet;

				attribute = sess.getPerunBl().getAttributesManagerBl().getAttribute(sess, user, AttributesManager.NS_USER_ATTR_VIRT + ":" + FRIENDLY_NAME);
				String value = (String) attribute.getValue();

				if (value == null || value.isEmpty()) {
					AttributeDefinition attributeDefinition = new AttributeDefinition(attribute);
					messageAttributeSet = attributeDefinition.serializeToString() + " removed for " + user.serializeToString() + ".";
				} else {
					messageAttributeSet = attribute.serializeToString() + " set for " + user.serializeToString() + ".";
				}
				resolvingMessages.add(messageAttributeSet);
			} else {
				log.error("Failed to get user from message: {}", message);
			}
		}
		return resolvingMessages;
	}

	@Override
	public AttributeDefinition getAttributeDefinition() {
		AttributeDefinition attr = new AttributeDefinition();
		attr.setNamespace(AttributesManager.NS_USER_ATTR_VIRT);
		attr.setFriendlyName(FRIENDLY_NAME);
		attr.setDisplayName("Bona fide researcher status");
		attr.setType(String.class.getName());
		attr.setDescription("Flag if user is qualified researcher. URI ‘http://www.ga4gh.org/beacon/bonafide/ver1.0’ value is provided if person is bona fide researcher. Empty value otherwise.");
		return attr;
	}
}

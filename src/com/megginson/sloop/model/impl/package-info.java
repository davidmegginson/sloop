/**
 * Implementation of the Sloop data model interfaces.
 * 
 * This package contains concrete implementations of the Sloop data interfaces, 
 * {@link com.megginson.sloop.model.DataCollection}, {@link com.megginson.sloop.model.DataRecord}, and 
 * {@link com.megginson.sloop.model.DataEntry}, along with a loader 
 * {@link com.megginson.sloop.model.impl.DataCollectionIO}.
 * 
 * These classes provide highly-optimized, read-only implementations of the interfaces — they avoid object
 * creation whenever possible, and use relatively little memory.
 * 
 * @author David Megginson
 */
package com.megginson.sloop.model.impl;